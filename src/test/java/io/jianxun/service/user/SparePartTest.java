package io.jianxun.service.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import io.jianxun.service.business.SparePartService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SparePartTest {

	@Autowired
	private SparePartService sparePartService;

	@Test
	public void testImport() {
		Resource resource = new FileSystemResource("D:\\test.txt");
	}

}
