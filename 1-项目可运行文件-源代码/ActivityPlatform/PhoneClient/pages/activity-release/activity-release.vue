<template>
	<view class="container">
	  <!-- 标题 -->
	  <view class="title">
		<text>发布活动</text>
	  </view>
	
	  <!-- 活动封面 -->
	  <view class="form-item">
		<label class="label">活动封面：</label>
		<input type="text" placeholder="请输入活动图片链接地址" v-model="imageurl" />
	  </view>
	
	  <!-- 活动名称 -->
	  <view class="form-item">
		<label class="label">活动名称：</label>
		<input type="text" placeholder="请输入活动名称" v-model="activityName" />
	  </view>
	
	  <!-- 发布人 -->
	  <view class="form-item">
		<label class="label">发布人：</label>
		<input type="text" placeholder='${publisher}' v-model="publisher" />
	  </view>
	
	  <!-- 开始时间 -->
	  <view class="form-item">
		<label class="label">开始时间：</label>
		<input type="datetime-local" v-model="startTime" />
	  </view>
	
	  <!-- 结束时间 -->
	  <view class="form-item">
		<label class="label">结束时间：</label>
		<input type="datetime-local" v-model="endTime" />
	  </view>
	
	  <!-- 人数 -->
	  <view class="form-item">
		<label class="label">人数：</label>
		<input type="number" placeholder="请输入人数" v-model="peopleLimit" />
	  </view>
	
	  <!-- 地点 -->
	  <view class="form-item">
		<label class="label">地点：</label>
		<input type="text" placeholder="请输入地点" v-model="location" />
	  </view>
	
	  <!-- 类型 -->
	  <view class="form-item">
		<label class="label">类型：</label>
		  <picker 
			mode="selector" 
			:range="activityTypes" 
			:value="activityTypes.indexOf(selectedActivityType)" 
			@change="onActivityTypeChange">
			<view class="picker">
			  <text>选择活动类型：</text>
			  <text>{{ selectedActivityType }}</text>
			</view>
		  </picker>
	  </view>
	
	  <!-- 报名要求 -->
	  <view class="form-item">
		<label class="label">报名要求：</label>
		<input type="text" placeholder="请输入报名要求" v-model="signupRequirement" />
	  </view>
	
	  <!-- 联系方式 -->
	  <view class="form-item">
		<label class="label">联系方式：</label>
		<input type="text" placeholder="请输入联系方式" v-model="contactInfo" />
	  </view>
	
	  <!-- 活动说明 -->
	  <view class="form-item">
		<label class="label">活动说明：</label>
		<textarea placeholder="请输入活动说明" v-model="description"></textarea>
	  </view>
	
	  <!-- 发布按钮 -->
	  <button class="submit-btn" @click="submitActivity">发布活动</button>
	</view>
</template>

<script>
export default {
  onLoad() {
    // 获取本地存储的用户ID
    const userId = uni.getStorageSync('userid'); 
	const userName = uni.getStorageSync('username');
    if (userId) {
		console.log(userName);
    } else {
  	  console.log('用户ID未找到');
    }
  },
  data() {
    return {
	  imageurl:'',
      activityName: '',
      publisher: uni.getStorageSync('username'),
      startTime: '',
      endTime: '',
      peopleLimit: 0,
      location: '',
      activityTypes: ['体育', '学术', '艺术', '桌游', '出行', '手游', '端游', '其他'],
      selectedActivityType: '体育',
      signupRequirement: '',
      contactInfo: '',
      description: '',
    };
  },
  methods: {
	onActivityTypeChange(event) {
      const index = event.detail.value;  // 获取用户选择的类型索引
      this.selectedActivityType = this.activityTypes[index];
	},
    submitActivity() {
      if (!this.activityName || !this.publisher) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        });
        return;
      }

      // 创建请求数据对象
      const requestData = {
        activityname: this.activityName,
        maxceiling: this.peopleLimit,
        type: this.selectedActivityType,
        status: "",  // 可能需要根据实际业务逻辑设置
        timesubmit: new Date().toISOString(),  // 设置提交时间
        timepass: "",  // 留空，等待后端处理
        timesignup1: "",  // 留空
        timesignup2: "",  // 留空
        timeproceed1: this.startTime ? `${this.startTime}Z` : "",  // 转换为标准ISO格式
        timeproceed2: this.endTime ? `${this.endTime}Z` : "",  // 转换为标准ISO格式
        score: 0,  // 默认分数
        headid: uni.getStorageSync('userid'),  // 用户 ID，可以根据实际情况设置
        profileimageurl: this.imageurl,
        location: this.location,
        headusername: this.publisher,
        activityrequest: this.signupRequirement,
        others: this.description
      };

      uni.request({
        url: 'http://192.168.1.43:8080/activities/request',
        method: 'POST',
        header:{
          'Content-Type': 'application/json'
        },
        data: requestData,
        success: (res) => {
          if (res.statusCode === 200) {
            console.log('活动发布成功');
            uni.showToast({
              title: '发布成功',
              icon: 'success'
            });
          } else {
			console.log(res.statusCode);
            uni.showToast({
              title: res.data.message || '发布失败',
              icon: 'none'
            });
          }
        },
        fail: (error) => {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'none'
          });
        }
      })
    }
  }
};
</script>

<style scoped>
.container {
  background-color: #fff3e0;
  padding: 5vw;
}

.title {
  text-align: center;
  font-size: 6vw;
  font-weight: bold;
  margin-bottom: 5vw;
}

.label {
	font-size: 1rem;
	font-weight: bold;
}

.form-item {
  width: 92% ;
  margin-bottom: 4vw;
}

label {
  display: block;
  font-size: 4vw;
  margin-bottom: 2vw;
}

input, textarea {
  width: 100%;
  padding: 3vw;
  font-size: 4vw;
  border: 1.5px solid #ccc;
  border-radius: 5px;
  font-size: 0.9rem;
}

textarea {
  min-height: 20vw;
  resize: none;
}

.upload-btn {
  display: flex;
  align-items: center;
}

.upload-input {
  font-size: 4vw; 
}

.picker {
  color: ;
  padding: 10px;
  background-color: transparent;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.submit-btn {
  width: 100%;
  padding: 4vw;
  background-color: #ffd700;
  text-align: center;
  border-radius: 5px;
  font-size: 4vw;
}
</style>

