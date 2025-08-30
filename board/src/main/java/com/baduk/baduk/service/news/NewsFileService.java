package com.baduk.baduk.service.news;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.component.BucketInitializer;
import com.baduk.baduk.constants.excpetion.FileExceptionConst;
import com.baduk.baduk.exception.NewsException;
import com.baduk.baduk.service.FileService;
import com.baduk.baduk.utils.file.MediaFormat;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Service("newsMinioFileService")
@RequiredArgsConstructor
public class NewsFileService implements FileService{
	
	@Qualifier("minioS3Client")
	private final S3Client minioS3SClient;
	
	@Qualifier("minioS3Presigner")
	private final S3Presigner minioS3Presigner;
	
	private final BucketInitializer bucketInitializer;
	
	@Value("${minio.bucket.bucket-name.news}")
	private String bucketName;
	
	@PostConstruct
	private void init() {
		bucketInitializer.initBucket(bucketName);
	}
	
	@Override
	public String fileUpload(MultipartFile file) {
		String fileName = UUID.randomUUID().toString();
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(fileName)
				.contentType(file.getContentType())
				.build();
		try {
			minioS3SClient.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
			
		} catch (AwsServiceException | SdkClientException | IOException e) {
			log.error("news file upload failed", e);
			throw new NewsException(FileExceptionConst.FAIL_TO_SAVE);
		}
		
		return fileName;
	}
	/**
	 * 파일 추상화 이전 임시 메서드 리팩토링 시 deprecated 예정
	 * @param fileByte
	 * @param foramt
	 * @return
	 */
	public String fileUploadByByteArray(byte[] fileByte, MediaFormat foramt) {
		String fileName = UUID.randomUUID().toString();
		
		if(fileByte.length == 0 | fileByte == null) throw new NewsException(FileExceptionConst.FAIL_TO_SAVE);
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(fileName)
				.contentType(foramt.MIME)
				.build();
		try {
			minioS3SClient.putObject(putObjectRequest, RequestBody.fromBytes(fileByte));
			
		} catch (AwsServiceException | SdkClientException e) {
			log.error("news file upload failed", e);
			throw new NewsException(FileExceptionConst.FAIL_TO_SAVE);
		}
		
		return fileName;
	}


	@Override
	public Map<String, URL> getFileUrl(List<String> fileUuidList) {
		Map<String,URL> presignedUrlMap = new HashMap<>();
		fileUuidList.stream().forEach(fileUuid -> presignedUrlMap.put(fileUuid, getPresignedUrl(fileUuid)));
		return presignedUrlMap;
	}

	@Override
	public void deleteFile(String fileUuid) {
		try {
			DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
					.bucket(bucketName)
					.key(fileUuid)
					.build();
			minioS3SClient.deleteObject(deleteRequest);
		}catch(S3Exception e) {
			throw new NewsException(FileExceptionConst.FAIL_TO_SAVE);
		}
	}
	
	private URL getPresignedUrl(String fileUuid) {
		
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				 							 .bucket(bucketName)
				 							 .key(fileUuid)
				 							 .build();
		
		 GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
		            									   .getObjectRequest(getObjectRequest)
		            									   .signatureDuration(Duration.ofMinutes(20))
		            									   .build();
		 
		 return minioS3Presigner.presignGetObject(getObjectPresignRequest).url();
	}
}
