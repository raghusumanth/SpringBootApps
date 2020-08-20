package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.bean.FilePart;
import com.example.bean.FileStorage;
import com.example.bean.FileStorageProperties;
import com.example.exception.FileStorageException;
import com.example.exception.MyFileNotFoundException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.FilePartRepo;
import com.example.response.ApiResponse;
import com.example.util.DropboxUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

@Service
public class FileStorageService {

	@Autowired
	private FilePartRepo filePartRepo;

	@Autowired
	private FileStorage fileStorage;

	public ApiResponse fileUpload(Long id, MultipartFile file) throws IOException{

		FilePart filePart = new FilePart();
		filePartRepo.save(filePart);
		String filename = "";
		filename = generateFileName(id, "file", file.getOriginalFilename());
		try {
			String directoryName = fileStorage.getUploadDir() + "/file/";
			File directory = new File(directoryName);
			if (!directory.exists()){
				directory.mkdir();
			}

			byte[] bytes = file.getBytes();
			Path path = Paths.get(directoryName + filename);
			Files.write(path, bytes);
			
			//Uploading to Dropbox
			File uploadedFile=new File(directoryName + filename);
			InputStream is = new FileInputStream(uploadedFile);
			String dropBoxUploadPath="/uploadedFiles/";
			boolean uploadStatus=DropboxUtil.uploadToDropbox(file.getOriginalFilename(), is, uploadedFile.length(),dropBoxUploadPath);
			if(uploadStatus) {
				System.out.println("Uploaded normal file successfully");
			}else {
				System.out.println("Failed while uploading normal file");
			}
			//Uploaded to Dropbox
			
			
			FilePart update = filePartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DiscussionTopic","topicId", id));
			update.setFileName(filename);
			filePartRepo.save(update);
			//			directoryName = fileStorage.getUploadDir()+ "/file/"+ filename;
			File dir = new File(directoryName+filename);
			if(dir.exists()) {
				String splitName = dir.getName().substring(0, dir.getName().lastIndexOf("."));
				File splitFile = new File(directoryName+"split");
				if (!splitFile.exists()) {
					splitFile.mkdirs();
					System.out.println("Directory Created -> "+ splitFile.getAbsolutePath());
				}
				int i = 01;// Files count starts from 1
				InputStream inputStream = new FileInputStream(dir);
				String videoFile = splitFile.getAbsolutePath() +"/"+ String.format("%02d", i) +"_";
				OutputStream outputStream = new FileOutputStream(videoFile+filename);
				System.out.println("File Created Location: "+ videoFile+filename);
				int totalPartsToSplit = 10;// Total files to split.
				int splitSize = inputStream.available() / totalPartsToSplit;
				int streamSize = 0;
				int read = 0;
				while ((read = inputStream.read()) != -1) {

					if (splitSize == streamSize) {
						if (i != totalPartsToSplit) {
							i++;
							String fileCount = String.format("%02d", i); // output will be 1 is 01, 2 is 02
							videoFile = splitFile.getAbsolutePath() +"/"+ fileCount +"_";
							outputStream = new FileOutputStream(videoFile+filename);
							System.out.println("File Created Location: "+ videoFile+filename);
							streamSize = 0;
						}
					}
					outputStream.write(read);
					streamSize++;
				}
				inputStream.close();
				outputStream.close();
				System.out.println("Total files Split ->"+ totalPartsToSplit);
			}else {
				System.err.println(dir.getAbsolutePath() +" File Not Found.");
			}

			return new ApiResponse(LocalDateTime.now(), HttpStatus.OK, "File Upload successfully", null);
		}catch (IOException e) {
			e.printStackTrace();
			return new ApiResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE, "IOException", null);
		}
	}

	public ApiResponse getFile(Long id) {
		FilePart update = filePartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DiscussionTopic","topicId", id));
		try {
			String directoryName = fileStorage.getUploadDir() + "/file/";
			System.out.println(directoryName);
			File splitFiles = new File(directoryName+"/split/");// get all files which are to be join
			if (splitFiles.exists()) {
				File[] files = splitFiles.getAbsoluteFile().listFiles();
				if (files.length != 0) {
					System.out.println("Total files to be join: "+ files.length);

					String joinFileName = Arrays.asList(files).get(0).getName();
					System.out.println("Join file created with name -> "+ joinFileName);

					String fileName = joinFileName.substring(0, joinFileName.lastIndexOf("."));// video fileName without extension
					File fileJoinPath = new File(directoryName+"/download");// merge video files saved in this location"
					if (!fileJoinPath.exists()) {
						fileJoinPath.mkdirs();
						System.out.println("Created Directory -> "+ fileJoinPath.getAbsolutePath());
					}

					OutputStream outputStream = new FileOutputStream(fileJoinPath.getAbsolutePath() +"/"+ joinFileName);

					for (File file : files) {
						System.out.println("Reading the file -> "+ file.getName());
						InputStream inputStream = new FileInputStream(file);

						int readByte = 0;
						while((readByte = inputStream.read()) != -1) {
							outputStream.write(readByte);
							
						}
						inputStream.close();
					}
					FilePart obj = filePartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DiscussionTopic","topicId", id));
					obj.setFileName(fileJoinPath.getAbsolutePath() +"/"+ joinFileName);
					filePartRepo.save(obj);
					System.out.println("Join file saved at -> "+ fileJoinPath.getAbsolutePath() +"/"+ joinFileName);
					outputStream.close();
				} else {
					System.err.println("No Files exist in path -> "+ splitFiles.getAbsolutePath());
				}
			} else {
				System.err.println("This path doesn't exist -> "+ splitFiles.getAbsolutePath());
			}
			return new ApiResponse(LocalDateTime.now(), HttpStatus.OK, "File download successfully", null);
		}catch(Exception e) {
			e.printStackTrace();
			return new ApiResponse(LocalDateTime.now(), HttpStatus.OK, "somthing wrong", null);

		}
	}


	private String generateFileName(Long csrId, String type, String filename) {
		String extenstion = filename.substring(filename.lastIndexOf(".")+1, filename.length());
		return type + "_"+ csrId + "." + extenstion;
	}
}
