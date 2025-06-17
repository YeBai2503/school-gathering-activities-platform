<template>
  <view class="container">
    <!-- 活动申请管理 -->
    <view class="activity-section">
      <view class="section-title">活动报名反馈</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(activity, index) in activityApplications" :key="index">
          <view class="activity-info" v-for="(id, index) in userRegistrations" :key="index">
            <view class="info-row">
              <text class="header">活动名称：</text>
              <text>{{ id.activityname || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">活动ID：</text>
              <text>{{ id.activityid || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">报名人：</text>
              <text>{{ id.username || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">备注：</text>
              <text>{{ id.reason || '无' }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 我的活动列表 -->
    <view class="activity-section">
      <view class="section-title">我的活动列表</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(activity, index) in myActivities" :key="index">
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
              <text class="header">负责人</text>
              <text>{{ activity.headusername || '无' }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 结束活动列表 -->
    <view class="activity-section">
      <view class="section-title">结束活动列表</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(activity, index) in completedActivities" :key="index">
          <view class="activity-info">
            <view class="info-row">
              <text class="header">活动名称：</text>
              <text>{{ activity.name || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">活动ID：</text>
              <text>{{ activity.id || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">发布人：</text>
              <text>{{ activity.organizer || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">开始时间：</text>
              <text>{{ activity.startTime || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">结束时间：</text>
              <text>{{ activity.endTime || '无' }}</text>
            </view>
            <view class="info-row">
              <text class="header">评分：</text>
              <input type="text" v-model="activity.score" placeholder="0-100" class="score-input" />
            </view>
            <view class="info-row">
              <button class="submit-btn" @click="submitScore(activity, index)">提交</button>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 发布活动按钮 -->
    <button class="publish-activity-btn" @click="publishActivity">
      <text class="plus-icon">+</text> 发布活动
    </button>
  </view>
</template>

<script>
export default {
  // 页面加载时调用
  onLoad() {
    const userId = uni.getStorageSync('userid'); 
    if (userId) {
      this.getMyPublishedActivities();
      this.getMyParticipatedActivities();
      this.getMyCompletedActivities(); // 确保这个方法被调用
    } else {
      console.log('用户ID未找到');
    }
  },
  data() {
    return {
      userid: uni.getStorageSync('userid'),
      activityApplications: [],
      myActivities: [],
      completedActivities: [],
      userRegistrations: {}
    };
  },
  methods: {
    getMyPublishedActivities() {
      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/activities/' + this.userid + '/activities',
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
            that.activityApplications = res.data.data;
          } else {
            this.loginError = res.data.message || '获取信息错误';
          }
        },
        fail(error) {
          this.loginError = '网络错误，请稍后重试';
        }
      });
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
            this.loginError = res.data.message || '获取信息错误';
          }
        },
        fail(error) {
          this.loginError = '网络错误，请稍后重试';
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
            that.completedActivities = res.data; // 将返回的数据赋给 completedActivities
          } else {
            this.loginError = res.data.message || '获取信息错误';
          }
        },
        fail(error) {
          this.loginError = '网络错误，请稍后重试';
        }
      });
    },

    submitScore(activity, index) {
      if (activity && activity.score) {
        console.log('提交评分：', activity.id, activity.score);
        uni.showToast({
          title: '评分成功',
          icon: 'success'
        });
        // 删除已评分的活动块
        this.completedActivities.splice(index, 1);
      } else {
        uni.showToast({
          title: '请输入评分',
          icon: 'none'
        });
      }
    },
    
    publishActivity() {
      // 跳转到活动发布页面
      uni.navigateTo({
        url: '../activity-release/activity-release'
      });
    }
  }
};
</script>