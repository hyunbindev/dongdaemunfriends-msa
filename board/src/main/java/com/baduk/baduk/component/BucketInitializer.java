package com.baduk.baduk.component;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

/**
 * Bucket 초기화 공통 로직 분리
 * @author hyunbinDev
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BucketInitializer {
    private final S3Client minioS3SClient;
    private List<Bucket> buckets;

    
	@PostConstruct
    private void init() {
    	buckets = minioS3SClient.listBuckets().buckets();
    }
	/**
	 * 버켓 초기화 로직
	 * @param bucketName
	 */
	public void initBucket(String bucketName) {
		boolean exists = buckets.stream().anyMatch(bucket ->  bucket.name().equals(bucketName));
		if(!exists) {
			try {
				CreateBucketRequest headBucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build();
				minioS3SClient.createBucket(headBucketRequest);
			}catch(Exception e) {
				log.error(e.getMessage());
			}
		}
	}
}
