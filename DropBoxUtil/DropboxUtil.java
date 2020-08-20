package com.example.util;

import java.io.File;
//import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.IOException;
import java.io.InputStream;
//import java.util.Date;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadSessionCursor;
import com.dropbox.core.v2.files.UploadSessionFinishErrorException;
import com.dropbox.core.v2.files.UploadSessionLookupErrorException;
import com.dropbox.core.v2.files.WriteMode;

public class DropboxUtil {
	private static final String accesstoken = "k_DvLaOA2ZAAAAAAAAAAC-86a5zLIg5nurgDpDuQqZgdIqRAFf9lnjCLJUOPmcEa";
	static DbxRequestConfig config = new DbxRequestConfig(
			"dropbox/APPLICATION-NAME-HERE");
	static DbxClientV2 client = new DbxClientV2(config, accesstoken);
	static final Long CHUNKED_UPLOAD_CHUNK_SIZE = 4 * 1024 * 1024L;// 4mb
	static final int CHUNKED_UPLOAD_MAX_ATTEMPTS = 5;

	public static boolean  uploadToDropbox(String filename,InputStream in,long size,String path) {
		System.out.println("Entered dropboxutil");
		
		if (size < CHUNKED_UPLOAD_CHUNK_SIZE) {
			try {
			
				 client.files()
						.uploadBuilder(path + filename)
						.withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
				 return true;
			} catch (Exception e) {
				e.getStackTrace();
				return false;
			}
		} else {
			boolean status=uploadInChunks(filename,in,size,path);
			return status;
		}
	}

	public static boolean uploadInChunks(String filename,InputStream in,final long size,String path) {
		long uploaded = 0L;
		DbxException thrown = null;
		ProgressListener progressListener = new ProgressListener() {
			long uploadedBytes = 0;

			@Override
			public void onProgress(long l) {
				printProgress(l + uploadedBytes, size);
				if (l == CHUNKED_UPLOAD_CHUNK_SIZE)
					uploadedBytes += CHUNKED_UPLOAD_CHUNK_SIZE;
			}
		};

		String sessionId = null;
		for (int i = 0; i < CHUNKED_UPLOAD_MAX_ATTEMPTS; ++i) {
			if (i > 0) {
				System.out.printf(
						"Retrying chunked upload (%d / %d attempts)\n", i + 1,
						CHUNKED_UPLOAD_MAX_ATTEMPTS);
			}

			try {//(InputStream in = new FileInputStream(file)) {
				// if this is a retry, make sure seek to the correct offset
				in.skip(uploaded);

				// (1) Start
				if (sessionId == null) {
					sessionId = client
							.files()
							.uploadSessionStart()
							.uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE,
									progressListener).getSessionId();
					uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
					printProgress(uploaded, size);
				}

				UploadSessionCursor cursor = new UploadSessionCursor(sessionId,
						uploaded);

				// (2) Append
				while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
					client.files()
							.uploadSessionAppendV2(cursor)
							.uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE,
									progressListener);
					uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
					printProgress(uploaded, size);
					cursor = new UploadSessionCursor(sessionId, uploaded);
				}

				// (3) Finish
				long remaining = size - uploaded;
				CommitInfo commitInfo = CommitInfo
						.newBuilder(path + filename)
						.withMode(WriteMode.OVERWRITE)
						//.withClientModified(new Date(file.lastModified()))
						.build();
				FileMetadata metadata = client.files()
						.uploadSessionFinish(cursor, commitInfo)
						.uploadAndFinish(in, remaining, progressListener);

				System.out.println(metadata.toStringMultiline());
				return true;
			} catch (RetryException ex) {
				thrown = ex;
				// RetryExceptions are never automatically retried by the client
				// for uploads. Must
				// catch this exception even if DbxRequestConfig.getMaxRetries()
				// > 0.
				sleepQuietly(ex.getBackoffMillis());
				continue;
			} catch (NetworkIOException ex) {
				thrown = ex;
				// network issue with Dropbox (maybe a timeout?) try again
				continue;
			} catch (UploadSessionLookupErrorException ex) {
				if (ex.errorValue.isIncorrectOffset()) {
					thrown = ex;
					// server offset into the stream doesn't match our offset
					// (uploaded). Seek to
					// the expected offset according to the server and try
					// again.
					uploaded = ex.errorValue.getIncorrectOffsetValue()
							.getCorrectOffset();
					continue;
				} else {
					// Some other error occurred, give up.
					System.err.println("Error uploading to Dropbox: "
							+ ex.getMessage());

					return false;
				}
			} catch (UploadSessionFinishErrorException ex) {
				if (ex.errorValue.isLookupFailed()
						&& ex.errorValue.getLookupFailedValue()
								.isIncorrectOffset()) {
					thrown = ex;
					// server offset into the stream doesn't match our offset
					// (uploaded). Seek to
					// the expected offset according to the server and try
					// again.
					uploaded = ex.errorValue.getLookupFailedValue()
							.getIncorrectOffsetValue().getCorrectOffset();
					continue;
				} else {
					// some other error occurred, give up.
					System.err.println("Error uploading to Dropbox: "
							+ ex.getMessage());

					return false;
				}
			} catch (DbxException ex) {
				System.err.println("Error uploading to Dropbox: "
						+ ex.getMessage());

				return false;
			} catch (IOException ex) {
				System.err.println("Error reading from file \"" + filename + "\": "
						+ ex.getMessage());

				return false;
			}
		}

		// if we made it here, then we must have run out of attempts
		System.err
				.println("Maxed out upload attempts to Dropbox. Most recent error: "
						+ thrown.getMessage());

		return false;
	}

	private static void printProgress(long uploaded, long size) {
		System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded,
				size, 100 * (uploaded / (double) size));
	}

	private static void sleepQuietly(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
			// just exit
			System.err
					.println("Error uploading to Dropbox: interrupted during backoff.");
			System.exit(1);
		}
	}

	public static boolean downloadFromDropbox(String path,String filename) {
		try {
			DbxDownloader<FileMetadata> downloader = client.files().download(
					"/encryptedFiles/" + filename);
			File outputfile = new File(path+filename);
			FileOutputStream out = new FileOutputStream(outputfile);
			downloader.download(out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
