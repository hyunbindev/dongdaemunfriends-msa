package com.baduk.baduk.utils.file;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.baduk.baduk.constants.excpetion.FileExceptionConst;
import com.baduk.baduk.exception.FileIOException;

import lombok.extern.slf4j.Slf4j;

/**
 * 이미지 webp 변환 util Class
 * 
 * 추후 writers 가 사용가능한지 등은 spring 기동시에 체크하고 writer등 메모리에 미리 적재 할필 및
 * 최적화 필요
 * 추후 Component로 분리할 예정
 * @author hyunbinDev
 * 
 */
@Slf4j
public class ImageConvertor {
	//이미지 품질 명시적으로 지정
	private static final float QUALITY = 0.8f;
	
	public static byte[] convert(MultipartFile image ,MediaFormat convertFormatType){
		String MIME_TYPE = image.getContentType();
		
		//이미지 파일이 아니라면 throw exception
		if(MIME_TYPE == null || !MIME_TYPE.startsWith("image/")) throw new FileIOException(FileExceptionConst.INVALID_FILE_TYPE);

		String formatName = MIME_TYPE.split("/")[1]; 
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
		
		
		//처리할 수 있는 imageWriter 가 없으면 throw Exception
		if (!writers.hasNext()) throw new FileIOException(FileExceptionConst.INVALID_FILE_TYPE,"imager writer 없음");
		

		BufferedImage inputImage=null;
		
		try {
			inputImage = ImageIO.read(image.getInputStream());
		} catch (IOException e) {
			log.info(e.fillInStackTrace().getMessage());
			throw new FileIOException(FileExceptionConst.FAIL_TO_SAVE);
		}
		
		ImageWriter writer = writers.next();
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		try(ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream)){
			writer.setOutput(imageOutputStream);
			
			ImageWriteParam writerParam = writer.getDefaultWriteParam();
			
			if(writerParam.canWriteCompressed()) {
				writerParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				writerParam.setCompressionQuality(QUALITY);
			}
			writer.write(null, new IIOImage(inputImage,null, null), writerParam);
		}catch(IOException e) {
			log.info(e.fillInStackTrace().getMessage());
			throw new FileIOException(FileExceptionConst.FAIL_TO_SAVE);
		}finally{
			writer.dispose();
		}
		//webp byte 배열 자료형으로 리턴
		return byteArrayOutputStream.toByteArray();
	}
}