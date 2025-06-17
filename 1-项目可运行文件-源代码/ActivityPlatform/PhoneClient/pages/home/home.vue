<template>
  <view class="container">
    <!-- 顶部标题栏 -->

    <view class="header">
      <text>校园活动，你我共建</text>
    </view>
   

    <!-- 轮播图区域 -->
    <view>
      <page-head title="swiper,可滑动视图"></page-head>
      <view class="uni-margin-wrap">
        <swiper class="swiper" circular :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval" :duration="duration">
          <swiper-item class="swiper-item">
            <img :src="swiperpic1" alt="热门活动图片" />
          </swiper-item>
          <swiper-item class="swiper-item">
            <img :src="swiperpic2" alt="热门活动图片" />
          </swiper-item>
          <swiper-item class="swiper-item">
            <img :src="swiperpic3" alt="热门活动图片" />
          </swiper-item>
        </swiper>
      </view>
    </view>

    <!-- 重要通知 -->
    <view class="user-info">
      
      <view class="user-details">
        <h3>重要通知</h3>
        <p><text class="point">·</text>网页将在今晚十一点开始维护，维护时间至明日早上七点。带来的不便敬请谅解</p>
        <p><text class="point">·</text>请同学们注意文明用语，近日网站将会对不文明用语进行监管，严重可能导致封号。</p>
        
      </view>
      
    </view>

	<!-- 近日活动 -->
	<view class="recent-activities">
	  <view class="activity-header">
		<text class="title">近日活动</text>
		
	  </view>
	  <view class="activity-list">
		<view class="activity-item" @click="navigatetoActivityDetail(recent1.id)">
		  <div class="activity-img">
			<img :src="recent1.pic" alt="近日活动1" class="activity-image"/>
		  </div>
		  
		  <div class="activity-details">
			<text class="activity-date">{{recent1.time}}</text>
			<text class="activity-name">{{recent1.name}}</text>
		  </div>
		</view>
		<view class="activity-item" @click="navigatetoActivityDetail(recent2.id)">
		  <div class="activity-img">
			<img :src="recent2.pic" alt="近日活动2"/>
		  </div>
		  <div class="activity-details">
			<text class="activity-date">{{recent2.time}}</text>
			<text class="activity-name">{{recent2.name}}</text>
		  </div>
		</view>
	  </view>
	</view>

	<!-- 热门活动 -->
	<view class="hot-activities">
	  <view class="activity-header">
		<text class="title">热门活动</text>
		
	  </view>
	  <view class="activity-list">
		<view class="hot-activity-item" @click="navigatetoActivityDetail(hot1.id)">
		  <div class="activity-img">
			<img :src="hot1.pic" alt="热门活动1"/>
		  </div>
		  <div class="activity-details">
			<text class="hot-name">{{hot1.name}}</text>
			<text class="activity-date">{{hot1.time}}</text>
			<text class="activity-location">{{hot1.location}}</text>
		  </div>
		</view>
		<view class="hot-activity-item" @click="navigatetoActivityDetail(hot2.id)">
		  <div class="activity-img">
			<img :src="hot2.pic" alt="热门活动2"/>
		  </div>
		  <div class="activity-details">
			<text class="hot-name">{{hot2.name}}</text>
			<text class="activity-date">{{hot2.time}}</text>
			<text class="activity-location">{{hot2.location}}</text>
		  </div>
		</view>
		<view class="hot-activity-item" @click="navigatetoActivityDetail(hot3.id)">
		  <div class="activity-img">
			<img :src="hot3.pic" alt="热门活动3"/>
		  </div>
		  <div class="activity-details">
			<text class="hot-name">{{hot3.name}}</text>
			<text class="activity-date">{{hot3.time}}</text>
			<text class="activity-location">{{hot3.location}}</text>
		  </div>
		</view>
	  </view>
	</view>
  </view>
</template>

