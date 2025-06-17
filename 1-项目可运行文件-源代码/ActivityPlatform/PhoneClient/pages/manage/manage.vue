<template>
  <view class="container">
	  <text class="head">活动管理</text>
    <!-- 活动报名反馈管理 -->
    <view class="activity-section">
      <view class="section-title">我发布的活动</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(activity, index) in displayedActivityApplications" :key="index"  
		>
          <view class="activity-info">
            <view class="info-row">
              <text class="header">活动名称：</text>
              <text>{{ activity.activityname || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">活动ID：</text>
              <text>{{ activity.activityid || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">开始时间：</text>
              <text>{{ activity.timeproceed1 || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">结束时间：</text>
              <text>{{ activity.timeproceed2 || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">地点：</text>
              <text>{{ activity.location || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">负责人：</text>
              <text>{{ activity.headusername || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">状态：</text>
              <text>{{ activity.status || '无' }}</text>
            </view>
            <view class="info-row">
              <button class="submit-btn" @click="confirmDeleteActivity(activity.activityid)">删除活动</button>
            </view>
          </view>
        </view>
      </view>
      <button class="submit-btn" v-if="activityApplications.length > 2" @click="toggleActivityApplications">
        {{ showAllActivityApplications ? '收起' : '点击查看全部' }}
      </button>
    </view>

    <!-- 我的活动列表 -->
    <view class="activity-section">
      <view class="section-title">我参加的活动</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(activity, index) in displayedMyActivities" :key="index"
		>
          <view class="activity-info">
            <view class="info-row">
              <text class="header">活动名称：</text>
              <text>{{ activity.activityname || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">活动ID：</text>
              <text>{{ activity.activityid || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">开始时间：</text>
              <text>{{ activity.timeproceed1 || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">结束时间：</text>
              <text>{{ activity.timeproceed2 || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">地点：</text>
              <text>{{ activity.location || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">负责人：</text>
              <text>{{ activity.headusername || '无' }}</text>
            </view>
            <!-- 退出活动按钮 -->
            <view class="info-row">
              <button class="submit-btn" @click="confirmLeaveActivity(activity.activityid)">退出活动</button>
            </view>
          </view>
        </view>
      </view>
      <button class="submit-btn" v-if="myActivities.length > 2" @click="toggleMyActivities">
        {{ showAllMyActivities ? '收起' : '点击查看全部' }}
      </button>
    </view>

    <!-- 结束活动列表 -->
    <view class="activity-section">
      <view class="section-title">参加过的活动</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(activity, index) in displayedCompletedActivities" :key="index"
		>
          <view class="activity-info">
            <view class="info-row">
              <text class="header">活动名称：</text>
              <text>{{ activity.activityname || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">活动ID：</text>
              <text>{{ activity.activityid || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">发布人：</text>
              <text>{{ activity.headusername || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">开始时间：</text>
              <text>{{ activity.timeproceed1 || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">结束时间：</text>
              <text>{{ activity.timeproceed2 || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">评分：</text>
              <input type="text" v-model="activity.score" placeholder="0-100" class="score-input" :disabled="activity.hasRated"/>
            </view>
            <view class="info-row">
              <button class="submit-btn" @click="submitScore(activity)" :disabled="activity.hasRated">提交</button>
            </view>
          </view>
        </view>
      </view>
      <button class="submit-btn" v-if="completedActivities.length > 2" @click="toggleCompletedActivities">
        {{ showAllCompletedActivities ? '收起' : '点击查看全部' }}
      </button>
    </view>

    <!-- 发布活动按钮 -->
    <button class="publish-activity-btn" @click="publishActivity">
      <text class="plus-icon">+</text> 发布活动
    </button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userid: uni.getStorageSync('userid'), // 从存储中获取用户ID
      activityApplications: [], // 存放活动报名反馈
      myActivities: [], // 存放用户发布的活动
      completedActivities: [], // 存放用户完成的活动
      showAllActivityApplications: false, // 控制活动报名反馈的显示
      showAllMyActivities: false, // 控制我的活动列表的显示
      showAllCompletedActivities: false, // 控制结束活动列表的显示
    };
  },
  computed: {
    displayedActivityApplications() {
      return this.showAllActivityApplications ? this.activityApplications : this.activityApplications.slice(0, 2);
    },
    displayedMyActivities() {
      return this.showAllMyActivities ? this.myActivities : this.myActivities.slice(0, 2);
    },
    displayedCompletedActivities() {
      return this.showAllCompletedActivities ? this.completedActivities : this.completedActivities.slice(0, 2);
    }
  },
  onLoad() {
    const userId = uni.getStorageSync('userid');
    if (userId) {
      this.getMyPublishedActivities();
      this.getMyParticipatedActivities();
      this.getMyCompletedActivities();
    } else {
      console.log('用户ID未找到');
    }
  },
  methods: {
    toggleActivityApplications() {
      this.showAllActivityApplications = !this.showAllActivityApplications;
    },
    toggleMyActivities() {
      this.showAllMyActivities = !this.showAllMyActivities;
    },
    toggleCompletedActivities() {
      this.showAllCompletedActivities = !this.showAllCompletedActivities;
    },
    async getMyPublishedActivities() {
      const userId = this.userid;
      try {
        const publishedActivitiesRes = await uni.request({
          url: `http://192.168.1.43:8080/activities/${userId}/activities`,
          method: 'GET',
          header: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        });

        if (publishedActivitiesRes.statusCode === 200) {
          const activities = publishedActivitiesRes.data.data || [];
          const activityIds = activities.map(activity => activity.activityid);

          const fetchParticipantsPromises = activityIds.map(async (activityId) => {
            const participantRes = await uni.request({
              url: 'http://192.168.1.43:8080/event/users',
              method: 'GET',
              data: { activityId },
              header: {
                'Content-Type': 'application/x-www-form-urlencoded'
              }
            });

            const countRes = await uni.request({
              url: `http://192.168.1.43:8080/event/${activityId}/count`,
              method: 'GET',
              header: {
                'Content-Type': 'application/x-www-form-urlencoded'
              }
            });

            const participantCount = countRes.statusCode === 200 ? countRes.data.data : 0;

            const maxParticipantsRes = await uni.request({
              url: 'http://192.168.1.43:8080/activities/max-ceiling',
              method: 'GET',
              header: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              data: {
                activityId
              }
            });

            const maxParticipants = maxParticipantsRes.statusCode === 200 ? maxParticipantsRes.data.data : 0;

            const activityData = activities.find(activity => activity.activityid === activityId) || {};

            return {
              ...activityData,
              participants: participantRes.data.data || [],
              participantCount,
              maxParticipants
            };
          });

          const activityParticipationDetails = await Promise.all(fetchParticipantsPromises);

          this.activityApplications = activityParticipationDetails.map(activity => {
            const participantInfo = activity.participants.length > 0 ? activity.participants[0] : {};
            return {
              activityid: activity.activityid,
              activityname: activity.activityname,
              headusername: activity.headusername,
              timeproceed1: activity.timeproceed1,
              timeproceed2: activity.timeproceed2,
              location: activity.location,
              username: participantInfo.username,
              reason: participantInfo.reason,
              participantCount: activity.participantCount,
              maxParticipants: activity.maxParticipants,
              status: activity.status,
            };
          });
        } else {
          console.error('获取活动失败', publishedActivitiesRes.data.message);
        }
      } catch (error) {
        console.error('请求出现错误', error);
      }
    },
    getMyParticipatedActivities() {
      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/activities/user/activities',
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          userId: this.userid
        },
        success(res) {
          if (res.statusCode === 200) {
            that.myActivities = res.data;
          } else {
            that.loginError = res.data.message || '获取信息错误';
          }
        },
        fail(error) {
          that.loginError = '网络错误，请稍后重试';
        }
      });
    },
    getMyCompletedActivities() {
      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/activities/user/finished-activities',
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          userId: this.userid
        },
        success(res) {
          if (res.statusCode === 200) {
            that.completedActivities = res.data;
            that.completedActivities.forEach(activity => {
              activity.hasRated = false;
            });
          } else {
            that.loginError = res.data.message || '获取信息错误';
          }
        },
        fail(error) {
          that.loginError = '网络错误，请稍后重试';
        }
      });
    },
    submitScore(activity) {
      const that = this;
      const userId = this.userid;
      const activityId = activity.activityid;
      const score = activity.score;

      if (score === undefined || isNaN(score) || score < 0 || score > 100) {
        uni.showToast({
          title: '请输入有效的评分（0-100）',
          icon: 'none'
        });
        return;
      }

      uni.request({
        url: `http://192.168.1.43:8080/event/${userId}/activities/${activityId}/rate`,
        method: 'POST',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          userid: userId,
          activityid: activityId,
          score: score
        },
        success(res) {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '评分成功',
              icon: 'success'
            });
            activity.hasRated = true;
            this.getMyCompletedActivities();
          } else {
            uni.showToast({
              title: res.data.message || '评分失败',
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
    confirmDeleteActivity(activityId) {
      const userId = this.userid;
      uni.showModal({
        title: '确认删除',
        content: '您确定要删除该活动吗？删除后无法恢复。',
        success: (res) => {
          if (res.confirm) {
            this.deleteActivity(activityId);
          } else if (res.cancel) {
            console.log('用户取消删除操作');
          }
        }
      });
    },
    deleteActivity(activityId) {
      const userId = this.userid;
      uni.request({
        url: `http://192.168.1.43:8080/activities/${userId}/activities/${activityId}`,
        method: 'DELETE',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: (res) => {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '活动删除成功',
              icon: 'success'
            });
            this.getMyPublishedActivities();
          } else {
            uni.showToast({
              title: res.data.message || '删除活动失败',
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
      });
    },
    // 新增确认退出活动的方法
    confirmLeaveActivity(activityId) {
      const userId = this.userid;
      uni.showModal({
        title: '确认退出',
        content: '您确定要退出该活动吗？',
        success: (res) => {
          if (res.confirm) {
            this.leaveActivity(activityId); // 执行退出活动
          } else {
            console.log('用户取消退出活动');
          }
        }
      });
    },
	  publishActivity() {
	      // 跳转到活动发布页面
	      uni.navigateTo({
	        url: '../activity-release/activity-release'
	      });
		  },
		  //跳转到
	goToPage(activityId) {
	    uni.navigateTo({
	      url: "../activitydetail/activitydetail?id=" + activityId,
	    });
	  },
	  
		  
    // 新增退出活动的方法
    leaveActivity(activityId) {
      const userId = this.userid;
      uni.request({
        url: `http://192.168.1.43:8080/event/${userId}/events/${activityId}`,
        method: 'DELETE',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: (res) => {
          if (res.statusCode === 200) {
            uni.showToast({
              title: '退出活动成功',
              icon: 'success'
            });
            this.getMyParticipatedActivities(); // 更新参与活动列表
          } else {
            uni.showToast({
              title: res.data.message || '退出活动',
			      title: res.data.message || '退出活动失败',
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
			        });
			      },
			     
			    }
			  };
			  </script>
<style scoped>
.container {
  max-width: 100%; /* 最大宽度为 100% */
  height: 100%; /* 高度为 100% */
  margin: 0 auto; /* 水平居中 */
  padding: 1rem; /* 内边距为 1rem */
  background-color: #fff3e0; /* 背景颜色 */
  border-radius: 0px; /* 保留绝对单位用于圆角 */
  box-shadow: 0 0.625rem 0.625rem rgba(0, 0, 0, 0.1); /* 阴影 */
  line-height: 1.875rem; /* 行高 */
  background-image: url('../../static/3.jpg');
}

.head {
  font-size: 1.5rem; /* 字体大小 */
  color: #333; /* 文字颜色 */
  font-weight: bold; /* 加粗 */
  display: grid; /* 使用网格布局 */
  place-items: center; /* 同时水平和垂直居中 */
}

.activity-section {
  margin-bottom: 5vw; /* 底部外边距为视口宽度的 5% */
}

.section-title {
  font-size: 1rem; /* 字体大小 */
  font-weight: bold; /* 加粗 */
  margin-bottom: 3vw; /* 底部外边距为视口宽度的 3% */
}

.activity-list {
  display: flex; /* 使用 Flexbox 布局 */
  flex-direction: column; /* 垂直排列元素 */
  gap: 3vw; /* 子元素间距为视口宽度的 3% */
}

.activity-item {
  background-color:  #f9f2e7;
  padding: 4vw; /* 内边距为视口宽度的 4% */
  border-radius: 10px; /* 保留绝对单位用于圆角 */
  box-shadow: 0 0.8vw 1.6vw rgba(0, 0, 0, 0.1); /* 阴影 */
  border: 0.3vw solid #ddd; /* 边框 */
}

.activity-info {
  display: flex; /* 使用 Flexbox 布局 */
  background-color:  #f9f2e7;
  flex-direction: column; /* 垂直排列元素 */
  gap: 3vw; /* 子元素间距为视口宽度的 3% */
}

.info-row {
  font-size: 3vw; /* 字体大小为视口宽度的 3% */
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: space-between; /* 主轴两端对齐 */
}

.header {
  font-weight: bold; /* 加粗 */
  font-size: 4vw; /* 字体大小为视口宽度的 4% */
}

.score-input {
  font-size: 3vw; /* 字体大小为视口宽度的 3% */
  width: 20vw; /* 宽度为视口宽度的 20% */
  padding: 0.8vw; /* 内边距为视口宽度的 0.8% */
  border: 0.3vw solid #ccc; /* 边框 */
  border-radius: 4px; /* 保留绝对单位用于圆角 */
}

.submit-btn {
  font-size: 1rem; /* 字体大小 */
  
}

.publish-activity-btn {
  font-size: 2vw; /* 字体大小为视口宽度的 2% */
  position: fixed; /* 固定定位 */
  bottom: 16vw; /* 距离视口底部 18vw */
  right: 2vw; /* 距离视口右侧 4vw */
  background-color: #ffd700; /* 背景颜色 */
  padding: 0.3vw 2vw; /* 内边距：上下 0.4vw，左右 5vw */
  border-radius: 50px; /* 保留绝对单位用于圆角 */
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直中心对齐 */
  justify-content: center; /* 水平中心对齐 */
  box-shadow: 0 1.2vw 1.8vw rgba(0, 0, 0, 0.1); /* 阴影 */
}

.plus-icon {
  font-size: 6vw; /* 字体大小为视口宽度的 6% */
  margin-right: 3vw; /* 右外边距为视口宽度的 3% */
}

</style>


