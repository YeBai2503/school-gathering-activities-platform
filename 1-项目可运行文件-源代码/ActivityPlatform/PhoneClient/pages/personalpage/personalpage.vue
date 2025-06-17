<template>
  <view class="profile-page">
    <text class="username">个人信息</text>
    <!-- 个人背景部分 -->
    <view class="profile-header">
      <view class="profile-info">
        <image class="avatar" :src="avatarImage" />
        <view class="user-info">
          <text class="username1">{{ nickname }}</text>
        </view>
      </view>
    </view>

    <!-- 个人资料 -->
    <view class="profile-details">
      <!-- ID -->
      <view class="detail-item">
        <text class="item-label">ID：</text>
        <text class="readonly-text">{{ userId }}</text>
      </view>

      <!-- 用户名 -->
      <view class="detail-item">
        <text class="item-label">用户名：</text>
        <text class="readonly-text">{{ username }}</text>
      </view>

      <!-- 权限等级 -->
      <view class="detail-item">
        <text class="item-label">权限等级：</text>
        <text class="readonly-text">{{ permission }}</text>
      </view>

      <!-- 个人评分 -->
      <view class="detail-item">
        <text class="item-label">个人评分：</text>
        <text class="readonly-text">{{ userScore }}</text>
      </view>

      <!-- 昵称 -->
      <view class="detail-item">
        <text class="item-label">昵称：</text>
        <input 
          class="input-field" 
          type="text" 
          v-model="nickname" 
          :readonly="!nicknameEditable" 
          :class="{ 'disabled': !nicknameEditable }" 
        />
        <button class="edit-btn" @click="toggleEdit('nickname')">
          {{ nicknameEditable ? '保存' : '修改' }}
        </button>
      </view>

      <!-- 邮箱 -->
      <view class="detail-item">
        <text class="item-label">邮箱：</text>
        <input 
          class="input-field" 
          type="email" 
          v-model="email" 
          :readonly="!emailEditable" 
          :class="{ 'disabled': !emailEditable }" 
        />
        <button class="edit-btn" @click="toggleEdit('email')">
          {{ emailEditable ? '保存' : '修改' }}
        </button>
      </view>

      <!-- 手机号 -->
      <view class="detail-item">
        <text class="item-label">手机号：</text>
        <input 
          class="input-field" 
          type="tel" 
          v-model="phone" 
          :readonly="!phoneEditable" 
          :class="{ 'disabled': !phoneEditable }" 
        />
        <button class="edit-btn" @click="toggleEdit('phone')">
          {{ phoneEditable ? '保存' : '修改' }}
        </button>
      </view>
    </view>

    <!-- 修改密码按钮 -->
    <view class="logout-container">
      <button class="logout-btn1" @click="showChangePasswordModal">修改密码</button>
    </view>
    
    <!-- 登出按钮 -->
    <view class="logout-container">
      <button class="logout-btn" @click="confirmLogout">退出登录</button>
    </view>
    
    <!-- 注销按钮 -->
    <view class="logout-container">
      <button class="logout-btn" @click="confirmAccountDeletion">销号跑路</button>
    </view>
    
    <!-- 修改密码弹窗 -->
    <view v-if="showPasswordModal" class="modal">
      <view class="modal-content">
        <text>输入原密码:</text>
        <input class="modal1" type="password" v-model="oldPassword" />
        <text>输入新密码:</text>
        <input class="modal1" type="password" v-model="newPassword" />
        <text>再次输入新密码:</text>
        <input class="modal1" type="password" v-model="confirmPassword" />
        <button  class="modal2" @click="updatePassword">确认修改</button>
        <button   @click="showPasswordModal = false">取消</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  onLoad() {
    const userId = uni.getStorageSync('userid'); 
    if (userId) {
      this.userId = userId;
      this.getUserInfo();
    } else {
      console.log('用户ID未找到');
    }
  },
  data() {
    return {
      userId: '', // 用户ID
      username: '', // 用户名
      permission: '', // 权限等级
      userScore: '', // 个人评分
      nickname: '', // 昵称
      email: '', // 邮箱
      phone: '', // 手机号
      password: '', // 当前登录账户密码，初始为空
      backgroundImage: '/static/back.jfif', // 背景图片
      avatarImage: '/static/logo1.png', // 头像图片
      nicknameEditable: false, // 昵称可编辑状态
      emailEditable: false, // 邮箱可编辑状态
      phoneEditable: false, // 手机号可编辑状态
      showPasswordModal: false, // 修改密码弹窗显示状态
      oldPassword: '', // 输入的原密码
      newPassword: '', // 输入的新密码
      confirmPassword: '', // 再次输入的新密码
    };
  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      const that = this;
      uni.request({
        url: `http://192.168.1.43:8080/auth/showuser/${this.userId}`,
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
            const data = res.data.data;
            that.username = data.username;
            that.nickname = data.nickname;
            that.email = data.email;
            that.phone = data.pnumber;
            that.permission = data.permission;
            that.userScore = data.reputation;
            that.userId = data.id;
            that.password = data.password; // 假设从后端可以获取到密码
          } else {
            console.log('获取信息错误');
          }
        },
        fail(error) {
          console.log('网络错误，请稍后重试');
        }
      });
    },
    // 切换字段编辑状态
    toggleEdit(field) {
      if (this[`${field}Editable`]) {
        // 保存内容到后端
        if (field === 'nickname') {
          this.updateNickname();
        } else if (field === 'email') {
          this.updateEmail();
        } else if (field === 'phone') {
          this.updatePhone();
        }
      }
      this[`${field}Editable`] = !this[`${field}Editable`];
    },
    // 更新昵称
    updateNickname() {
      const that = this;
      uni.request({
        url: `http://192.168.1.43:8080/auth/${this.userId}/nickname`,
        method: 'PUT',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          nickname: this.nickname
        },
        success(res) {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '昵称更新成功',
              icon: 'success'
            });
            that.nicknameEditable = false;
          } else {
            uni.showToast({
              title: '昵称更新失败',
              icon: 'error'
            });
            console.log('昵称更新失败');
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'error'
          });
          console.log('网络错误，请稍后重试');
        }
      });
    },
    // 更新邮箱
    updateEmail() {
      const that = this;
      uni.request({
        url: `http://192.168.1.43:8080/auth/${this.userId}/email`,
        method: 'PUT',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          email: this.email
        },
        success(res) {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '邮箱更新成功',
              icon: 'success'
            });
            that.emailEditable = false;
          } else {
            uni.showToast({
              title: '邮箱更新失败',
              icon: 'error'
            });
            console.log('邮箱更新失败');
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'error'
          });
          console.log('网络错误，请稍后重试');
        }
      });
    },
    // 更新手机号
    updatePhone() {
      const that = this;
      uni.request({
        url: `http://192.168.1.43:8080/auth/${this.userId}/pnumber`,
        method: 'PUT',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          pnumber: this.phone
        },
        success(res) {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '手机号更新成功',
              icon: 'success'
            });
            that.phoneEditable = false;
          } else {
            uni.showToast({
              title: '手机号更新失败',
              icon: 'error'
            });
            console.log('手机号更新失败');
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'error'
          });
          console.log('网络错误，请稍后重试');
        }
      });
    },
    // 显示修改密码弹窗
    showChangePasswordModal() {
      this.showPasswordModal = true;
      this.oldPassword = '';
      this.newPassword = '';
      this.confirmPassword = '';
    },
    // 修改密码
    updatePassword() {
      if (this.oldPassword !== this.password) {
        uni.showToast({
          title: '原密码错误',
          icon: 'error'
        });
        return;
      }
      if (this.newPassword !== this.confirmPassword) {
        uni.showToast({
          title: '两次输入的新密码不一致',
          icon: 'error'
        });
        return;
      }
      
      const that = this;
      uni.request({
        url: `http://192.168.1.43:8080/auth/${this.userId}/password`,
        method: 'PUT',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          password: this.newPassword
        },
        success(res) {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '密码修改成功',
              icon: 'success'
            });
            that.showPasswordModal = false; // 关闭弹窗
          } else {
            uni.showToast({
              title: '密码修改失败',
              icon: 'error'
            });
            console.log('密码修改失败');
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'error'
          });
          console.log('网络错误，请稍后重试');
        }
      });
    },
    // 显示登出确认弹窗
    confirmLogout() {
      uni.showModal({
        title: '确认退出登录',
        content: '您确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            this.handleLogout();
          }
        }
      });
    },
    // 退出登录
    handleLogout() {
      uni.clearStorageSync(); // 清除本地存储的用户数据
      uni.reLaunch({
        url: '/pages/login/login' // 跳转到登录页面
      });
    },
    // 显示销号确认弹窗
    confirmAccountDeletion() {
      uni.showModal({
        title: '确认销号',
        content: '您确定要销号并删除所有数据吗？此操作不可恢复。',
        success: (res) => {
          if (res.confirm) {
            this.handleAccountDeletion();
          }
        }
      });
    },
    // 删除账号
    handleAccountDeletion() {
      const that = this;
      uni.request({
        url: `http://192.168.1.43:8080/auth/${this.userId}`,
        method: 'DELETE',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '账号已注销',
              icon: 'success'
            });
            // 清除本地存储的用户数据并跳转到登录页面
            uni.clearStorageSync();
            uni.reLaunch({
              url: '/pages/login/login'
            });
          } else {
            uni.showToast({
              title: '注销失败',
              icon: 'error'
            });
            console.log('注销失败');
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'error'
          });
          console.log('网络错误，请稍后重试');
        }
      });
    }
  }
}
</script>
<style scoped>
/* 页面容器样式 */
.profile-page {
  padding: 5vw; /* 保持不变 */
  background-color: #fff3e0; /* 背景色 */
  min-height: 100vh; /* 保持高度相对于视口 */
  background-image: url('../../static/3.jpg');
}

