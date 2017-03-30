package io.jianxun.web.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;

@Component
public class Utils {

	private final static String PAGE = "page";
	private final static String SIZE = "size";
	private final static String TOTAL = "total";
	private final static String ORDER_FIELD = " orderField";
	private final static String ORDER_DIRECTION = " orderDirection";
	private final static String CONTENT = "content";
	
	public final static String PAGE_TEMPLATE_SUFFIX = "page";
	public final static String SAVE_TEMPLATE_SUFFIX = "form";
	

	public Map<String, Object> getSearchMap(MultiValueMap<String, String> parameters) {
		Map<String, Object> map = Maps.newHashMap();
		Set<String> keySet = parameters.keySet();
		for (String property : keySet) {
			if (!Arrays.asList(PAGE, SIZE, TOTAL, ORDER_FIELD, ORDER_DIRECTION).contains(property)) {
				List<String> values = parameters.get(property);
				if (values != null && values.size() != 0) {
					if (values.size() == 1)
						map.put(property, parameters.getFirst(property));
					else
						map.put(property, StringUtils.collectionToDelimitedString(values, ","));
				}
			}
		}
		return map;
	}

	// 分页相关参数设定
	public Map<String, Object> getPageMap(MultiValueMap<String, String> parameters, Page<?> page) {
		Map<String, Object> map = Maps.newHashMap();
		// 分页参数设定
		map.put(PAGE, Integer.valueOf(page.getNumber()));
		map.put(SIZE, Integer.valueOf(page.getSize()));
		map.put(TOTAL, Long.valueOf(page.getTotalElements()));
		map.put(ORDER_FIELD, page.getSort().iterator().next().getProperty());
		map.put(ORDER_DIRECTION, page.getSort().iterator().next().getDirection());
		// 列表数据设定
		map.put(CONTENT, page.getContent());
		return map;
	}

}
