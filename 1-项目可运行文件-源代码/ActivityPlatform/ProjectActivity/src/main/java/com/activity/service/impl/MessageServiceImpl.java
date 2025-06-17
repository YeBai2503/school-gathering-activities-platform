package com.activity.service.impl;

import com.activity.model.Message;
import com.activity.model.User;
import com.activity.service.MessageService;
import com.activity.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.activity.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private UserService userService;

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publisherid", userId);
        return this.list(queryWrapper);
    }


    @Override
    public boolean addMessage(Message message) {
        // 如果是新帖子（requestid == 0），先检查该用户是否已经发布过帖子
        if (message.getRequestid() == 0) {
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("publisherid", message.getPublisherid())
                    .eq("requestid", 0); // 0 表示帖子，而不是回复

            Message existingMessage = this.getOne(queryWrapper);

            // 如果已经发布过帖子，返回 false，表示不允许再次发布
            if (existingMessage != null) {
                return false;
            }
        }
        // 如果是回复，设置 requestnickname
        if (message.getRequestid() != 0) {
            Message requestMessage = getById(message.getRequestid());
            if (requestMessage != null) {
                User requestUser = userService.getById(requestMessage.getRequestid());
                if (requestUser != null) {
                    message.setRequestnickname(requestUser.getNickname());
                }
            }
        }
        // 设置发布者的昵称
        User publisherUser = userService.getById(message.getPublisherid());
        if (publisherUser != null) {
            message.setPublishnickname(publisherUser.getNickname());
        }

        // 保存消息（帖子或回复）
        return this.save(message);
    }



    @Override
    public boolean deleteMessageById(int messageId, int userId, int userPermission) {
        // 获取消息
        Message message = this.getById(messageId);
        if (message == null) {
            return false; // 如果留言不存在，返回 false
        }

        // 判断用户权限：普通用户只能删除自己的留言，管理员可以删除所有留言
        if (userPermission == 1 || (userPermission == 0 && message.getPublisherid() == userId)) {
            // 如果这是一个帖子（requestid 为 0），删除所有与该帖子关联的回复
            if (message.getRequestid() == 0) {
                // 删除与该帖子关联的所有回复
                QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("publisherid",message.getPublisherid()).ne("requestid", 0);
                this.remove(queryWrapper);
            }
            // 删除帖子或回复
            return this.removeById(messageId);
        } else {
            return false; // 权限不足，删除失败
        }
    }

    @Override
    public List<Message> getAllPosts() {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("requestid", 0); // 只查询requestid为0的记录，即帖子
        return this.list(queryWrapper);
    }

    @Override
    public List<Message> getRepliesByPostId(int postId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publisherid", postId).ne("requestid", 0);       // 且requestid不为0的记录
        return this.list(queryWrapper);
    }
    @Override
    public List<Message> getPostsByPublisherId(int publisherId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publisherid", publisherId).eq("requestid", 0); // 只查询帖子，即requestid为0
        return this.list(queryWrapper);
    }
    @Override
    public List<Message> getAllMessages() {
        return this.list(); // 返回所有消息
    }
}