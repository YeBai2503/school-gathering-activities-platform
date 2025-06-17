package com.activity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@TableName("activities")
public class Activities {
    @TableId(value = "activityid",type = IdType.AUTO)
    @Schema(description = "活动id")
    private int activityid;//活动id（主键）
    @TableField("activityname")
    @Schema(description = "活动名称")
    private String activityname;              //活动名
    @Schema(description = "最大参加人数")
    private int maxceiling;                //最大参会人数
    @Schema(description = "活动类型")
    private String type;                    //活动类型
    @Schema(description = "活动当前状态")
    private String status;                  //活动当前状态
    @Schema(description = "活动提交时间")
    private LocalDateTime timesubmit;      //活动提交时间
    @Schema(description = "活动审核通过时间")
    private LocalDateTime timepass;        //活动审核通过时间
    @Schema(description = "活动报名开始时间")
    private LocalDateTime timesignup1;     //活动报名开始时间
    @Schema(description = "报名截止时间")
    private LocalDateTime timesignup2;     //活动报名截至时间
    @Schema(description = "进行开始时间")
    private LocalDateTime timeproceed1;    //活动进行开始时间
    @Schema(description = "进行截止时间")
    private LocalDateTime timeproceed2;    //活动进行截至时间
    @Schema(description = "活动总体评分")
    private int score;                      //活动总体评分
    @Schema(description = "负责人id")
    private int headid;                     //负责人id
    @Schema(description = "用户头像路径")
    private String profileimageurl;         //用户头像的URL或路径
    @Schema(description = "活动地址")
    private String location;                //活动地址
    @Schema(description = "负责人用户名")
    private String headusername;           //负责人的用户名
    @Schema(description = "活动要求说明")
    private String activityrequest;        //活动要求说明
    @Schema(description = "活动说明")
    private String others;                 //活动说明
}