/* 个人背景部分样式 */
.profile-header {
  position: relative;
  height: 13vh; /* 使用视口高度 */
  margin-bottom: 5vw; /* 保持不变 */
}

.background-blur {
  width: 100%; /* 宽度保持不变 */
  height: 100%; /* 高度保持不变 */
  object-fit: cover; /* 保持 */
  filter: blur(0.25rem); /* 保持 */
  position: absolute; /* 保持 */
  top: 1rem; /* 保持 */
  left: 0; /* 保持 */
}

.profile-info {
  position: relative;
  z-index: 1; 
  display: flex;
  align-items: center;
  padding: 2vw; /* 保持 */
}

.avatar {
    width: 5rem; /* 头像使用相对单位 */
    height: 5rem; /* 头像使用相对单位 */
    border-radius: 50%; /* 使用50%实现圆形 */
    margin-right: 0.625rem; /* 保持 */
}

.user-info {
  flex-grow: 1; /* 保持 */
  margin-left: 4vw; /* 保持 */
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 1.5rem; /* 保持 */
  font-weight: bold; /* 保持 */
  display: grid;
  place-items: center; /* 保持 */
}

.username1 {
  font-size: 1.3rem; /* 保持 */
  font-weight: bold; /* 保持 */
}

.department {
  font-size: 4vw; /* 使用视口宽度的百分比 */
}

