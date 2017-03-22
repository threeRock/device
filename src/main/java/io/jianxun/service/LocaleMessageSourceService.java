package io.jianxun.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LocaleMessageSourceService {

	@Autowired
	private MessageSource messageSource;

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param code
	 *            ：对应messages配置的key.
	 * @return
	 */
	public String getMessage(String code) {
		return getMessage(code, null);
	}

	/**
	 *
	 * @param code
	 *            ：对应messages配置的key.
	 * @param args
	 *            : 数组参数.
	 * @return
	 */
	public String getMessage(String code, Object[] args) {
		return getMessage(code, args, "");
	}

	public String getMessage(String code, Object[] args, String defaultMessage) {
		// 这里使用比较方便的方法，不依赖request.
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}

}
