package com.activity.controller;

import com.activity.model.EventRegist;
import com.activity.model.Result;
import com.activity.model.ResultCode;
import com.activity.model.User;
import com.activity.service.EventRegistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name="活动参加表")
@RequestMapping("/event")
public class EventRegistController {
    @Autowired
    private EventRegistService eventRegistService;
    //；
    @Operation(summary = "活动报名功能",description = "传输当前登录用户的id和要报名参加的用户id")
    @PostMapping("/register")
    public Result<String> registerForEvent(@RequestParam int userid, @RequestParam int activityid,@RequestParam String reason) {
        boolean success = eventRegistService.registerForEvent(userid, activityid,reason);
        if (success) {
            return Result.success("Registration successful for activity ID: " + activityid);
        } else {
            return Result.error(ResultCode.FAIL, "当前活动已满人或现在无法报名");
        }
    }
    //的功能，
    @Operation(summary = "获取当前用户所有参加的活动",description = "传输当前登录用户的id")
    @GetMapping("/{userid}/events")
    public Result<List<EventRegist>> getUserEvents(@PathVariable int userid) {
        List<EventRegist> events = eventRegistService.getEventsByUserId(userid);
        return events.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No events found for this user.") : Result.success(events);
    }
    //，
    @Operation(summary = "退出当前活动",description = "传输当前登录用户的id和当前活动的id")
    @DeleteMapping("/{userid}/events/{activityid}")
    public Result<String> cancelRegistration(@PathVariable int userid, @PathVariable int activityid) {
        boolean success = eventRegistService.cancelRegistration(userid, activityid);
        if (success) {
            return Result.success("Successfully canceled registration for activity ID: " + activityid);
        } else {
            return Result.error(ResultCode.FAIL, "Failed to cancel registration. Please check the user ID and activity ID.");
        }
    }
    //
    @Operation(summary = "为活动打分",description = "")
    @PostMapping("/{userid}/activities/{activityid}/rate")
    public Result<String> rateActivity(@PathVariable int userid, @PathVariable int activityid, @RequestParam int score) {
        boolean success = eventRegistService.rateActivity(userid, activityid, score);
        if (success) {
            return Result.success("Successfully rated activity ID: " + activityid + " with score: " + score);
        } else {
            return Result.error(ResultCode.FAIL, "Failed to rate activity. Please check the user ID, activity ID, and ensure the user has registered for the activity.");
        }
    }

    @Operation(summary = "获取当前活动的参加人数",description = "传输当前登录活动的id")
    @GetMapping("/{activityid}/count")
    public Result<Integer> getRegistrationCount(@PathVariable int activityid) {
        int count = eventRegistService.countRegistrationsByActivityId(activityid);
        return Result.success(count);
    }
    @Operation(summary = "通过activityid查询所有参加的报名信息")
    @GetMapping("/users")
    public Result<List<EventRegist>> getUsersByActivityId(@RequestParam int activityId) {
        List<EventRegist> eventRegists = eventRegistService.getUsersByActivityId(activityId);
        return Result.success(eventRegists);
    }
    @Operation(summary = "通过activityid和userid查询备注")
    @GetMapping("/regist")
    public Result<EventRegist> getEventRegist(
            @RequestParam int userId,
            @RequestParam int activityId) {

        EventRegist eventRegist = eventRegistService.getEventRegistByUserIdAndActivityId(userId, activityId);
        return eventRegist != null ? Result.success(eventRegist) : Result.error(ResultCode.NOT_FOUND, "EventRegist not found");
    }
    @Operation(summary = "更新评分",description = "")
    @PutMapping("/update-score")
    public Result<String> updateScore(@RequestParam int userid, @RequestParam int activityid, @RequestParam int score) {
        boolean success = eventRegistService.updateScore(userid, activityid, score);
        if (success) {
            return Result.success("Score updated successfully.");
        } else {
            return Result.error(ResultCode.FAIL, "Failed to update score. Record not found.");
        }
    }
    @Operation(summary = "参加h'd活动前五的用户",description = "")
    @GetMapping("/top5usernames")
    public List<Map<String, Object>> getTop5Usernames() {
        return eventRegistService.getTop5Usernames();
    }
    @Operation(summary = "获取某个用户给某个活动的评分")
    @GetMapping("/getScore")
    public Result<Integer> getScore(@RequestParam int userid, @RequestParam int activityid) {
        // 调用 service 层方法获取评分
        Integer score = eventRegistService.getScoreByUserAndActivity(userid, activityid);

        // 判断是否找到评分记录
        if (score != null) {
            return Result.success(score);
        } else {
            return Result.error(ResultCode.FAIL);
        }
    }
    @Operation(summary = "获取所有活动申请表信息")
    @GetMapping("/all")
    public Result<List<EventRegist>> getAllEventRegists() {
        try {
            List<EventRegist> eventRegistList = eventRegistService.getAllEventRegists();
            return Result.success(eventRegistList);
        } catch (Exception e) {
            return Result.error(ResultCode.FAIL,"获取活动注册信息失败");
        }
    }
}