<script>
export default {
  onLoad() {
    const userId = uni.getStorageSync('userid'); 
	this.getOngoingActivities();
	this.getOnHotActivities();
    if (userId) {
      this.userId = userId;
      this.getUserInfo();
    } else {
      console.log('用户ID未找到');
    }
  },
  data() {
    return {
	  userid: uni.getStorageSync('userid'),
	  username: '',
	  recent1:{
		id: 0,
		name: '',
		time: '',
		pic: ''
	  },
	  recent2:{
		id: 0,
	    name: '',
	    time: '',
	    pic: ''
	  },
	  hot1:{
		id: 0,
		name: '',
		time: '',
		pic: '',
		location: '',
	  },
	  hot2:{
		id: 0,
		name: '',
		time: '',
		pic: '',
		location: '',
	  },
	  hot3:{
		id:0,
		name: '',
		time: '',
		pic: '',
		location: '',
	  },
      swiperpic1: '/static/wallhaven-yx6dyk.jpg',
      swiperpic2: '/static/wallhaven-0wg61x.png',
      swiperpic3: '/static/wallhaven-9mxpx1.png',
      avatar: '/static/logo1.png',
      indicatorDots: true,
      autoplay: true,
      interval: 2000,
      duration: 500,
      showBackToTop: false,
    };
  },
  methods: {
	getUserInfo(){
		const that = this;
		uni.request({
			url: 'http://192.168.1.43:8080/auth/showuser/' + this.userid,
			method: 'GET',
			header:{
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			data:{
				id: this.userid
			},
			success(res) {
			  if (res.statusCode === 200) {
				 console.log('成功');
				 that.username = res.data.data.username;
			  } else {
			     this.loginError = res.data.message || '获取信息错误';
			  }
			},
			fail(error) {
			  this.loginError = '网络错误，请稍后重试';
			}
		})
	},
	
	getOngoingActivities(){
		const that = this;
		uni.request({
			url: 'http://192.168.1.43:8080/activities/ongoingRegistrations',
			method: 'GET',
			header:{
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			success(res) {
			  if (res.statusCode === 200) {
				 that.recent1.name = res.data.data[0].activityname;
				 that.recent1.time = res.data.data[0].timeproceed1;
				 that.recent1.pic = res.data.data[0].profileimageurl;
				 that.recent1.id = res.data.data[0].activityid;
				 that.recent2.name = res.data.data[1].activityname;
				 that.recent2.time = res.data.data[1].timeproceed1;
				 that.recent2.pic = res.data.data[1].profileimageurl;
				 that.recent2.id = res.data.data[1].activityid;
			  } else {
			     this.loginError = res.data.message || '获取信息错误';
			  }
			},
			fail(error) {
			  this.loginError = '网络错误，请稍后重试';
			}
		})
	},
	
	getOnHotActivities(){
		const that = this;
		uni.request({
			url: 'http://192.168.1.43:8080/activities/top-three',
			method: 'GET',
			header:{
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			success(res) {
			  if (res.statusCode === 200) {
				 that.hot1.id = res.data[0].activityid;
				 that.hot1.name = res.data[0].activityname;
				 that.hot1.time = res.data[0].timeproceed1;
				 that.hot1.pic = res.data[0].profileimageurl;
				 that.hot1.location = res.data[0].location;
				 that.hot2.id = res.data[1].activityid;
				 that.hot2.name = res.data[1].activityname;
				 that.hot2.time = res.data[1].timeproceed1;
				 that.hot2.pic = res.data[1].profileimageurl;
				 that.hot2.location = res.data[1].location;
				 that.hot3.id = res.data[2].activityid;
				 that.hot3.name = res.data[2].activityname;
				 that.hot3.time = res.data[2].timeproceed1;
				 that.hot3.pic = res.data[2].profileimageurl;
				 that.hot3.location = res.data[2].location;
			  } else {
			     this.loginError = res.data.message || '获取信息错误';
			  }
			},
			fail(error) {
			  this.loginError = '网络错误，请稍后重试';
			}
		})
	},
	
    switchTabToPersonalPage() {
      uni.switchTab({
        url: '../personalpage/personalpage',
      });
    },
	
    switchTabToActivityPage() {
      uni.switchTab({
        url: '../activity/activity',
      });
    },
	
    navigatetoActivityDetail(id) {
      uni.navigateTo({
        url: "../activitydetail/activitydetail?id=" + id // 跳转活动细节
      });
    },
    handleScroll() {
      if (window.scrollY > 100) {
        this.showBackToTop = true;
      } else {
        this.showBackToTop = false;
      }
    },
    scrollToTop() {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
  },
  mounted() {
    this.fetchActivities();
    window.addEventListener('scroll', this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll);
  },
};
</script>

<style scoped>
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  html {
    font-size: 16px;
  }

  body {
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
    color: #333;
    line-height: 1.5;
  }

  .container {
    max-width: 100%;
    margin: 0 auto;
    padding: 1rem;
    background-color: #fff3e0;
    border-radius: 0.5rem;
    box-shadow: 0 0.625rem 0.625rem rgba(0, 0, 0, 0.1);
	background-image: url('../../static/3.jpg');
  }

  .header {
    font-weight: bold;
	display: grid;
	 place-items: center; /* 同时水平和垂直居中 */
  }

  .header text {
    font-size: 1.5rem;
    color: #333;
  }

  .user-info {
	  
	border: 1px solid #ccc;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem;
    background-color:  #f9f2e7;
    border-radius: 0.5rem;
	box-shadow: 0 0.8vw 1.6vw rgba(0, 0, 0, 0.1);
	
  }

  .avatar {
    width: 3.5rem;
    height: 3.5rem;
    border-radius: 50%;
    margin-right: 0.625rem;
  }

  .user-details {
     display: grid;
	 place-items: center; /* 同时水平和垂直居中 */
	 font-size: 0.9rem;
  }

  .username {
    font-weight: bold;
    margin-bottom: 0.1rem;
    color: #333;
	font-size: 1.2rem;
  }

  .viewpersonalpage-btn {
	border: 0.5px solid #ccc;
    background-color: #faea3f;
    padding: 0.5rem 0.75rem;
    border-radius: 2rem;
    font-size: 0.875rem;
  }

  .recent-activities,
  .hot-activities {
	  
    margin: 1.25rem 0;
  }

  .activity-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .activity-header .title {
    font-size: 1.3rem;
    font-weight: bold;
    color: #333;
  }

  .more-link {
	margin-top: 1vw;
    font-weight: 400;
    font-size: 1rem;
    cursor: pointer;
  }

  .activity-item .activity-img img {
	  width: 100%;
	  height: 7rem;
	  overflow: hidden;
	  margin-bottom: 0.3125rem;
	  display: flex;
  }
  
   .hot-activity-item .activity-img img{
	   width: 100%;
	   height: 12.5rem;
	   overflow: hidden;
	   margin-bottom: 0.3125rem;
	   display: flex;
	   
   }
  


  .activity-item .activity-details,
  .hot-activity-item .activity-details {
    display: flex;
    flex-direction: column;
    margin-top: 0.5rem;
	background-color:  #f9f2e7;
  }

  .activity-date,
  .activity-location {
	text-align: center;
    color: #777;
    font-size: 0.85rem;
    margin-top: 0.3125rem;
  }
  
  .activity-list {
    display: flex;
    flex-wrap: wrap; /* 允许换行 */
    justify-content: space-between; /* 在主轴方向上分配空白 */
	
  }
  
  .activity-item {
    border: 1px solid #ccc;
    margin-top: 0.625rem;
  background-color:  #f9f2e7;
    padding: 1rem;
    border-radius: 8px;
    width: calc(50% - 0.625rem); /* 每个活动占据 50% 的宽度减去边距 */
    box-shadow: 0 0.8vw 1.6vw rgba(0, 0, 0, 0.1);
  }
  
  .activity-item image{
	  width: 100%;
	  height: 7rem;
	  display: block;
	  border-radius: 8px;
  }

  
  .hot-activity-item {
    border: 1px solid #ccc;
    margin-top: 0.625rem;
   background-color:  #f9f2e7;
    padding: 1rem;
    border-radius: 8px;
    width: 100%; /* 每个热门活动占据整行宽度 */
    box-shadow: 0 0.8vw 1.6vw rgba(0, 0, 0, 0.1);
  }
  
  .hot-activity-item image{
  	  width: 100%;
  	  height: 20rem;
  	  display: block;
  	  border-radius: 8px;
  }
  
  .activity-name {
	  font-size: 1.1rem;
	  text-align: center;
	  font-weight: bold;
  }
  
  .hot-name{
	  font-weight: bold;
	  text-align: center;
	  font-size: 1.8rem;
  }
  
  .uni-margin-wrap {
	  height: 20rem;
    width: 100vh;
  }

  .swiper {
    height: 20rem;
	width: 20rem;
	
  }

  .back-to-top {
    position: fixed;
    bottom: 20vw;
    right: 20vw;
    background-color: #4caf50;
    color: white;
    border: none;
    padding: 20vw 20vw;
    font-size: 16px;
    border-radius: 50%;
    cursor: pointer;
    z-index: 100;
    transition: background-color 0.3s;
  }
  .point{
	font-weight: 1000;  
  }
  
  .back-to-top:hover {
    background-color: #45a049;
  }
  
  .swiper-item{
	 width: 100%;
	 height: 15rem;
	 overflow: hidden;
	 margin-bottom: 0.3125rem;
  }
  
  .swiper-item img{
	  margin-top: 1rem;
	  margin-left: 1rem;
	  width: 100%;
	  height: 80%;
	  object-fit: cover;
	  border-radius: 8px;
	  display: block;
  }

</style>









