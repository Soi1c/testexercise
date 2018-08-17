package com.pestov.testexercise.mapper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMapper<S, T> implements Mapper<S, T> {

	@Override
	public T map(S source) {
		return map(source, null);
	}

	@Override
	public T map(S source, T target, Object mappingContext) {
		return map(source, target);
	}

	@Override
	public List<T> mapList(List<S> sourceList) {
		List<T> targetList = new ArrayList<>();
		if (sourceList != null && sourceList.size() != 0) {
			for (S source : sourceList) {
				targetList.add(map(source, null));
			}
		}
		return targetList;
	}

	@Override
	public List<T> mapList(List<S> sourceList, List<T> targetList) {
		return mapList(sourceList);
	}

	@Override
	public List<T> mapList(List<S> sourceList, List<T> targetList, Object mappingContext) {
		return mapList(sourceList, targetList);
	}
}
