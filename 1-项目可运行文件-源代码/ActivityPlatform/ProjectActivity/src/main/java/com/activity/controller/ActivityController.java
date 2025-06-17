package com.activity.controller;
import com.activity.model.Activities;
import com.activity.model.Result;
import com.activity.model.ResultCode;
import com.activity.service.ActivityService;
import com.activity.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "活动列表")
@RequestMapping("/activities")
public class ActivityController {
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ActivityService activityService;
    @Operation(summary = "通过activityid获取活动")
    @GetMapping("/showactivity/{activityid}")
    public Result<Activities> getUserInfo(@PathVariable int activityid) {
        Activities activities = activityService.getActivityinfo(activityid);
        if (activities != null) {
            return Result.success(activities);
        } else {
            return Result.error(ResultCode.NOT_FOUND, "Activity not found");
        }
    }
    @Operation(summary = "返回评分前三的活动信息",description = "")
    @GetMapping("/top-three")               //返回评分前三的活动信息

    public List<Activities> getTopThreeActivities() {
        return activityService.getTopThreeActivities();
    }
    @Operation(summary = "注册（发布）活动",description = "即活动申请，添加信息到数据库")
    @PostMapping("/request")  // 注册活动，即活动申请，添加信息到数据库
    public Result<String> registerActivity(@RequestBody Activities activities) {
        // 设置活动的提交时间为当前时间
        activities.setTimesubmit(LocalDateTime.now());

        activities.setStatus("审核中");

        // 将活动信息保存到数据库
        Activities requestedActivity = activityService.requestActivity(activities);
        if (requestedActivity != null) {
            return Result.success("Activity registered successfully");
        } else {
            return Result.error(ResultCode.FAIL, "Activity registration failed");
        }
    }

    @Operation(summary = "修改活动名称",description = "")
    @PutMapping("/{activityid}/activityname")       //
    public Result<String> updateActivityname(@PathVariable int activityid, @RequestParam String activityname) {
        boolean success = activityService.updateActivityFieldById(activityid, "activityname", activityname);
        return success ? Result.success("activityname updated successfully") : Result.error(ResultCode.FAIL, "Failed to update activityname");
    }
    @Operation(summary = "修改活动最大人数",description = "")
    @PutMapping("/{activityid}/maxceiling")         //
    public Result<String> updatemaxceiling(@PathVariable int activityid, @RequestParam int maxceiling) {
        boolean success = activityService.updateActivityFieldById(activityid, "maxceiling", maxceiling);
        return success ? Result.success("maxceiling updated successfully") : Result.error(ResultCode.FAIL, "Failed to update maxceiling");
    }
    @Operation(summary = "修改活动类型",description = "")
    @PutMapping("/{activityid}/type")               //
    public Result<String> updatetype(@PathVariable int activityid, @RequestParam String type) {
        boolean success = activityService.updateActivityFieldById(activityid, "type", type);
        return success ? Result.success("type updated successfully") : Result.error(ResultCode.FAIL, "Failed to update type");
    }
    @Operation(summary = "修改活动状态",description = "")
    @PutMapping("/{activityid}/status")             //
    public Result<String> updatestatus(@PathVariable int activityid, @RequestParam String status) {
        boolean success = activityService.updateActivityFieldById(activityid, "status", status);
        return success ? Result.success("status updated successfully") : Result.error(ResultCode.FAIL, "Failed to update status");
    }
    @Operation(summary = "修改活动报名时间起",description = "")
    @PutMapping("/{activityid}/timesignup1")        //
    public Result<String> updatetimesignup1(@PathVariable int activityid,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timesignup1) {
        boolean success = activityService.updateActivityFieldById(activityid, "timesignup1", timesignup1);
        return success ? Result.success("timesignup1 updated successfully") : Result.error(ResultCode.FAIL, "Failed to update timesignup1");
    }
    @Operation(summary = "修改活动报名时间止",description = "")
    @PutMapping("/{activityid}/timesignup2")        //
    public Result<String> updatetimesignup2(@PathVariable int activityid,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timesignup2) {
        boolean success = activityService.updateActivityFieldById(activityid, "timesignup2", timesignup2);
        return success ? Result.success("timesignup2 updated successfully") : Result.error(ResultCode.FAIL, "Failed to update timesignup2");
    }
    @Operation(summary = "修改活动进行时间起",description = "")
    @PutMapping("/{activityid}/timeproceed1")       //
    public Result<String> updatetimeproceed1(@PathVariable int activityid,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeproceed1) {
        boolean success = activityService.updateActivityFieldById(activityid, "timeproceed1", timeproceed1);
        return success ? Result.success("timeproceed1 updated successfully") : Result.error(ResultCode.FAIL, "Failed to update timeproceed1");
    }
    @Operation(summary = "修改活动进行时间止",description = "")
    @PutMapping("/{activityid}/timeproceed2")       //
    public Result<String> updatetimeproceed2(@PathVariable int activityid,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeproceed2) {
        boolean success = activityService.updateActivityFieldById(activityid, "timeproceed2", timeproceed2);
        return success ? Result.success("timeproceed2 updated successfully") : Result.error(ResultCode.FAIL, "Failed to update timeproceed2");
    }
    @Operation(summary = "修改活动地点")
    @PutMapping("/{activityid}/location")             //修改活动地点
    public Result<String> updatelocation(@PathVariable int activityid, @RequestParam String location) {
        boolean success = activityService.updateActivityFieldById(activityid, "location", location);
        return success ? Result.success("location updated successfully") : Result.error(ResultCode.FAIL, "Failed to update location");
    }
    @Operation(summary = "修改活动要求")
    @PostMapping("/update-request")
    public Result<String> updateActivityRequest(
            @RequestParam int activityId,
            @RequestParam String activityRequest) {

        boolean isUpdated = activityService.updateActivityRequest(activityId, activityRequest);
        if (isUpdated) {
            return Result.success("Success to update activity request");
        } else {
            return Result.error(ResultCode.FAIL, "Failed to update activity request");
        }
    }
    @Operation(summary = "修改活动其他说明")
    @PostMapping("/update-others")
    public Result<String> updateActivityOthers(
            @RequestParam int activityId,
            @RequestParam String others) {

        boolean isUpdated = activityService.updateActivityOthers(activityId, others);
        if (isUpdated) {
            return Result.success("Success to update activity others");
        } else {
            return Result.error(ResultCode.FAIL, "Failed to update activity others");
        }
    }

