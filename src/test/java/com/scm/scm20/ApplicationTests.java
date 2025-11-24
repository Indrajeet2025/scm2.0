package com.scm.scm20;

import com.scm.scm20.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private EmailService emailService;

	@Test
	void contextLoads() {
	}

    @Test
    void sendEmailTest()
    {
        emailService.sendEmail("itechinnovations017@gmail.com","Testing email service","this is scm project email service");
    }

}
