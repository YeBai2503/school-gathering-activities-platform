package com.activity.service;

import com.activity.model.EventRegist;
import com.activity.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EventRegistService extends IService<EventRegist> {
    boolean registerForEvent(int userid, int activityid,String reason);
    List<EventRegist> getEventsByUserId(int userid);
    boolean cancelRegistration(int userid, int activityid);
    boolean rateActivity(int userid, int activityid, int score);

    int countRegistrationsByActivityId(int activityId);       //获取参与当前活动的人数
    List<EventRegist> getUsersByActivityId(int activityId);
    EventRegist getEventRegistByUserIdAndActivityId(int userId, int activityId);
    boolean updateScore(int userid, int activityid, int score);

    List<Map<String, Object>> getTop5Usernames();
    Integer getScoreByUserAndActivity(int userid, int activityid);
    List<EventRegist> getAllEventRegists();
}
