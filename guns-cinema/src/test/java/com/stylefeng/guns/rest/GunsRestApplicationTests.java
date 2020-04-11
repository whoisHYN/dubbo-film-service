package com.stylefeng.guns.rest;

import com.stylefeng.guns.rest.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GunsRestApplicationTests {

	@Autowired
	private RedisUtil redisUtil;

	@Test
	public void contextLoads() {
		Jedis jedis = redisUtil.getJedis();
		Set<String> keys = jedis.keys("*");

		System.out.println(keys);
	}
}