/* 个人资料样式 */
.profile-details {
  background-color:  #f9f2e7;
  padding: 5vw; /* 保持 */
  border-radius: 10px; /* 边框圆角 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 保持 */
}

.detail-item {
  display: flex; /* 保持 */
  justify-content: space-between; /* 保持 */
  align-items: center; /* 保持 */
  margin-bottom: 4vw; /* 保持 */
}

.item-label {
  font-size: 1.1rem; /* 保持 */
  font-weight: bold; /* 保持 */
  color: #333; /* 字体颜色 */
}

.input-field {
  flex: 1; /* 保持 */
  margin-right: 3vw; /* 保持 */
  padding: 3vw; /* 保持 */
  font-size: 3vw; /* 使用视口宽度的百分比 */
  border: transparent; /* 保持 */
  border-radius: 5px; /* 边框圆角 */
  background-color: #f9f9f9; /* 背景色 */
}

.readonly-text {
  font-size: 4vw; /* 使用视口宽度的百分比 */
  color: #555; /* 字体颜色 */
}

.edit-btn {
  padding: 0.5vw 4vw; /* 保持 */
  font-size: 2vw; /* 使用视口宽度的百分比 */
  background-color: #f0f0f0; /* 背景色 */
  border-radius: 5px; /* 边框圆角 */
  border: none; /* 无边框 */
  color: #333; /* 字体颜色 */
  transition: background-color 0.3s; /* 保持 */
}

