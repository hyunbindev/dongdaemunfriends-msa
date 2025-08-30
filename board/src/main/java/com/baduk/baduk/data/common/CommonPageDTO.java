package com.baduk.baduk.data.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPageDTO <D>{
	private boolean prePage = true;
	private boolean nextPage = true;
	private int pageNo;
	private long totalCount;
	Map<String,List<D>> content = new HashMap<>();
	
	/*
	 * @param json key값
	 * @param page 객체
	 * @param mapper함수
	 */
	//TODO 필요시 List로부터 DTO 만드는것도 추가
	public <T>CommonPageDTO(String valuName, Page<T> page, Function<T,D> mapper) {
		List<D> dto = page.getContent().stream()
				.map(mapper)
				.collect(Collectors.toList());
		this.content.put(valuName, dto);
		this.pageNo = page.getNumber();
		this.prePage = !page.isFirst();
		this.nextPage = !page.isLast();
		this.totalCount = page.getTotalElements();
	}
}