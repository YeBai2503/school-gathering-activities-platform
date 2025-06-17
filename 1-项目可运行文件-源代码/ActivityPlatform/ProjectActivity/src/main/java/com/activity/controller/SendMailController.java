package com.activity.controller;

import com.activity.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试邮件发送
 * @author qzz
 */
@RestController
@Tag(name = "官方邮件发送")
@RequestMapping("/mail")
public class SendMailController {

    @Autowired
    private MailService mailService;


    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param text
     */
    @Operation(summary = "发送简单文件")
    @RequestMapping("/sendTextMail")
    public void sendTextMail(String to,String subject,String text){
        mailService.sendTextMailMessage(to,subject,text);
    }

    /**
     * 发送HTML邮件
     * @param to
     * @param subject
     * @param content
     */
    @RequestMapping("/sendHtmlMailMessage")
    public void sendHtmlMailMessage(String to,String subject,String content){
        mailService.sendHtmlMailMessage(to,subject,content);
    }

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     */
    @RequestMapping("/sendAttachmentMailMessage")
    public void sendAttachmentMailMessage(String to,String subject,String content,String filePath){
        filePath="D:\\1.png";
        mailService.sendAttachmentMailMessage(to,subject,content,filePath);
    }
}
