package com.baduk.baduk.data.news;


import lombok.Getter;
import lombok.Setter;
/**
 * innerClass 로 묶어 관리하는거 괜찮은 방법같지만 가독성이 많이 떨어짐
 * => request 와 response만 묶어보는걸로 일단 분리하자
 * @author hyunbinDev
 */
public class NewsRequestDTO {
	@Getter
	public static class Create{
		@Setter
		private String authorUuid;
		private String title;
		private String text;
	}
}