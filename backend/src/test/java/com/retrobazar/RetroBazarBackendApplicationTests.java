package com.retrobazar;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties =
		"app.security.jwt.secret=a-secure-test-secret-with-at-least-32-characters")
class RetroBazarBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
