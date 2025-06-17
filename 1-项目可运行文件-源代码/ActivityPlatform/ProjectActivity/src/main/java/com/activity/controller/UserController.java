package com.activity.controller;

import com.activity.model.Result;
import com.activity.model.ResultCode;
import com.activity.model.User;
import com.activity.service.FileUploadService;
import com.activity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "用户接口")
@RequestMapping("/auth")
@Tag("用户操作接口")
public class UserController {
    public static final Logger log =LoggerFactory.getLogger(UserController.class);
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserService userService;
    //注册功能，传送user参数
    @Operation(summary = "注册功能",description = "注册，传输User")
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
            return Result.success(registeredUser.getUsername());
        } else {
            return Result.error(ResultCode.FAIL, "Username already exists");
        }
    }

    @Operation(summary = "获取当前登录用户的信息",description = "")
    @GetMapping("/getUserInfo")
    public Result<User> getThisUserInfo(HttpSession session) {
        log.info("getthisuserSession: {}",session.hashCode());
        User loggedInUser = (User) session.getAttribute("loggedUser");
        if (loggedInUser != null) {
            return Result.success(loggedInUser);
        } else {
            return Result.error(ResultCode.UNAUTHORIZED, "User not logged in");
        }
    }

    //，传输两个String，一个是用户名或者邮箱，一个是密码；返回了一个User数据
    @Operation(summary = "登录功能",description = "传输两个String，一个是用户名或者邮箱，一个是密码；返回了一个User数据")
    @PostMapping("/login")
    public Result<User> login(@RequestParam String usernameoremail, @RequestParam String password, HttpSession session) {
        User user1 = userService.loginUserbyusername(usernameoremail, password);
        User user2 = userService.loginUserbyemail(usernameoremail, password);
        if (user1 != null || user2 != null) {
            User loggedInUser = user1 != null ? user1 : user2;
            log.info("loginSession: {}",session.hashCode());
            session.setAttribute("loggedUser", loggedInUser);
            return Result.success(loggedInUser);
        } else {
            return Result.error(ResultCode.UNAUTHORIZED, "Invalid username or password");
        }
    }

    @Operation(summary = "用户总数量",description = "")
    @GetMapping("/count")
    public Result<Integer> getUserCount() {
        int userCount = userService.countUsers();
        return Result.success(userCount);
    }
    //；
    @Operation(summary = "登出",description = "")
    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.removeAttribute("loggedUser");
        return Result.success("Successfully logged out.");
    }

    //展示个人信息的功能；id=登录后user的id

    @Operation(summary = "展示个人信息的功能",description = "id=登录后user的id")
    @GetMapping("/showuser/{id}")
    public Result<User> getUserInfo(@PathVariable int id) {
        User user = userService.getUser(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error(ResultCode.NOT_FOUND, "User not found");
        }
    }
    //；
    @Operation(summary = "注销功能",description = "销毁账号，传输当前登录用户的id")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable int id) {
        boolean success = userService.deleteUser(id);
        return success ? Result.success("User account deleted successfully") : Result.error(ResultCode.FAIL, "Failed to delete user account");
    }
    //，
    @Operation(summary = "修改用户名",description = "传输当前登录用户的id和用户名")
    @PutMapping("/{id}/username")
    public Result<String> updateUsername(@PathVariable int id, @RequestParam String username) {
        boolean success = userService.updateUserFieldById(id, "username", username);
        return success ? Result.success("Username updated successfully") : Result.error(ResultCode.FAIL, "Failed to update username");
    }
    //，
    @Operation(summary = "修改昵称",description = "传输当前登录用户的id和昵称")
    @PutMapping("/{id}/nickname")
    public Result<String> updateNickname(@PathVariable int id, @RequestParam String nickname) {
        boolean success = userService.updateUserFieldById(id, "nickname", nickname);
        return success ? Result.success("Nickname updated successfully") : Result.error(ResultCode.FAIL, "Failed to update nickname");
    }
    //，
    @Operation(summary = "修改社团",description = "传输当前登录用户的id和社团名称")
    @PutMapping("/{id}/community")
    public Result<String> updateCommunity(@PathVariable int id, @RequestParam String community) {
        boolean success = userService.updateUserFieldById(id, "community", community);
        return success ? Result.success("Community updated successfully") : Result.error(ResultCode.FAIL, "Failed to update community");
    }
    //，
    @Operation(summary = "修改性别",description = "传输当前登录用户的id和性别")
    @PutMapping("/{id}/sex")
    public Result<String> updateSex(@PathVariable int id, @RequestParam String sex) {
        boolean success = userService.updateUserFieldById(id, "sex", sex);
        return success ? Result.success("Sex updated successfully") : Result.error(ResultCode.FAIL, "Failed to update sex");
    }
    @Operation(summary = "修改邮箱",description = "传输当前登录用户的id和email")
    @PutMapping("/{id}/email")
    public Result<String> updateEmail(@PathVariable int id, @RequestParam String email) {
        boolean success = userService.updateUserFieldById(id, "email", email);
        return success ? Result.success("Email updated successfully") : Result.error(ResultCode.FAIL, "Failed to update email");
    }
    //，
    @Operation(summary = "修改电话号码",description = "传输当前登录用户的id和电话号码")
    @PutMapping("/{id}/pnumber")
    public Result<String> updatePnumber(@PathVariable int id, @RequestParam String pnumber) {
        boolean success = userService.updateUserFieldById(id, "pnumber", pnumber);
        return success ? Result.success("Phone number updated successfully") : Result.error(ResultCode.FAIL, "Failed to update phone number");
    }
    //，
    @Operation(summary = "修改大学名称",description = "传输当前登录用户的id和大学名称")
    @PutMapping("/{id}/university")
    public Result<String> updateUniversity(@PathVariable int id, @RequestParam String university) {
        boolean success = userService.updateUserFieldById(id, "university", university);
        return success ? Result.success("University updated successfully") : Result.error(ResultCode.FAIL, "Failed to update University");
    }
    //，
    @Operation(summary = "修改密码",description = "传输当前登录用户的id和密码")
    @PutMapping("/{id}/password")
    public Result<String> updatePassword(@PathVariable int id, @RequestParam int password) {
        boolean success = userService.updateUserFieldById(id, "password", password);
        return success ? Result.success("Password number updated successfully") : Result.error(ResultCode.FAIL, "Failed to update phone Password");
    }
    //，
    @Operation(summary = "进入后台管理判定",description = "如果不是管理员进不去，传输当前登录用户的用户名")
    @GetMapping("/admin")
    public Result<String> adminAccess(@RequestParam String username) {
        if (userService.checkPermission(username, 1)) {
            return Result.success("Welcome to the admin dashboard, " + username);
        } else {
            return Result.error(ResultCode.FORBIDDEN, "Access denied. You do not have permission to access the admin dashboard.");
        }
    }
    //，次
    @Operation(summary = "修改个人信誉分",description = "每次有活动由该用户新发布需要调用一")
    @PostMapping("/{userId}/update-reputation")
    public Result<String> updateReputationBasedOnActivityScores(@PathVariable int userId) {
        boolean success = userService.updateReputationBasedOnActivityScores(userId);
        if (success) {
            return Result.success("Successfully updated reputation for user ID: " + userId);
        } else {
            return Result.error(ResultCode.FAIL, "Failed to update reputation. Please check the user ID or ensure the user has published activities.");
        }
    }
    @Operation(summary = "修改头像图片路径",description = "传输当前用户id和图片路径")
    @PostMapping("/{userId}/upload-profile-image")//，
    public Result<String> uploadProfileImage(@PathVariable int userId, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = fileUploadService.uploadImage(file);
            boolean success = userService.updateProfileImage(userId, imageUrl);
            if (success) {
                return Result.success("Successfully updated profile image for user ID: " + userId);
            } else {
                return Result.error(ResultCode.FAIL, "Failed to update profile image. Please check the user ID.");
            }
        } catch (IOException e) {
            return Result.error(ResultCode.FAIL, "Failed to upload image: " + e.getMessage());
        }
    }

    @Operation(summary = "信誉分前5的用户",description = "")
    @GetMapping("/top5Reputation")
    public Result<List<Map<String, Object>>> getTop5UsersByReputation() {
        try {
            List<Map<String, Object>> top5Users = userService.getTop5UsersByReputation();
            return Result.success(top5Users);
        } catch (Exception e) {
            return Result.error(ResultCode.FAIL,"获取信誉分前5名用户失败");
        }
    }
    @Operation(summary = "通过邮箱获取密码")
    @GetMapping("/getPasswordByEmail")
    public Result<String> getPasswordByEmail(@RequestParam String email) {
        String password = userService.getPasswordByEmail(email);

        if (password != null) {
            return Result.success(password);
        } else {
            return Result.error(ResultCode.NOT_FOUND, "用户不存在");
        }
    }
    @Operation(summary = "返回所有的用户信息")
    @GetMapping("/getAllUsers")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }
}
