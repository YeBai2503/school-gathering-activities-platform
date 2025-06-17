package com.activity.controller;

import com.activity.model.Message;
import com.activity.model.Result;
import com.activity.model.ResultCode;
import com.activity.model.User;
import com.activity.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "留言版")
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Operation(summary = "发布者发布帖子或回复者回复帖子(同一用户只能发一个帖子)",description = "requestid=0时是帖子")
    @PostMapping("/add")
    public Result<String> addMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        boolean success = messageService.addMessage(message);
        if (success) {
            return Result.success("Message added successfully");
        } else {
            return Result.error(ResultCode.FAIL, "User has already posted a message and cannot post another one.");
        }
    }
    @Operation(summary = "列出所有帖子和回复")
    @GetMapping("/list")
    public Result<List<Message>> listMessages() {
        List<Message> messages = messageService.getAllMessages();
        return Result.success(messages);
    }
    @Operation(summary = "列出所有帖子")
    @GetMapping("/posts")
    public Result<List<Message>> getAllPosts() {
        List<Message> posts = messageService.getAllPosts();
        return Result.success(posts);
    }
    @Operation(summary = "获取某个帖子下的所有回复")
    @GetMapping("/replies/{publisherId}")
    public Result<List<Message>> getRepliesByPostId(@PathVariable int publisherId) {
        List<Message> replies = messageService.getRepliesByPostId(publisherId);
        return Result.success(replies);
    }
    @Operation(summary = "管理员或贴主人删除")
    @DeleteMapping("/delete/{messageId}")
    public Result<String> deleteMessage(@PathVariable int messageId, @RequestParam int userId, @RequestParam int userPermission) {
        boolean success = messageService.deleteMessageById(messageId, userId, userPermission);
        if (success) {
            return Result.success("Message deleted successfully");
        } else {
            return Result.error(ResultCode.FORBIDDEN, "Failed to delete message or insufficient permissions");
        }
    }
    @Operation(summary = "获取每个用户发布的每个帖子")
    @GetMapping("/posts/publisher/{publisherId}")
    public Result<List<Message>> getPostsByPublisherId(@PathVariable int publisherId) {
        List<Message> posts = messageService.getPostsByPublisherId(publisherId);
        return Result.success(posts);
    }
}