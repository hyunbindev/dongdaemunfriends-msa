package com.baduk.baduk.data;

public interface ResponseDTOBuilder<E,O> {
	public void builder(E entity);
	public O build();
}
