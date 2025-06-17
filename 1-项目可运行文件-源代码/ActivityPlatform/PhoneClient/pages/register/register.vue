<template>
	<view class="register-page">
		<view class="register-container">
		  <view>
		    <text class="register-title">注册</text>
		  </view>
		  <form>
		  <!--<form @submit.prevent="handleRegister">-->
		    <view class="input-group">
		      <label for="registerEmail">邮箱号</label>
		      <input 
		        type="email" 
		        id="registerEmail" 
		        v-model="registerEmail" 
		        placeholder="请输入邮箱号" 
		        required 
		      />
		    </view>
		    <view class="input-group">
		      <label for="registerUsername">用户名</label>
		      <input 
		        type="text" 
		        id="registerUsername" 
		        v-model="registerUsername" 
		        placeholder="请输入用户名" 
		        required 
		      />
		    </view>
			 <view class="input-group">
			    <label for="registerPassword">密码</label>
			    <view class="password-container">
			      <input 
			        :type="isPasswordVisible ? 'text' : 'password'" 
			        id="registerPassword" 
			        v-model="registerPassword" 
			        placeholder="请输入密码" 
			        required 
			        class="password-input"
			      />
			      <!-- 密码可见性切换图标 -->
			      <uni-icons 
			        :type="isPasswordVisible ? 'eye-filled' : 'eye-slash-filled'" 
			        size="25" 
			        @click="togglePasswordVisibility" 
			        class="toggle-icon"
			      />
			    </view>
			  </view>
			
			  <view class="input-group">
			    <label for="comfirmRegisterPassword">确认密码</label>
			    <view class="password-container">
			      <input 
			        :type="isConfirmPasswordVisible ? 'text' : 'password'" 
			        id="comfirmRegisterPassword" 
			        v-model="comfirmRegisterPassword" 
			        placeholder="请再次输入密码" 
			        required 
			        class="password-input"
			      />
			      <!-- 确认密码可见性切换图标 -->
			      <uni-icons 
			        :type="isConfirmPasswordVisible ? 'eye-filled' : 'eye-slash-filled'" 
			        size="25" 
			        @click="toggleConfirmPasswordVisibility" 
			        class="toggle-icon"
			      />
			    </view>
			  </view>
		    <button class="register-button" @click="handleRegister">注册</button>
		    <text class="registererror">{{ registerError }}</text>
		    <view class="register-link">
		      <navigator url="../login/login">已有账户？点此登录</navigator>
		    </view>
		  </form>
		</view>
	</view>
</template>

<script>
export default {
  data() {
    return {
      registerEmail: '',
      registerUsername: '',
      registerPassword: '',
      registerError: '',
	  comfirmRegisterPassword: '',
	  isPasswordVisible: false, // 控制密码可见性
	  isConfirmPasswordVisible: false // 控制确认密码可见性
    };
  },
  methods: {
	togglePasswordVisibility(){
		this.isPasswordVisible = !this.isPasswordVisible; // 切换密码的可见性
	},
	
	toggleConfirmPasswordVisibility(){
		this.isConfirmPasswordVisible = !this.isConfirmPasswordVisible; // 切换确认密码的可见性
	},
	
    handleRegister() {
      if (!this.registerEmail || !registerUsername || !this.registerPassword) {
        this.registerError = '请填写完整信息';
        return
	  };
	  if (this.registerPassword !== this.comfirmRegisterPassword) {
		this.registerError = '两次输入的密码不一致';
		return
	  };
	  
      uni.request({
        url: 'http://192.168.1.43:8080/auth/register', 
        method: 'POST',
		header: {
		  'Content-Type': 'application/json' 
		},
        data: {
			"id": 0,
			"username": this.registerUsername,
			"password": this.registerPassword,
			"email": this.registerEmail,
			"nickname": "",
			"community": "",
			"sex": "",
			"pnumber": 0,
			"university": "",
			"hobby": "",
			"permission": 0,
			"reputation": 0,
			"profileimageurl": "",
        },
        success: (res) => {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '注册成功',
              icon: 'success'
            });
            // 注册成功后跳转到登录页面
            uni.navigateTo({
              url: '/pages/login/login'
            });
          } else {
            this.registerError = res.data.message || '注册失败';
			console.log('注册失败原因:', res.data);
          }
        },
        fail: (err) => {
          this.registerError = '网络错误，请稍后重试';
        }
      });
    }
  }
};
</script>

<style scoped>
	.register-page {
	  background-color: #fff3e0;
	  display: flex;
	  justify-content: center;
	  align-items: center;
	  height: 100vh;
	  background-image: url('../../static/3.jpg');
	}
	
	.register-container {
	  background-color: white;
	  padding: 2rem; 
	  border-radius: 12px; 
	  box-shadow: 0 0.25rem 0.625rem rgba(0, 0, 0, 0.1);
	  width: 70%;
	  max-width: 25rem; /* 最大宽度改为rem，1rem通常为16px */
	  text-align: center;
	}
	
	.register-title {
	  font-size: 1.2rem; /* 改为rem单位 */
	  font-weight: bold;
	  margin-bottom: 1.25rem; /* 改为rem单位 */
	}
	
	/* 输入框样式 */
	
	.input-group {
	  margin-bottom: 1rem; /* 改为rem单位 */
	  text-align: left;
	}
	
	.input-group label {
	  font-size: 1rem; 
	  margin-bottom: 0.3125rem; 
	  color: #333;
	}
	
	.input-group input {
	  width: 100%;
	  font-weight: normal;
	  padding: 0.625rem; 
	  border: 0.0625rem solid #ccc; 
	  border-radius: 4px;
	  font-size: 0.75rem; 
	}
	
	/* 容器样式，确保图标与输入框在同一行 */
	.password-container {
	  position: relative;
	}
	
	.password-input {
	  width: 100%;
	  margin-top: 0.3rem;
	  font-weight: normal;
	  padding: 0.625rem;
	  border: 0.0625rem solid #ccc;
	  border-radius: 4px;
	  font-size: 0.7rem;
	}
	
	/* 图标的样式，定位到输入框的右侧 */
	.toggle-icon {
		position: absolute;
		top: 90%;
		transform: translateY(-50%);
		cursor: pointer;
	}
	
	/* 图标的样式，定位到输入框的右侧 */
	.toggle-icon {
	  /*position: absolute;*/
	  right: 10px;
	  transform: translateY(-100%);
	  cursor: pointer;
	  color: #333;
	}
	
	/* 按钮样式 */
	.register-button {
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
	
	.register-button:hover {
	  background-color: #003bbd;
	}
	
	/* 错误提示样式 */
	.registererror {
	  color: red;
	  margin-top: 0.625rem; /* 改为rem单位 */
	  text-align: center;
	}
	
	.register-link {
	  text-align: center;
	  margin-top: 1rem; /* 改为rem单位 */
	  font-size: 0.875rem; /* 改为rem单位 */
	  text-align: center;
	  color: #6c63ff;
	}
</style>