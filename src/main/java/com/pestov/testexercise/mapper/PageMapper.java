package com.pestov.testexercise.mapper;

import com.pestov.testexercise.dto.PageDto;
import com.pestov.testexercise.models.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper extends AbstractMapper<Page, PageDto> {


	@Override
	public PageDto map(Page source, PageDto target) {
		if (source == null) return null;
		if (target == null) target = new PageDto();

		target.setId(source.getId());
		target.setNumeration(source.getNumeration());
		target.setText(source.getText());

		return target;
	}
}
