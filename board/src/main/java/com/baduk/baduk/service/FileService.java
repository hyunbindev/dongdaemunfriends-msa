package com.baduk.baduk.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
/**
 * @author 김현빈
 * @since 2025-07-19
 * 전략 패턴을 사용하기 위해 추상해 놓았지만 코드중복이 너무 많이 일어남
 * 구채화 할 필요가 있음
 */
public interface FileService {
	/**
	 * 단일 파일 업로드
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public String fileUpload(MultipartFile file);
	/**
	 * 파일 접근 url 조회
	 * @param fileUuid
	 * @return
	 */
	public Map<String, URL> getFileUrl(List<String> fileUuidList);
	/**
	 * 파일 삭제
	 * @param fileUuid
	 */
	public void deleteFile(String fileUuid);
}