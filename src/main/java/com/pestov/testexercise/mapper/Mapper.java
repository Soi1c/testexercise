package com.pestov.testexercise.mapper;

import java.util.List;

public interface Mapper<S, T> {

	T map(S source);

	T map(S source, T target);

	T map(S source, T target, Object mappingContext);

	List<T> mapList(List<S> sourceList);

	List<T> mapList(List<S> sourceList, List<T> targetList);

	List<T> mapList(List<S> sourceList, List<T> targetList, Object mappingContext);
}
