package com.baduk.baduk.utils;

import java.util.Random;
/**
 * @author hyunbinDev
 * @since 2025-06-18
 * 익명 랜덤 이름 생성
 */
public class AnonNameUtil {
	private static final Random RANDOM = new Random();
	//익명 이름 형용사
	private static final String[] ADJECTIVES = {"무자비한",
												"눈치없는",
												"요망한",
												"발칙한",
												"음흉한",
												"치명적인",
												"잔인한",
												"야릇한",
												"야만적인"
												};
	//익명 이름 명사
	private static final String[] NOUNS = {"박춘배",
							 			   "김춘식",
							 			   "김동수",
										   "홍춘자",
										   "이미영",
										   "배달혜",
										   "송점순",
										   "김금자",
										   "구자옥",
										   "전복남",
										   "박금자"
										 };
	public static String getAnonName() {
		int randAdjectiveIndex = RANDOM.nextInt(ADJECTIVES.length);
		int randNounIndex = RANDOM.nextInt(NOUNS.length);
		
		return ADJECTIVES[randAdjectiveIndex]+" "+ NOUNS[randNounIndex];
	}
}
