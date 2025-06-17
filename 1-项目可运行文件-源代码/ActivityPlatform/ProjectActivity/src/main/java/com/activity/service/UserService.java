package com.activity.service;

import com.activity.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService extends IService<User> {
    User registerUser(User user);
    User loginUserbyusername(String username, String password);
    User getUser(int id);
    boolean updateUserFieldById(int id, String fieldName, Object fieldValue);
    boolean deleteUser(int id);
    User loginUserbyemail(String email,String password);
    boolean checkPermission(String username, int requiredLevel);
    boolean updateReputationBasedOnActivityScores(int userId);
    boolean updateProfileImage(int userId, String imageUrl);
    int countUsers();
    List<Map<String, Object>> getTop5UsersByReputation();
    String getPasswordByEmail(String email);
    List<User> getAllUsers();
}
