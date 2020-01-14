package com.gmail.filoghost.quakecraft.objects;

import java.util.List;

public class CyclicIterator {

	private int index = 0;
	private int max = 0;
	
	
	public CyclicIterator(@SuppressWarnings("rawtypes") List list) {
		max = list.size();
		if (max == 0) {
			throw new IllegalArgumentException("CyclicIterator requires a list with at least 1 element");
		}
	}
	
	public CyclicIterator(Object[] array) {
		max = array.length;
		if (max == 0) {
			throw new IllegalArgumentException("CyclicIterator requires an array with at least 1 element");
		}
	}
	
	public int next() {
		int copy = index;
		
		index++;
		if (index >= max) {
			index = 0;
		}
		
		return copy;
	}
}
