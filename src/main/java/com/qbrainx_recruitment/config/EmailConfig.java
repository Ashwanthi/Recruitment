package com.qbrainx_recruitment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
@EnableAsync
public class EmailConfig {

    @Value("${spring.mail.default-encoding}")
    private String mailDefaultEncoding;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.port}")
    private Integer mailPort;

    @Value("${spring.mail.protocol}")
    private String mailProtocol;

    @Value("${spring.mail.debug}")
    private String mailDebug;

    @Value("${spring.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${spring.mail.smtp.starttls.enable}")
    private String mailSmtpStartTls;

    @Value("${spring.mail.smtp.ssl.trust}")
    private String mailSslTrust;

    @Bean
    @Primary
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        final FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("/templates/");
        return bean;
    }

    @Bean
    public JavaMailSender getMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setDefaultEncoding(mailDefaultEncoding);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        final Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.starttls.enable", mailSmtpStartTls);
        javaMailProperties.setProperty("mail.smtp.auth", mailSmtpAuth);
        javaMailProperties.setProperty("mail.transport.protocol", mailProtocol);
        javaMailProperties.setProperty("mail.smtp.ssl.trust", mailSslTrust);
        javaMailProperties.setProperty("mail.debug", mailDebug);

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
}
