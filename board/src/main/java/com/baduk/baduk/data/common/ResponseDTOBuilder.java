package com.baduk.baduk.data.common;

public interface ResponseDTOBuilder<E,O> {
	public void builder(E entity);
	public O build();
}