.edit-btn:hover {
  background-color: #e0e0e0; /* 保持 */
}

/* 确认修改按钮 */
.button-container {
	
  text-align: center; /* 保持 */
  margin-top: 8vw; /* 保持 */
}

.confirm-btn {
  padding: 2vw 4vw; /* 保持 */
  background-color: #4CAF50; /* 背景色 */
  color: white; /* 字体颜色 */
  font-size: 3vw; /* 使用视口宽度的百分比 */
  border-radius: 5px; /* 边框圆角 */
  border: none; /* 无边框 */
  transition: background-color 0.3s; /* 保持 */
}

.confirm-btn:hover {
  background-color: #45a049; /* 保持 */
}

/* 注销按钮样式 */
.logout-container {
  margin-top: 5vw; /* 保持 */
  text-align: center; /* 保持 */
}

.logout-btn {
  width: 92%; /* 保持 */
  padding: 3vw 8vw; /* 保持 */
  background-color: #ff4d4f; /* 背景色 */
  color: white; /* 字体颜色 */
  font-size: 3vw; /* 使用视口宽度的百分比 */
  border-radius: 5px; /* 边框圆角 */
  border: none; /* 无边框 */
  transition: background-color 0.3s; /* 保持 */
}

.logout-btn:hover {
  background-color: #e63946; /* 保持 */
}

/* 禁用状态样式 */
.disabled {
  pointer-events: none; /* 保持 */
  background-color: #f0f0f0; /* 背景色 */
  color: #999; /* 字体颜色 */
}

.modal {
  position: fixed; /* 保持 */
  top: 0; /* 保持 */
  left: 0; /* 保持 */
  right: 0; /* 保持 */
  bottom: 0; /* 保持 */
  background-color: rgba(0, 0, 0, 0.5); /* 背景色 */
  display: flex; /* 保持 */
  justify-content: center; /* 保持 */
  align-items: center; /* 保持 */
  
}
.modal1 {
	 border-radius: 8px; /* 边框圆角 */
   background-color: #e0e0e0; /* 背景色 */
   padding: 2vw; /* 使用视口宽度的百分比 */
   margin: 2vw; /* 使用视口宽度的百分比 */
}
.modal2 {
	
  background-color: #ADD8E6; /* 浅蓝色 */
   
}
.modal-content {
	font-size: 1.1rem; /* 保持 */
  background: #fff; /* 背景色 */
  padding: 2vw; /* 使用视口宽度的百分比 */
  border-radius: 8px; /* 边框圆角 */
  width: 80vw; /* 使用视口宽度的百分比 */
}
.logout-btn1 {
  width: 92%; /* 宽度 */
  padding: 3vw 8vw; /* 使用视口宽度的百分比 */
  background-color: #0000FF; /* 背景色 */
  color: white; /* 字体颜色 */
  font-size: 3vw; /* 使用视口宽度的百分比 */
  border-radius: 5px; /* 边框圆角 */
  border: none; /* 无边框 */
  transition: background-color 0.3s; /* 保持 */
}

.logout-btn1:hover { /* 改正 hover 样式 */
  background-color: #00008B; /* 深蓝色背景 */
}
</style>








