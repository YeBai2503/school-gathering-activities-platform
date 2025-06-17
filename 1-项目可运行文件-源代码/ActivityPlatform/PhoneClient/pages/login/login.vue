<template>
  <view class="login-page">
    <view class="login-container">
      <text class="login-title">登录</text>
      
      <view class="input-group">
        <label for="username">用户名</label>
        <input type="text" id="loginUsername" v-model="loginUsername" placeholder="请输入用户名" />
      </view>
      
      <view class="input-group">
        <label for="password">密码</label>
        <input type="password" id="loginPassword" v-model="loginPassword" placeholder="请输入密码" />
      </view>
      
      <button class="login-button" @click="handlelogin">登录</button>
      <text class="loginerror" v-model="loginError" id="loginError">{{ loginError }}</text>
      <view class="login-link">
        <navigator url="../register/register">没有账户？点击注册</navigator>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loginUsername: '',
      loginPassword: '',
      loginError: ''
    };
  },
  methods: {
    handlelogin() {
      if (!this.loginUsername || !this.loginPassword) {
        this.loginError = "用户名和密码不能为空";
        uni.showToast({
          title: this.loginError,
          icon: 'none'
        });
        return;
      }
      uni.request({
        url: 'http://192.168.1.43:8080/auth/login', 
        method: 'POST',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          usernameoremail: this.loginUsername,
          password: this.loginPassword
        },
        success: (res) => {
          if (res.statusCode === 200 && res.data.code === 200) {
            uni.setStorageSync('userid', res.data.data.id);
			uni.setStorageSync('username', res.data.data.username);
            uni.showToast({
              title: '登录成功，欢迎:' + res.data.data.username,
              icon: 'success'
            });
            uni.switchTab({
              url: '../home/home'
            });
          } else if (res.data.code === 401) {
            // 密码错误
            uni.showModal({
              title: '错误',
              content: '用户名或密码错误，请重新输入',
              showCancel: false
            });
          } else if (res.data.code === 404) {
            // 用户不存在
            uni.showModal({
              title: '错误',
              content: '用户不存在，请检查用户名或注册新账户',
              showCancel: false
            });
          } else {
            uni.showToast({
              title: '登录失败，用户名或密码错误',
              icon: 'none'
            });
            this.loginError = res.data.message;
          }
        },
        fail: (error) => {
          this.loginError = '网络错误，请稍后重试';
          uni.showToast({
            title: this.loginError,
            icon: 'none'
          });
        }
      });
    }
  }
};
</script>


<style scoped>
	.login-page {
	  background-color: #fff3e0;;
	  display: flex;
	  justify-content: center;
	  align-items: center;
	  height: 100vh;
	  background-image: url('../../static/3.jpg');
	}

	.login-container {
	 background-color:  #f9f2e7;
	  padding: 2rem; 
	  border-radius: 12px; 
	  box-shadow: 0 0.25rem 0.625rem rgba(0, 0, 0, 0.1);
	  width: 70%;
	  max-width: 25rem; /* 最大宽度改为rem，1rem通常为16px */
	  text-align: center;
	}

	.login-title {
	  font-size: 1.2rem; /* 改为rem单位 */
	  font-weight: bold;
	  margin-bottom: 1.25rem;
	}

	.input-group {
	  margin-bottom: 1rem; /* 改为rem单位 */
	  text-align: left;
	}

	.input-group input {
	  width: 100%;
	  margin-top: 0.5rem;
	  font-weight: normal;
	  padding: 0.625rem; /* 改为rem单位 */
	  border: 0.01rem solid #ccc;
	  border-radius: 4px;
	  font-size: 0.75rem; /* 改为rem单位 */
	}

	.login-button {
	  align-items: center;
	  background-color: #0056FF;
	  color: white;
	  border: none;
	  padding: 0.5rem 0; 
	  border-radius: 4px;
	  font-size: 0.75rem; 
	  cursor: pointer;
	  margin-top: 0.625rem;
	  width: 100%;
	}

	.login-link {
	  margin-top: 1rem; 
	  font-size: 0.875rem;
	  text-align: center;
	  color: #6c63ff;
	}
	
	.loginerror {
	  color: red;
	  margin-top: 0.625rem; 
	  text-align: center;
	}
</style>
