package com.baduk.baduk.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
/**
 * @author hyunbin
 * @since2025-06-14
 * 미디어파일 저장 및 서빙을 위한 S3Client Configuration
 */
@Configuration
public class S3ClientConfig {
	//MINIO ENDPOINT URL
	@Value("${minio.url}")
	private URI MINIO_URL;
	
	//MINIO accessKey
	@Value("${minio.access-key}")
	private String MINIO_ACCESS_KEY;
	
	//MINIO secretKey
	@Value("${minio.secret-key}")
	private String MINIO_SECRET_KEY;
	
	//MINIO S3 CLIENT BEAN 정의
	@Bean(name = "minioS3Client")
	public S3Client MinIOS3Client() {
		return S3Client.builder()
				//MINIO_URL
				.endpointOverride(MINIO_URL)
				//credential 설정
				.credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)))
				//region 동북아시아
				.region(Region.AP_NORTHEAST_1)
				.forcePathStyle(true)
				.build();
	}
	@Bean(name="minioS3Presigner")
	public S3Presigner presigner() {
		return S3Presigner.builder()
						  //Minio Endpoint 설정
						  .endpointOverride(MINIO_URL)
						  //Minio 인증 정보 설정
						  .credentialsProvider(StaticCredentialsProvider.create(
			                        AwsBasicCredentials.create(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)))
						  //리전 설정
						  .region(Region.AP_NORTHEAST_1)
						  .serviceConfiguration(S3Configuration.builder()
							        .pathStyleAccessEnabled(true)
							        .build())
						  .build();
	}
}
