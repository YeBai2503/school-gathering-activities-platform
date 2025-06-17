<template>
	<view class="container">
		<view class="activity-detail-page">
		  <!-- 活动信息 -->
		  <view class="activity-info-container">
			  <!-- 活动图片 -->
			  <view class="activity-image-container">
			    <img :src="activityImage"/>
			  </view>
		    <view class="activity-title">{{ activityTitle }}</view>
		    
			<view class="activity-detail">
			  <text>发布者：{{ organizerName }}</text>
			</view>
			
		    <view class="activity-detail">
		      <text>地点：{{ activityLocation || '暂无具体地点'}}</text>
		    </view>
			<view class="activity-detail">
			  <text>人数:   {{ numbers}}/{{maxnumbers}}</text>
			</view>
		    <view class="activity-detail">
		      <text>类型：{{ activityType }}</text>
		    </view>
			
		    <view class="activity-detail">
		      <text>报名要求：{{ activityRequirement || '无报名要求'}}</text>
		    </view>
		    
		    <view class="activity-detail">
		      <text>联系方式：{{ organizerContact || '暂无联系方式'}}</text>
		    </view>
			<view class="activity-detail">
					      <text>时间：{{ activityTime }}</text>
					    </view>
		  </view>
		
			<!-- 活动详细介绍 -->
			<view class="activity-description-container">
			  <view class="section-title">活动详细介绍</view>
			  <view class="activity-description">{{ activityDescription }}</view>
			</view>
			
		  <!-- 报名按钮 -->
		  <view class="register-container">
		    <input type="text" v-model="registerNote" placeholder="请输入报名简短说明..."/>
		    <button @click="handleRegister" class="register-button">报名</button>
		  </view>
		</view>
	</view>
</template>

<script>
export default {
  onLoad(options) {
    // 获取传递过来的活动ID
    this.activityId = options.id;
    console.log('接收到的活动ID:', this.activityId);

    // 调用方法获取活动详情
    this.getActivityDetail(this.activityId);
	this.getnumbers(this.activityId);
  },
  data() {
    return {
      activityImage: '', // 活动图片
      activityTitle: '', // 活动标题
      activityTime: '', // 活动时间
      activityLocation: '', // 活动地点
      activityType: '', // 活动类型
      activityRequirement: '', // 报名要求
      organizerName: '', // 发布者名字
      organizerContact: '', // 发布者联系方式
      activityDescription: '', // 活动介绍
      registerNote: '', // 报名备注
      activityId: null ,// 活动ID
	  numbers: 0,
	  maxnumbers: 0,
	  status: '',
    };
  },
  methods: {
    // 获取发布者的联系方式
    getContact(headid) {
      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/auth/showuser/' + headid, // 使用 headid 构造请求 URL
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
            const userData = res.data.data;
            that.organizerContact = userData.pnumber || '暂无联系方式';
          } else {
            uni.showToast({
              title: res.data.message || '获取发布者信息失败',
              icon: 'none'
            });
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'none'
          });
        }
      });
    },
	
	getstatus(status){
		if(status == "已结束"){
			uni.showToast({
			  title: '已结束,无法报名',
			  icon: 'none'
			});
		}
		if(status == "审核中"){
			uni.showToast({
			  title: '审核中,无法报名',
			  icon: 'none'
			});
		}
	},
	  
    // 获取活动详情
    getActivityDetail(activityId) {
      const that = this;
      uni.request({
        url: "http://192.168.1.43:8080/activities/showactivity/" + activityId,
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
            const data = res.data.data;
            // 绑定数据到页面
            that.activityImage = data.profileimageurl; 
            that.activityTitle = data.activityname;
            that.activityTime = data.timeproceed2 ? `${data.timeproceed1} 至 ${data.timeproceed2}` : data.timeproceed1; 
            that.activityLocation = data.location;
            that.activityType = data.type;
            that.activityRequirement = data.activityrequest;
            that.organizerName = data.headusername;
            that.activityDescription = data.others || '暂无详细介绍';
			that.maxnumbers = data.maxceiling;
			that.status = data.status;
			that.getstatus(data.status);
            // 使用 headid 获取发布者的联系方式
            that.getContact(data.headid);
          } else {
            uni.showToast({
              title: res.data.message || '获取活动信息失败',
              icon: 'none'
            });
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'none'
          });
        }
      });
    },
	
	getnumbers(activityid){
		const that = this;
		uni.request({
		  url: "http://192.168.1.43:8080/event/" + activityid + "/count",
		  method: 'GET',
		  header: {
		    'Content-Type': 'application/x-www-form-urlencoded'
		  },
		  data:{
			activityid: activityid
		  },
		  success(res) {
		    if (res.statusCode === 200) {
				that.numbers = res.data.data;
		    } else {
		      uni.showToast({
		        title: res.data.message || '获取活动信息失败',
		        icon: 'none'
		      });
		    }
		  },
		  fail(error) {
		    uni.showToast({
		      title: '网络错误，请稍后重试',
		      icon: 'none'
		    });
		  }
		});
	},
	
    // 处理报名
    handleRegister() {
      const userId = uni.getStorageSync('userid');
      if (!userId) {
        uni.showToast({
          title: '用户未登录，请先登录',
          icon: 'none'
        });
        return;
      }

      if (this.registerNote.trim() === '') {
        uni.showToast({
          title: '请填写报名说明',
          icon: 'none'
        });
        return;
      }

      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/event/register',
        method: 'POST',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          userid: userId,
          activityid: that.activityId,
          reason: that.registerNote
        },
        success(res) {
          if (res.statusCode === 200 && res.data.code === 200) {
            uni.showToast({
              title: '报名成功',
              icon: 'success'
            });
          } else {
            uni.showToast({
              title: res.data.message || '报名失败，请重试',
              icon: 'none'
            });
          }
        },
        fail(error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'none'
          });
        }
      });
    }
  }
};
</script>

