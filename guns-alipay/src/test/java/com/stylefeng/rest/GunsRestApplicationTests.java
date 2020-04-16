package com.stylefeng.rest;

import com.stylefeng.guns.rest.AlipayApplication;
import com.stylefeng.guns.rest.common.util.FtpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AlipayApplication.class)
public class GunsRestApplicationTests {

	@Autowired
	private FtpUtil ftpUtil;

	@Test
	public void contextLoads() {
		/*String a = ftpUtil.getFileStrByAddress("cgs.json");
		System.out.println(a);*/

		File file = new File("/Users/huangyaning/test.txt");
		boolean b = ftpUtil.uploadFile("test.txt", file);
		System.out.println("是否上传成功:" + b);
	}

}
