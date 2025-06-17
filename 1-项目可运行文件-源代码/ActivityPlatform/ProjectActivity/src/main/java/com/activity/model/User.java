package com.activity.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户id")
    private int id;
    @Schema(description = "用户名")
    private String username;//用户名
    @Schema(description = "密码")
    private String password;//密码
    @Schema(description = "昵称")
    private String nickname;//昵称
    @Schema(description = "社团")
    private String community;//社团
    @Schema(description = "性别")
    private String sex;//性别
    @Schema(description = "电话号码")
    private String pnumber;//电话号码
    @Schema(description = "大学")
    private String university;//大学
    @Schema(description = "电子邮件")
    private String email;//电子邮件
    @Schema(description = "下去啊哈")
    private String hobby;//兴趣爱好
    @Schema(description = "权限")
    private int permission;//权限，0为普通，1为管理员
    @Schema(description = "个人信誉分")
    private int reputation;//个人信誉分
    @Schema(description = "用户头像路径")
    private String profileimageurl;//用户头像的URL或路径

}
