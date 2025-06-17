package com.activity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "留言序列号")
    private int id; // 留言的主键之一
    @TableField("publisherid")
    @Schema(description = "发布者id")
    private int publisherid; // 发布者的用户ID，主键之一
    @TableField("requestid")
    @Schema(description = "回复者id")
    private int requestid; // 回复的消息ID，如果是帖子，则为0
    @Schema(description = "留言内容")
    @TableField("content")
    private String content; // 留言内容
    @Schema(description = "留言时间戳，默认当前时间")
    @TableField("timestamp")
    private LocalDateTime timestamp = LocalDateTime.now(); // 留言时间戳，默认当前时间
    @Schema(description = "留言状态")
    @TableField("status")
    private String status = "pending"; // 留言状态，默认 "pending"
    @TableField("publishnickname")
    @Schema(description = "发布者昵称")
    private String publishnickname; // 发布者的昵称
    @TableField("requestnickname")
    @Schema(description = "回复对象的昵称")
    private String requestnickname; // 回复的对象的昵称（如果是回复）
}
