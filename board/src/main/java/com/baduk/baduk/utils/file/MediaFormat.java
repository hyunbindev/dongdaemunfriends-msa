package com.baduk.baduk.utils.file;

public enum MediaFormat{
	WEBP("image/webp","webp"),
	JPEG("image/jpeg","jpg"),
	PNG("image/png","png");
	
	public final String MIME;
	public final String EXTENSION;
	
	MediaFormat(String mimeType, String extension) {
		this.MIME = mimeType;
		this.EXTENSION = extension;
	}
}