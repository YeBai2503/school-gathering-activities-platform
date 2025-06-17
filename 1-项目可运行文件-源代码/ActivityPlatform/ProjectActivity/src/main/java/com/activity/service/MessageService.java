package com.activity.service;

import com.activity.model.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<Message> getMessagesByUserId(int userId);

    boolean addMessage(Message message);

    boolean deleteMessageById(int messageId, int userId, int userPermission);

    List<Message> getAllPosts(); // 查询所有帖子

    List<Message> getRepliesByPostId(int postId); // 查询某个帖子下的所有回复

    List<Message> getAllMessages(); // 获取所有帖子和回复
    List<Message> getPostsByPublisherId(int publisherId); // 获取指定发布者的所有帖子
}