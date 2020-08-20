package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.bean.FilePart;
import com.example.bean.FileStorage;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.FilePartRepo;
import com.example.response.ApiResponse;
import com.example.service.FileStorageService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FilePartRepo filePartRepo;

	@Autowired
	private FileStorage fileStorage;

	@PostMapping("/{id}/upload")
	public ApiResponse fileUpload(
			@PathVariable(name = "id", required = true) Long id,
			@RequestParam(name = "file", required = true) MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return new ApiResponse(LocalDateTime.now(), HttpStatus.OK, "Empty file", null);
		}	
		return fileStorageService.fileUpload(id, file);
	}

	@GetMapping("/{id}/download")
	public ApiResponse downloadPDFResource(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable(name="id", required = true) Long id) throws IOException {
		FilePart update = filePartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DiscussionTopic","topicId", id));
		try {
			String directoryName1 = fileStorage.getUploadDir() + "/file/";
			File splitFiles = new File(directoryName1+"/split/");// get all files which are to be join
			if (splitFiles.exists()) {
				File[] files = splitFiles.getAbsoluteFile().listFiles();
				if (files.length != 0) {
					System.out.println("Total files to be join: "+ files.length);

					String joinFileName = Arrays.asList(files).get(0).getName();
					System.out.println("Join file created with name -> "+ joinFileName);

					String fileName = joinFileName.substring(0, joinFileName.lastIndexOf("."));// video fileName without extension
					File fileJoinPath = new File(directoryName1+ "/download");// merge video files saved in this location

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
					obj.setFileName(joinFileName);
					filePartRepo.save(obj);
					System.out.println("Join file saved at -> "+ fileJoinPath.getAbsolutePath() +"/"+ joinFileName);
					outputStream.close();
				} else {
					System.err.println("No Files exist in path -> "+ splitFiles.getAbsolutePath());
				}
			} else {
				System.err.println("This path doesn't exist -> "+ splitFiles.getAbsolutePath());
			}
			String directoryName = fileStorage.getUploadDir() + "/file/download/";
			String jobResumeName = update.getFileName();
			String extenstion = jobResumeName.substring(jobResumeName.lastIndexOf(".")+1, jobResumeName.length());
			String fileName = "01_file_" + id + "." + extenstion;
			File file = new File(directoryName+fileName);
			try {
			if (file.exists()) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				response.setContentType(mimeType);

				response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				FileCopyUtils.copy(inputStream, response.getOutputStream());
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return new ApiResponse(LocalDateTime.now(), HttpStatus.OK, "somthing wrong", null);

		}

	}

}