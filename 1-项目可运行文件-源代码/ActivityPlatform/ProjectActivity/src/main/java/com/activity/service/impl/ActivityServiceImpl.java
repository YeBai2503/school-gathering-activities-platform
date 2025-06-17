package com.activity.service.impl;

import com.activity.mapper.ActivityMapper;
import com.activity.model.Activities;
import com.activity.model.EventRegist;
import com.activity.model.User;
import com.activity.service.ActivityService;
import com.activity.service.EventRegistService;
import com.activity.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper,Activities> implements ActivityService {

    @Autowired
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "activities:";

    @PostConstruct
    public void preloadActivitiesToRedis() {
        // 从数据库中获取所有活动数据
        List<Activities> activitiesList = this.list();

        // 将每个活动数据保存到Redis中，以活动ID作为键
        for (Activities activity : activitiesList) {
            redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + activity.getActivityid(), activity);
        }
    }

    @Autowired
    @Lazy
    private EventRegistService eventRegistService;
    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Activities> getTopThreeActivities() {
        String query = "SELECT * FROM activities ORDER BY score DESC LIMIT 3";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Activities activity = new Activities();
            activity.setActivityid( rs.getInt("activityid"));
            activity.setActivityname(rs.getString("activityname"));
            activity.setMaxceiling(rs.getInt("maxceiling"));
            activity.setType(rs.getString("type"));
            activity.setStatus(rs.getString("status"));
            activity.setTimesubmit(rs.getObject("timesubmit", LocalDateTime.class));
            activity.setTimepass(rs.getObject("timepass", LocalDateTime.class));
            activity.setTimesignup1(rs.getObject("timesignup1", LocalDateTime.class));
            activity.setTimesignup2(rs.getObject("timesignup2", LocalDateTime.class));
            activity.setTimeproceed1(rs.getObject("timeproceed1", LocalDateTime.class));
            activity.setTimeproceed2(rs.getObject("timeproceed2", LocalDateTime.class));
            activity.setScore(rs.getInt("score"));
            activity.setHeadid(rs.getInt("headid"));
            activity.setProfileimageurl(rs.getString("profileimageurl"));
            activity.setLocation(rs.getString("location"));
            activity.setHeadusername(rs.getString("headusername"));
            activity.setActivityrequest(rs.getString("activityrequest"));
            activity.setOthers(rs.getString("others"));
            return activity;
        });
    }


    @Override
    public Activities getActivityinfo(int activityid) {
        return getById(activityid);
    }


    @Override
    public Activities requestActivity(Activities activities) {
        // 获取负责人信息
        User headUser = userService.getById(activities.getHeadid());
        if (headUser != null) {
            // 设置headusername为负责人的用户名
            activities.setHeadusername(headUser.getUsername());
        }
        if(activities.getType()==null){
            return null;
        }
        // 保存活动信息
        boolean result = this.save(activities);

        // 如果活动保存成功，自动将发布者加入活动参与表
        if (result) {
            EventRegist eventRegist = new EventRegist();
            eventRegist.setUserid(activities.getHeadid()); // 设置发布者ID为用户ID
            eventRegist.setActivityid(activities.getActivityid()); // 设置活动ID
            eventRegist.setActivityname(activities.getActivityname()); // 设置活动名称
            eventRegist.setUsername(activities.getHeadusername()); // 设置发布者的用户名
            eventRegist.setReason("自动加入"); // 设置默认原因
            eventRegist.setScore(80); // 设置默认评分为0

            // 保存参与记录
            eventRegistService.save(eventRegist);

            return activities;
        } else {
            return null;
        }
    }
    //活动模糊搜索
    @Override
    public List<Activities> searchActivitiesByName(String keyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("activityname", keyword);
        return list(queryWrapper);
    }

    @Override                                       //以活动类型来进行搜索
    public List<Activities> searchActivitiesByType(String keyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("type", keyword);
        return list(queryWrapper);
    }

    @Override                                       //以活动状态来进行搜索
    public List<Activities> searchActivitiesByStatus(String keyword){
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("status", keyword);
        return list(queryWrapper);
    }


    @Override                                       //以状态和类型来搜索活动
    public List<Activities> searchActivitiesByStatusAndType(String statusKeyword, String typeKeyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        // 根据活动状态进行搜索
        queryWrapper.like("status", statusKeyword);
        // 根据活动类型进一步过滤
        if (typeKeyword != null && !typeKeyword.isEmpty()) {
            queryWrapper.like("type", typeKeyword);
        }
        return list(queryWrapper);
    }


    @Override                                       //以两人来进行搜索
    public List<Activities> searchActivitiesByCeiling2(String statusKeyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("status", statusKeyword);
        queryWrapper.eq("maxceiling", 2);
        return list(queryWrapper);
    }

    @Override                                       //以3到5人来进行搜索
    public List<Activities> searchActivitiesByCeiling35(String statusKeyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("status", statusKeyword);
        queryWrapper.gt("maxceiling", 2).lt("maxceiling",6);
        return list(queryWrapper);
    }

    @Override                                       //以6到10人来进行搜索
    public List<Activities> searchActivitiesByCeiling610(String statusKeyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("status", statusKeyword);
        queryWrapper.gt("maxceiling", 5).lt("maxceiling",11);
        return list(queryWrapper);
    }

    @Override                                       //以大于10人来进行搜索
    public List<Activities> searchActivitiesByCeiling100(String statusKeyword) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("status", statusKeyword);
        queryWrapper.gt("maxceiling", 10);
        return list(queryWrapper);
    }


    //更新活动数据
    @Override
    public boolean updateActivityFieldById(int activityid, String fieldName, Object fieldValue) {
        UpdateWrapper<Activities> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activityid", activityid).set(fieldName, fieldValue);
        return update(updateWrapper);
    }
    //返回报名中的活动
    @Override
    public List<Activities> getRandomOngoingRegistrationActivities() {
        // 创建查询条件，仅返回 status 为“报名中”的活动
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Activities::getStatus, "报名中");

        // 查询匹配的活动列表
        List<Activities> activitiesList = this.list(queryWrapper);

        // 随机打乱列表顺序
        Collections.shuffle(activitiesList);

        // 返回前三个活动，如果活动不足三个则返回全部
        return activitiesList.size() > 3 ? activitiesList.subList(0, 3) : activitiesList;
    }
    //查看自己发布的活动
    @Override
    public List<Activities> getActivitiesByHeadId(int headid) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("headid", headid)
                .ne("status", "已结束"); // 过滤掉 status 为 "已结束" 的活动
        return this.list(queryWrapper);
    }
    //删除自己发布的活动
    @Override
    public boolean deleteActivityById(int activityid, int headid) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityid", activityid).eq("headid", headid);
        return this.remove(queryWrapper);
    }
    //更新活动图片
    @Override
    public boolean updateActivityImage(int activityId, String imageUrl) {
        Activities activity = this.getById(activityId);
        if (activity != null) {
            activity.setProfileimageurl(imageUrl);
            return this.updateById(activity);
        }
        return false;
    }

    //查看今天正在进行的活动
    @Override
    public List<Activities> getTodayOngoingActivities() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 今天的开始时间：00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 今天的结束时间：23:59:59.999999999

        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("status", "进行中") // 假设活动状态为 "进行中" 表示正在进行的活动
                .ge("timeproceed1", startOfDay); // 活动开始时间在今天之内
        return this.list(queryWrapper);
    }
    //统计各获得类型的比例
    @Override
    public Map<String, Object> getActivityTypeStatistics() {
        // 定义活动类型
        String[] activityTypes = {"体育", "学术", "艺术", "桌游", "出行", "手游", "端游", "其他"};

        // 创建一个Map用于存储统计结果
        Map<String, Integer> typeCounts = new HashMap<>();
        int totalActivities = 0;

        // 统计每种活动类型的数量
        for (String type : activityTypes) {
            QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type);
            int count = (int) this.count(queryWrapper);
            typeCounts.put(type, count);
            totalActivities += count;
        }

        // 创建另一个Map用于存储最终的统计和比例结果
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalActivities", totalActivities);

        // 计算比例并存储在统计结果中
        Map<String, Double> typeRatios = new HashMap<>();
        for (String type : activityTypes) {
            int count = typeCounts.get(type);
            double ratio = totalActivities > 0 ? (double) count / totalActivities : 0.0;
            typeRatios.put(type, ratio);
        }

        statistics.put("typeCounts", typeCounts);
        statistics.put("typeRatios", typeRatios);

        return statistics;
    }
    //搜索栏和标签搜索
    @Override
    public List<Activities> searchActivitiesbyfour(String keyword, String type, String status, Integer minCeiling,Integer maxCeiling) {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();

        // 1. 模糊搜索活动名称
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("activityname", keyword);
        }

        // 2. 按活动类型搜索
        if (type != null && !type.isEmpty()) {
            queryWrapper.like("type", type);
        }

        // 3. 按活动状态搜索
        if (status != null && !status.isEmpty()) {
            queryWrapper.like("status", status);
        }

        // 4. 按参加人数搜索
        if (minCeiling != null && maxCeiling != null) {
            queryWrapper.between("maxceiling", minCeiling, maxCeiling);
        } else if (minCeiling != null) {
            queryWrapper.ge("maxceiling", minCeiling);
        } else if (maxCeiling != null) {
            queryWrapper.le("maxceiling", maxCeiling);
        }
        queryWrapper.orderByDesc("timeproceed1");

        return list(queryWrapper);
    }
    //获取最大参加人数
    @Override
    public int getMaxCeilingByActivityId(int activityId) {
        Activities activity = getById(activityId);
        return activity != null ? activity.getMaxceiling() : 0;
    }
    @Override
    public List<Activities> getUserParticipatedActivities(int userId) {
        // 1. 通过 EventRegist 查询用户参与的所有活动的 activityid
        QueryWrapper<EventRegist> eventRegistQueryWrapper = new QueryWrapper<>();
        eventRegistQueryWrapper.eq("userid", userId);
        List<EventRegist> registrations = eventRegistService.list(eventRegistQueryWrapper);

        if (registrations.isEmpty()) {
            return null; // 如果没有参加的活动，返回 null 或者空列表
        }

        // 2. 提取出所有的 activityid
        List<Integer> activityIds = registrations.stream()
                .map(EventRegist::getActivityid)
                .distinct()
                .collect(Collectors.toList());  // 使用 collect(Collectors.toList()) 代替 toList()

        // 3. 根据 activityid 和 status 查询 Activities 表中的活动信息
        QueryWrapper<Activities> activitiesQueryWrapper = new QueryWrapper<>();
        activitiesQueryWrapper.in("activityid", activityIds)
                .in("status", "进行中", "报名中");
        activitiesQueryWrapper.orderByDesc("timeproceed1");
        return this.list(activitiesQueryWrapper);
    }
    @Override
    public List<Activities> getUserParticipatedFinishedActivities(int userId) {
        // 1. 通过 EventRegist 查询用户参与的所有活动的 activityid
        QueryWrapper<EventRegist> eventRegistQueryWrapper = new QueryWrapper<>();
        eventRegistQueryWrapper.eq("userid", userId);
        List<EventRegist> registrations = eventRegistService.list(eventRegistQueryWrapper);

        if (registrations.isEmpty()) {
            return null; // 如果没有参加的活动，返回 null 或者空列表
        }

        // 2. 提取出所有的 activityid
        List<Integer> activityIds = registrations.stream()
                .map(EventRegist::getActivityid)
                .distinct()
                .collect(Collectors.toList());  // 使用 collect(Collectors.toList()) 代替 toList()

        // 3. 根据 activityid 和 status 查询 Activities 表中的活动信息，状态为“已结束”
        QueryWrapper<Activities> activitiesQueryWrapper = new QueryWrapper<>();
        activitiesQueryWrapper.in("activityid", activityIds)
                .eq("status", "已结束");
        activitiesQueryWrapper.orderByDesc("timeproceed1");
        return this.list(activitiesQueryWrapper);
    }
    //修改活动要求
    @Override
    public boolean updateActivityRequest(int activityId, String activityRequest) {
        UpdateWrapper<Activities> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activityid", activityId);

        Activities activity = new Activities();
        activity.setActivityrequest(activityRequest);

        return this.update(activity, updateWrapper);
    }
    //修改活动其他说明
    @Override
    public boolean updateActivityOthers(int activityId, String others) {
        UpdateWrapper<Activities> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activityid", activityId);

        Activities activity = new Activities();
        activity.setOthers(others);

        return this.update(activity, updateWrapper);
    }
