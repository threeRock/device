package io.jianxun.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class LocalMessageSourceServiceTest {

	private final String idNotNull = "id.notnull";

	LocaleMessageSourceService localMessageSource;

	@Before
	public void setUp() {
		localMessageSource = new LocaleMessageSourceService();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		localMessageSource.setMessageSource(messageSource);
	}

	@Test
	public void smockTest() {
		assertThat(this.localMessageSource).isNotNull();
	}

	@Test
	public void getMessageSuccess() {
		String message = localMessageSource.getMessage(idNotNull);
		assertThat(message).isNotNull().isEqualTo("ID 不能为空");
	}
	
	@Test
	public void getMessageFalse() {
		String message = localMessageSource.getMessage("");
		assertThat(message).isEmpty();
	}

}
