package com.baduk.baduk.service.persona;

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
import com.baduk.baduk.exception.PersonaException;
import com.baduk.baduk.service.FileService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
/**
 * @author hyunbinDev
 * @since 2025-06-14
 * 동대문 보이스 오디오 파일 저장 서비스
 */
@Slf4j
@Service("voiceMinioFileSerivce")
@RequiredArgsConstructor
public class PersonaFileService implements FileService{
	@Qualifier("minioS3Client")
	private final S3Client minioS3SClient;
	
	@Qualifier("minioS3Presigner")
	private final S3Presigner minioS3Presigner;
	
	private final BucketInitializer bucketInitializer;
	
	@Value("${minio.bucket.bucket-name.persona}")
	private String bucketName;
	/**
	 * @author hyunbinDev
	 * @since 2025-06-14
	 * voiceFilerService 초기화
	 * 버켓 존재하지 않을 시 버켓 생성
	 */
	@PostConstruct
	private void init() {
		bucketInitializer.initBucket(bucketName);
	}
	
	/**
	 * @author hyunbinDev
	 * @since 2025-06-14
	 * 파일저장
	 */
	@Override
	public String fileUpload(MultipartFile file){
		String fileName = UUID.randomUUID().toString();
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
											.bucket(bucketName)
											.key(fileName)
											.contentType(file.getContentType())
											.build();
		
		try {
			//voice 파일 저장
			minioS3SClient.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
		} catch (AwsServiceException | SdkClientException | IOException e) {
			log.error("Voice file upload failed", e);
			//저장실패시 runtime Exception Throw
			throw new PersonaException(FileExceptionConst.FAIL_TO_SAVE);
		}
		return fileName;
	}
	/**
	 * @author hyunbinDev
	 * @since 2025-06-14
	 * 이미지 url 조회
	 */
	@Override
	public Map<String, URL> getFileUrl(List<String> fileUuidList) {
		Map<String,URL> presignedUrlMap = new HashMap<>();
		
		// key : fileuuid , URL : presignedURL 형식으로 매핑
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
			throw new PersonaException(FileExceptionConst.FAIL_TO_SAVE);
		}
	}
	/**
	 * @param fileUuid
	 * @return 20분의 유효기간을 갖는 presigned URL 객체
	 * @since 2025-06-15
	 * 자원의 GET 요청
	 */
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