    @Operation(summary = "模糊搜索活动",description = "")
    @GetMapping("/search")                          //搜索活动
    public Result<List<Activities>> searchActivities(@RequestParam String keyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByName(keyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }
    @Operation(summary = "按照活动类型搜索")
    @GetMapping("/searchByType")                          //以类型来搜索活动
    public Result<List<Activities>> searchActivitiesByType(@RequestParam String keyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByType(keyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }

    @Operation(summary = "按照活动状态搜索")
    @GetMapping("archByStatus")                          //以活动状态来搜索活动
    public Result<List<Activities>> searchActivitiesByStatus(@RequestParam String keyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByStatus(keyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }

    @Operation(summary = "按照活动类型和状态搜索")
    @GetMapping("archByStatusAndType")  // 以活动状态和类型来搜索活动
    public Result<List<Activities>> searchActivitiesByStatusAndName(@RequestParam String statusKeyword, @RequestParam(required = false) String typeKeyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByStatusAndType(statusKeyword, typeKeyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }

    @Operation(summary = "两人参加的活动")
    @GetMapping("/searchByCeiling2")                          //以两人数搜索活动
    public Result<List<Activities>> searchActivitiesByCeiling2(@RequestParam String statusKeyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByCeiling2(statusKeyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }
    @Operation(summary = "3-5人参加的活动")
    @GetMapping("/searchByCeiling35")                          //以3到5人数搜索活动
    public Result<List<Activities>> searchActivitiesByCeiling35(@RequestParam String statusKeyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByCeiling35(statusKeyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }
    @Operation(summary = "6-10人参加的活动")
    @GetMapping("/searchByCeiling610")                          //以6到10人数搜索活动
    public Result<List<Activities>> searchActivitiesByCeiling610(@RequestParam String statusKeyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByCeiling610(statusKeyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }
    @Operation(summary = "大于10人的活动")
    @GetMapping("/searchByCeiling100")                          //以多于10人数搜索活动
    public Result<List<Activities>> searchActivitiesByCeiling100(@RequestParam String statusKeyword) {
        List<Activities> activitiesList = activityService.searchActivitiesByCeiling100(statusKeyword);
        return activitiesList.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found") : Result.success(activitiesList);
    }

    @Operation(summary = "返回随机三个报名中的活动信息",description = "")
    @GetMapping("/ongoingRegistrations")            //
    public Result<List<Activities>> getRandomOngoingRegistrationActivities() {
        List<Activities> activitiesList = activityService.getRandomOngoingRegistrationActivities();
        //activitiesList.forEach(System.out::println);  // 打印查询结果，便于调试
        return Result.success(activitiesList);
    }
    //
    @Operation(summary = "查看自己发布负责的活动",description = "")
    //查看自己参加的活动
    @GetMapping("/{userid}/activities")
    public Result<List<Activities>> getUserActivities(@PathVariable int userid) {
        List<Activities> activities = activityService.getActivitiesByHeadId(userid);
        return activities.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No activities found for this user.") : Result.success(activities);
    }
    //
    @Operation(summary = "删除自己发布的活动",description = "")
    @DeleteMapping("/{userid}/activities/{activityid}")
    public Result<String> deleteActivity(@PathVariable int userid, @PathVariable int activityid) {
        boolean success = activityService.deleteActivityById(activityid, userid);
        if (success) {
            return Result.success("Successfully deleted activity ID: " + activityid);
        } else {
            return Result.error(ResultCode.FAIL, "Failed to delete activity. Please check the activity ID and user permissions.");
        }
    }
    @Operation(summary = "修改活动图片路径",description = "")
    @PostMapping("/{activityId}/upload-activity-image")//，传输当前活动id和图片路径
    public Result<String> uploadActivityImage(@PathVariable int activityId, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = fileUploadService.uploadImage(file);
            boolean success = activityService.updateActivityImage(activityId, imageUrl);
            if (success) {
                return Result.success("Successfully updated activity image for activity ID: " + activityId);
            } else {
                return Result.error(ResultCode.FAIL, "Failed to update activity image. Please check the activity ID.");
            }
        } catch (IOException e) {
            return Result.error(ResultCode.FAIL, "Failed to upload image: " + e.getMessage());
        }
    }
    @Operation(summary = "今天正在进行的活动",description = "")
    @GetMapping("/today")
    public Result<List<Activities>> getTodayOngoingActivities() {
        List<Activities> activities = activityService.getTodayOngoingActivities();
        return activities.isEmpty() ? Result.error(ResultCode.NOT_FOUND, "No ongoing activities found for today.") : Result.success(activities);
    }
    @Operation(summary = "各类活动数量统计和比例以及总量",description = "里面有统计和比例")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getActivityTypeStatistics() {
        Map<String, Object> statistics = activityService.getActivityTypeStatistics();
        return Result.success(statistics);
    }
    @Operation(summary = "四个条件一起查询")
    @GetMapping("/searchbyfour")
    public Result<List<Activities>> searchActivities(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minCeiling,
            @RequestParam(required = false) Integer maxCeiling) {

        List<Activities> activities = activityService.searchActivitiesbyfour(keyword, type, status, minCeiling, maxCeiling);
        return Result.success(activities);
    }
    @Operation(summary = "获取活动最大参加人数")
    @GetMapping("/max-ceiling")
    public int getMaxCeiling(@RequestParam int activityId) {
        return activityService.getMaxCeilingByActivityId(activityId);
    }
    @Operation(summary = "该用户参与的活动（未结束的）的所有信息")
    @GetMapping("/user/activities")
    public List<Activities> getUserActivitiesNotend(@RequestParam int userId) {
        return activityService.getUserParticipatedActivities(userId);
    }
    @Operation(summary = "该用户参与的活动（已结束）的所有信息")
    @GetMapping("/user/finished-activities")
    public List<Activities> getUserFinishedActivities(@RequestParam int userId) {
        return activityService.getUserParticipatedFinishedActivities(userId);
    }

//    @GetMapping("/redisTest")
//    public void redisTest(){
//        activityService.redisTest();
//    }
    @Operation(summary = "删除redis中的所有活动数据")
    @DeleteMapping("/clear-cache")
        public Result<String> clearActivitiesCache() {
        activityService.deleteAllActivitiesFromRedis();
        return Result.success("All activities cache cleared from Redis.");
    }

    @Operation(summary = "近七天每天的活动数量",description = "")
    @GetMapping("/countlast7days")
    public Map<String, Integer> getActivitiesCountInLast7Days() {
        return activityService.getActivitiesCountInLast7Days();
    }

    @Operation(summary = "活动总数",description = "")
    @GetMapping("/countActivity")
    public Result<Integer> getActivityCount() {
        int Count = activityService.countActivity();
        return Result.success(Count);
    }

    @Operation(summary = "各活动状态统计",description = "")
    @GetMapping("/statusCounts")
    public Result<Map<String, Integer>> getStatusCounts() {
        try {
            Map<String, Integer> statusCounts = activityService.getStatusCounts();
            return Result.success(statusCounts);
        } catch (Exception e) {
            return Result.error(ResultCode.FAIL,"获取活动状态统计失败");
        }
    }

    @Operation(summary = "各活动类型统计",description = "")
    @GetMapping("/typeCounts")
    public Result<Map<String, Integer>> getTypeCounts() {
        try {
            Map<String, Integer> typeCounts = activityService.getTypeCounts();
            return Result.success(typeCounts);
        } catch (Exception e) {
            return Result.error(ResultCode.FAIL,"获取活动类型统计失败");
        }
    }

    @Operation(summary = "人数分别统计",description = "")
    @GetMapping("/maxceilingcounts")
    public Result<Map<String, Integer>> getMaxCeilingCounts() {
        Map<String, Integer> counts = activityService.getMaxCeilingCounts();
        return Result.success(counts);
    }
    @Operation(summary = "后台获取所有活动的数据")
    @GetMapping("/all")
    public Result<List<Activities>> getAllActivities() {
        try {
            List<Activities> activitiesList = activityService.getAllActivities();
            return Result.success(activitiesList);
        } catch (Exception e) {
            return Result.error(ResultCode.FAIL,"获取活动列表失败");
        }
    }
}
