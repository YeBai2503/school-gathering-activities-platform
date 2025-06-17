package com.activity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("eventregist")
public class EventRegist {
    @Schema(description = "活动参加表id")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @Schema(description = "参加活动人的id")
    @TableField("userid")
    private int userid;
    @Schema(description = "活动id")
    @TableField("activityid")
    private int activityid;
    @Schema(description = "活动名称")
    @TableField("activityname")
    private String activityname;
    @Schema(description = "参加活动人用户名")
    @TableField("username")
    private String username;
    @Schema(description = "参加理由")
    @TableField("reason")
    private String reason;
    @TableField("score")
    @Schema(description = "评分")
    private int score;
}