<style scoped>
	.container{
		padding-bottom: 4rem;
		background-image: url('../../static/3.jpg');
		height: 100%;
	}
	
	.activity-detail-page {
	  padding: 15px;
	  background-color:  ;
	}

	.activity-info-container{
	  margin-top: 15px;
	background-color:  #f9f2e7;
	  padding: 15px;
	  border-radius: 8px;
	  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	}
	
   .activity-image-container {
	  width: 100%;
	  height: 15rem;
	  overflow: hidden;
	  margin-bottom: 1rem;
	  
	}

	.activity-title {
	  font-size: 20px;
	  font-weight: bold;
	  color: #333;
	  margin-bottom: 10px;
	}

	.activity-detail {
	  margin-bottom: 8px;
	  font-size: 14px;
	  color: #555;
	  
	}

	.register-container {
	  position: fixed;
	  bottom: 0.02rem;
	  left: 0;
	  right: 0;
	  display: flex;
	  align-items: center;
	  padding: 3vw;
	  background-color: transparent;
	}

	.register-container input {
	  flex: 1;
	  padding: 3vw;
	  border: 2px solid #ccc;
	  border-radius: 10vw;
	  background-color: #f9f2e7;
	}

	.register-button {
	  background-color: #fdd835;
	  border: none;
	  padding: 1vw 5vw;
	  border-radius: 10vw;
	  margin-left: 3vw;
	  cursor: pointer;
	  font-size: 4vw;
	}

	.activity-description-container {
	  margin-top: 20px;
	  background-color:  #f9f2e7;
	  padding: 15px;
	  border-radius: 8px;
	  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	}

	.section-title {
	  font-size: 18px;
	  font-weight: bold;
	  margin-bottom: 10px;
	  color: #333;
	}

	.activity-description {
	  font-size: 14px;
	  color: #555;
	}
</style>

