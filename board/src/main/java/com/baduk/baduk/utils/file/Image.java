package com.baduk.baduk.utils.file;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Image implements File{
	MediaFormat mediaFormat;
	byte[] byteData;
	int width;
    int height;
	@Override
	public byte[] getByteData() {
		return this.byteData;
	}
}