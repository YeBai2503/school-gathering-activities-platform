package com.activity.service.impl;

import com.activity.mapper.EventRegistMapper;
import com.activity.model.Activities;
import com.activity.model.EventRegist;
import com.activity.model.User;
import com.activity.service.ActivityService;
import com.activity.service.EventRegistService;
import com.activity.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EventRegistServiceImpl extends ServiceImpl<EventRegistMapper,EventRegist> implements EventRegistService {
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private ActivityService activityService;

    @Override
    public boolean registerForEvent(int userid, int activityid, String reason) {
        // 检查用户和活动是否存在
        User user = userService.getById(userid);
        Activities activity = activityService.getById(activityid);

        // 检查用户是否已经注册了该活动
        QueryWrapper<EventRegist> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("userid", userid).eq("activityid", activityid);
        EventRegist existingRegistration = this.getOne(checkWrapper);

        // 检查活动状态是否为 "审核中" 或 "已结束"
        if (activity != null && ("审核中".equals(activity.getStatus()) || "已结束".equals(activity.getStatus()))) {
            return false; // 如果活动状态为 "审核中" 或 "已结束"，则不允许报名
        }
        // 如果已经注册了该活动，返回 false
        if (existingRegistration != null) {
            return false;
        }
        // 检查活动参与人数是否已满
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityid", activityid);
        int num = (int) this.count(queryWrapper);

        // 如果用户和活动都存在，且人数未超限，则允许注册
        if (user != null && activity != null && num < activity.getMaxceiling()) {
            EventRegist eventRegist = new EventRegist();
            eventRegist.setReason(reason);
            eventRegist.setUserid(userid);
            eventRegist.setActivityid(activityid);
            eventRegist.setActivityname(activity.getActivityname());
            eventRegist.setUsername(user.getUsername());
            eventRegist.setScore(80);  // 默认评分
            return this.save(eventRegist);
        }

        // 如果用户不存在、活动不存在或人数已满，则返回 false
        return false;
    }
    @Override
    public boolean updateScore(int userid, int activityid, int score) {
        // 1. 构建查询条件，找到指定的用户和活动的注册记录
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).eq("activityid", activityid);

        // 2. 查询数据库中的记录
        EventRegist registration = this.getOne(queryWrapper);

        if (registration != null) {
            // 3. 更新评分
            registration.setScore(score);

            // 4. 将更新后的记录保存回数据库
            return this.updateById(registration);
        }

        // 如果未找到记录，则返回 false
        return false;
    }
    @Override
    public List<EventRegist> getEventsByUserId(int userid) {
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid);
        return this.list(queryWrapper);
    }

    @Override
    public boolean cancelRegistration(int userid, int activityid) {
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).eq("activityid", activityid);
        return this.remove(queryWrapper);
    }

    @Override
    public boolean rateActivity(int userid, int activityid,int score) {
        // 1. 更新用户的评分
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).eq("activityid", activityid);
        EventRegist registration = this.getOne(queryWrapper);

        if (registration != null) {
            updateScore(registration.getUserid(),registration.getActivityid(),score);

            // 2. 获取活动的所有评分记录
            QueryWrapper<EventRegist> activityQueryWrapper = new QueryWrapper<>();
            activityQueryWrapper.eq("activityid", activityid);

            List<EventRegist> registrations = this.list(activityQueryWrapper);

            if (registrations != null && !registrations.isEmpty()) {
                // 3. 计算所有用户评分的平均值
                int totalScore = registrations.stream().mapToInt(EventRegist::getScore).sum();
                int averageScore = totalScore / registrations.size();

                // 4. 更新活动的评分为平均值
                Activities activity = activityService.getById(activityid);
                if (activity != null) {
                    activity.setScore(averageScore);
                    return activityService.updateById(activity);
                }
            }
        }
        return false;
    }


    @Override
    public int countRegistrationsByActivityId(int activityId) {
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityid", activityId);
        return (int)this.count(queryWrapper);
    }
    @Override
    public List<EventRegist> getUsersByActivityId(int activityId) {
        // 1. 通过 EventRegist 查询活动的所有参与用户ID
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityid", activityId);
        return this.list(queryWrapper);
    }

    @Override
    public EventRegist getEventRegistByUserIdAndActivityId(int userId, int activityId) {
        // 创建查询条件
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userId)
                .eq("activityid", activityId);

        // 返回查询到的 EventRegist 对象
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getTop5Usernames() {
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username", "COUNT(username) as count")
                .groupBy("username")
                .orderByDesc("count")
                .last("LIMIT 5");

        return baseMapper.selectMaps(queryWrapper);
    }
    @Override
    public Integer getScoreByUserAndActivity(int userid, int activityid) {
        // 使用 QueryWrapper 来构建查询条件
        QueryWrapper<EventRegist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).eq("activityid", activityid);

        // 查询指定用户和活动的记录
        EventRegist registration = this.getOne(queryWrapper);

        // 如果记录存在，返回评分；否则返回null表示没有找到记录
        if (registration != null) {
            return registration.getScore();
        }

        return null;
    }

    @Override
    public List<EventRegist> getAllEventRegists() {
        return list();  // 使用 MyBatis-Plus 提供的 list() 方法获取所有记录
    }
}
