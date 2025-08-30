package com.baduk.baduk.service.blame;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author hyunbinDev
 * @since 2025-06-14
 * 저격글 이미지 저장 서비스
 * 아직 구현 예정
 */
@Slf4j
@RequiredArgsConstructor
@Service("blameMinioFileService")
public class BlameImageService implements FileService{
	@Qualifier("minioS3SClient")
	private final S3Client minioS3SClient;
	
	private final String bucketName = "blame";

	@Override
	public String fileUpload(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, URL> getFileUrl(List<String> fileUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(String fileUuid) {
		// TODO Auto-generated method stub
		
	}
}