//    @Override
//    public void redisTest (){
//        redisTemplate.opsForValue().set("name","张三");
//    }
    @Override
    public void deleteAllActivitiesFromRedis() {
        Set<String> keys = redisTemplate.keys(REDIS_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            System.out.println("Deleted " + keys.size() + " activities from Redis.");
        } else {
            System.out.println("No activities found in Redis to delete.");
        }
    }
    //近七天每天的活动数量
    @Override
    public Map<String, Integer> getActivitiesCountInLast7Days() {
        Map<String, Integer> activitiesCount = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = now.minusDays(i).toLocalDate();
            QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("timeproceed1", date.atStartOfDay(), date.plusDays(1).atStartOfDay());
            Long count = baseMapper.selectCount(queryWrapper);  // 将 int 改为 Long
            activitiesCount.put(date.toString(), count.intValue());  // 将 Long 转为 int
        }
        return activitiesCount;
    }

    @Override
    public int countActivity() {
        return (int) this.count();  // 统计所有活动的数量
    }

    @Override
    public Map<String, Integer> getStatusCounts() {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status", "COUNT(*) as count").groupBy("status");

        List<Map<String, Object>> results = baseMapper.selectMaps(queryWrapper);
        Map<String, Integer> statusCounts = new HashMap<>();

        for (Map<String, Object> result : results) {
            String status = (String) result.get("status");
            Integer count = ((Long) result.get("count")).intValue();
            statusCounts.put(status, count);
        }

        return statusCounts;
    }

    @Override
    public Map<String, Integer> getTypeCounts() {
        QueryWrapper<Activities> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("type", "COUNT(*) as count").groupBy("type");

        List<Map<String, Object>> results = baseMapper.selectMaps(queryWrapper);
        Map<String, Integer> typeCounts = new HashMap<>();

        for (Map<String, Object> result : results) {
            String status = (String) result.get("type");
            Integer count = ((Long) result.get("count")).intValue();
            typeCounts.put(status, count);
        }

        return typeCounts;
    }

    @Override
    public Map<String, Integer> getMaxCeilingCounts() {
        Map<String, Integer> result = new HashMap<>();

        // 查询 2 的数量
        QueryWrapper<Activities> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("maxceiling", 2);
        Long count1 = baseMapper.selectCount(wrapper1);
        result.put("2", count1.intValue());

        // 查询 3-5 的数量
        QueryWrapper<Activities> wrapper2 = new QueryWrapper<>();
        wrapper2.between("maxceiling", 3, 5);
        Long count2 = baseMapper.selectCount(wrapper2);
        result.put("3-5", count2.intValue());

        // 查询 6-10 的数量
        QueryWrapper<Activities> wrapper3 = new QueryWrapper<>();
        wrapper3.between("maxceiling", 6, 10);
        Long count3 = baseMapper.selectCount(wrapper3);
        result.put("6-10", count3.intValue());

        // 查询 11-999 的数量
        QueryWrapper<Activities> wrapper4 = new QueryWrapper<>();
        wrapper4.between("maxceiling", 11, 999);
        Long count4 = baseMapper.selectCount(wrapper4);
        result.put("11-999", count4.intValue());

        return result;
    }

    @Override
    public List<Activities> getAllActivities() {
        return list();  // 使用list() 方法获取所有记录
    }
}