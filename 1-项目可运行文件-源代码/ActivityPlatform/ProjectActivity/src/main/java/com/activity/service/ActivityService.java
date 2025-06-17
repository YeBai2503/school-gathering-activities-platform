package com.activity.service;

import com.activity.model.Activities;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ActivityService extends IService<Activities>{

    Activities getActivityinfo(int activityid);

    Activities requestActivity(Activities activities);

    List<Activities> getTopThreeActivities();

    boolean updateActivityFieldById(int activityid, String fieldName, Object fieldValue);

    List<Activities> searchActivitiesByName(String keyword);

    List<Activities> searchActivitiesByType(String keyword);

    List<Activities> searchActivitiesByStatus(String keyword);


    List<Activities> searchActivitiesByStatusAndType(String statusKeyword, String nameKeyword);


    List<Activities> searchActivitiesByCeiling2(String statusKeyword);

    List<Activities> searchActivitiesByCeiling35(String statusKeyword);

    List<Activities> searchActivitiesByCeiling610(String statusKeyword);

    List<Activities> searchActivitiesByCeiling100(String statusKeyword);



    List<Activities> getRandomOngoingRegistrationActivities(); // 随机返回三个 type 为“报名中”的活动

    List<Activities> getActivitiesByHeadId(int headid);
    boolean deleteActivityById(int activityid, int headid);
    boolean updateActivityImage(int activityId, String imageUrl);
    List<Activities> getTodayOngoingActivities();
    Map<String, Object> getActivityTypeStatistics();
    List<Activities> searchActivitiesbyfour(String keyword, String type, String status, Integer minCeiling, Integer maxCeiling);
    int getMaxCeilingByActivityId(int activityId);
    List<Activities> getUserParticipatedActivities(int userId);
    List<Activities> getUserParticipatedFinishedActivities(int userId);
    boolean updateActivityRequest(int activityId, String activityRequest);
    boolean updateActivityOthers(int activityId, String others);

    //public void redisTest ();
    void deleteAllActivitiesFromRedis();

    Map<String, Integer> getActivitiesCountInLast7Days();

    int countActivity();

    Map<String, Integer> getStatusCounts();

    Map<String, Integer> getTypeCounts();

    Map<String, Integer> getMaxCeilingCounts();

    List<Activities> getAllActivities();
}
