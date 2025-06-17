package com.activity.service.impl;

import com.activity.mapper.UserMapper;
import com.activity.model.Activities;
import com.activity.model.User;
import com.activity.service.ActivityService;
import com.activity.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private ActivityService activityService;
    @Override
    public User registerUser(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        QueryWrapper<User> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("email",user.getEmail());
        User existingUser1=this.getOne(queryWrapper1);
        User existingUser = this.getOne(queryWrapper);

        if (existingUser != null) {
            // 用户名已存在
            return null;
        }
        if(existingUser1!=null){
            //邮箱已存在
            return null;
        }
        user.setPermission(0);
        user.setReputation(80);
        boolean result = this.save(user);
        return result ? user : null;
    }
    @Override
    public User loginUserbyusername(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User getUser(int id) {
        return getById(id);
    }
    public boolean updateUserFieldById(int id, String fieldName, Object fieldValue) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set(fieldName, fieldValue);
        return update(updateWrapper);
    }

    @Override
    public boolean deleteUser(int id) {
        return removeById(id);
    }

    @Override
    public User loginUserbyemail(String email, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = this.getOne(queryWrapper);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean checkPermission(String username, int requiredLevel) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);
        return user != null && user.getPermission() >= requiredLevel;
    }

    @Override
    public boolean updateReputationBasedOnActivityScores(int userId) {
        // 获取该用户发布的所有活动
        List<Activities> activities = activityService.getActivitiesByHeadId(userId);

        // 检查活动列表是否为空
        if (activities == null || activities.isEmpty()) {
            return false;
        }
        // 计算活动评分的平均值
        OptionalDouble averageScore = activities.stream()
                .mapToInt(Activities::getScore)
                .average();

        if (averageScore.isPresent()) {
            double avg = averageScore.getAsDouble();

            // 更新用户的信誉分
            User user = this.getById(userId);
            if (user != null) {
                user.setReputation((int) avg); // 将平均分更新为用户的reputation
                return this.updateById(user);
            }
        }
        return false;
    }
    @Override
    public boolean updateProfileImage(int userId, String imageUrl) {
        User user = this.getById(userId);
        if (user != null) {
            user.setProfileimageurl(imageUrl);
            return this.updateById(user);
        }
        return false;
    }

    @Override
    public int countUsers() {
        return (int) this.count();  // 统计所有用户的数量
    }

    @Override
    public List<Map<String, Object>> getTop5UsersByReputation() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username", "reputation")
                .orderByDesc("reputation")
                .last("limit 5");

        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public String getPasswordByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);

        User user = this.getOne(queryWrapper);

        if (user != null) {
            return user.getPassword();
        } else {
            return null; // 如果找不到用户，返回 null
        }
    }
    @Override
    public List<User> getAllUsers() {
        return this.list(); // 获取所有用户
    }
}
