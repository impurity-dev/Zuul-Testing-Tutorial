package com.tmk2003.zuultesting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZuulTestingApplicationTests {

	@Autowired
	private Environment env;

	/**
	 * Ensure the Zuul application can actually load its context
	 */
	@Test
	public void contextLoads() {
		assertTrue("Application loads context properly", true);
		assertNotNull(env);
	}

}
