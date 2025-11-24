package com.scm.scm20.config;

import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler
{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
       //System.out.println("AuthFailureHandler running.....");
        System.out.println("AuthFailureHandler running.....");
        System.out.println("Exception class: " + exception.getClass().getName());
        System.out.println("Exception message: " + exception.getMessage());

        HttpSession session = request.getSession();

        if (exception instanceof DisabledException) {
            System.out.println("yes it is (DisabledException)");
            session.setAttribute("message",
                    Message.builder()
                            .content("User is disabled. Please verfify email with your verfication link sent at your emailId .")
                            .type(MessageType.red)
                            .build());
        } else {
            session.setAttribute("message",
                    Message.builder()
                            .content("Invalid email or password.")
                            .type(MessageType.red)
                            .build());
        }

        response.sendRedirect("/login?error=true");
    }
}
