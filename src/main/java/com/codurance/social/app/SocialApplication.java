package com.codurance.social.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author nickk
 * 
 * This class launches the environment and sets up the input listener
 * Input is done via the console
 */
public class SocialApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/application-context.xml");
        CommandProcessor commandListener = (CommandProcessor)context.getBean("commandProcessor");
        commandListener.scanForInput();
    }
}

