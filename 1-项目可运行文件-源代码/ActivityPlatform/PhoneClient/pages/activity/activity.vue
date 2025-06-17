<template>
  <view class="container">
    <!-- 顶部标题栏 -->
    <view class="header">
      <text>活动广场</text>
    </view>

    <!-- 搜索框 -->
    <div class="search-area">
      <input type="text" placeholder="搜索活动..." class="search-input" v-model="searchQuery" />
      <button class="search-button" @click="fetchActivities">搜索</button>
    </div>

    <!-- 分类栏 -->
    <view class="classification">
      <h3>分类</h3>
      <span class="activity-item">
        <button :class="{active: selectedCategory === '全部'}" @click="updateCategory('全部')">全部</button>
        <button :class="{active: selectedCategory === '体育'}" @click="updateCategory('体育')">体育</button>
        <button :class="{active: selectedCategory === '学术'}" @click="updateCategory('学术')">学术</button>
        <button :class="{active: selectedCategory === '艺术'}" @click="updateCategory('艺术')">艺术</button>
        <button :class="{active: selectedCategory === '桌游'}" @click="updateCategory('桌游')">桌游</button><br />
        <button :class="{active: selectedCategory === '出行'}" @click="updateCategory('出行')">出行</button>
        <button :class="{active: selectedCategory === '手游'}" @click="updateCategory('手游')">手游</button>
        <button :class="{active: selectedCategory === '端游'}" @click="updateCategory('端游')">端游</button>
        <button :class="{active: selectedCategory === '其他'}" @click="updateCategory('其他')">其他</button>
      </span>

      <h3>状态</h3>
      <span class="activity-item">
        <button :class="{active: selectedStatus === '全部'}" @click="updateStatus('全部')">全部</button>
        <button :class="{active: selectedStatus === '审核中'}" @click="updateStatus('审核中')">审核中</button>
        <button :class="{active: selectedStatus === '报名中'}" @click="updateStatus('报名中')">报名中</button>
        <button :class="{active: selectedStatus === '进行中'}" @click="updateStatus('进行中')">进行中</button>
        <button :class="{active: selectedStatus === '已结束'}" @click="updateStatus('已结束')">已结束</button>
      </span>

      <h3>人数</h3>
      <span class="activity-item">
        <button :class="{active: selectedPeople === '全部'}" @click="updatePeople('全部')">全部</button>
        <button :class="{active: selectedPeople === '2'}" @click="updatePeople('2')">双人</button>
        <button :class="{active: selectedPeople === '3-5'}" @click="updatePeople('3-5')">3~5人</button>
        <button :class="{active: selectedPeople === '6-10'}" @click="updatePeople('6-10')">6~10人</button>
        <button :class="{active: selectedPeople === '11+'}" @click="updatePeople('11+')">>10人</button>
      </span>
    </view>

    <!-- 搜索结果 -->
    <div class="activities">
      <!-- 搜索结果标题 -->
      <div class="activity-header">
        <h3>搜索结果</h3>
      </div>

      <!-- 活动容器 -->
      <div class="activity-container">
        <div
          class="activity-item2"
          v-for="(activity, index) in activities"
          :key="index"
          @click="goToPage(activity.id)"
        >
          <!-- 图片容器 -->
          <div class="activity-img">
            <img :src="activity.profileimageurl" :alt="activity.activityname" />
          </div>
          <!-- 详细信息 -->
          <div class="activity-details">
            <h2>{{ activity.activityname }}</h2>
            <p>发布人: {{ activity.publisher }}</p>
            <p>开始时间: {{ formatDate(activity.timesubmit) }}</p>
            <p>人数: {{ activity.maxceiling }}人</p>
            <p>地点: {{ activity.location }}</p>
            <p>类型: {{ activity.type }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 返回顶部按钮 -->
    <button
      v-if="showBackToTop"
      class="back-to-top"
      @click="scrollToTop"
    >
      返回顶部
    </button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '', // 搜索框内容
      selectedCategory: '全部', // 当前选择的分类
      selectedStatus: '全部', // 当前选择的状态
      selectedPeople: '全部', // 当前选择的人数
      activities: [], // 活动列表数据
      showBackToTop: false, // 控制返回顶部按钮的显示
    };
  },
  methods: {
    // 更新分类选择
    updateCategory(category) {
      this.selectedCategory = category;
      this.fetchActivities();
    },
    updateStatus(status) {
      this.selectedStatus = status;
      this.fetchActivities();
    },
    updatePeople(people) {
      this.selectedPeople = people;
      this.fetchActivities();
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      if (isNaN(date)) return '未设置';
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    fetchActivities() {
      const url = new URL('http://192.168.1.43:8080/activities/searchbyfour');
      const params = {};

      if (this.searchQuery) {
        params.keyword = this.searchQuery;
      }
      if (this.selectedCategory !== '全部') {
        params.type = this.selectedCategory;
      }
      if (this.selectedStatus !== '全部') {
        params.status = this.selectedStatus;
      }
      if (this.selectedPeople !== '全部') {
        if (this.selectedPeople === '2') {
          params.minCeiling = 2;
          params.maxCeiling = 2;
        } else if (this.selectedPeople === '3-5') {
          params.minCeiling = 3;
          params.maxCeiling = 5;
        } else if (this.selectedPeople === '6-10') {
          params.minCeiling = 6;
          params.maxCeiling = 10;
        } else if (this.selectedPeople === '11+') {
          params.minCeiling = 11;
          params.maxCeiling = 9999;
        }
      }

      Object.keys(params).forEach((key) => url.searchParams.append(key, params[key]));

      fetch(url)
        .then((response) => {
          if (!response.ok) throw new Error('网络响应错误');
          return response.json();
        })
        .then((data) => {
          if (data.code === 200) {
            this.activities = data.data.map((activity) => ({
              id: activity.activityid,
              activityname: activity.activityname,
              profileimageurl: activity.profileimageurl || 'default_image.png',
              publisher: activity.headusername,
              timesubmit: activity.timeproceed1,
              maxceiling: activity.maxceiling,
              location: activity.location || '未设置',
              type: activity.type || '未指定类型',
            }));
          } else {
            this.activities = [];
            console.error('获取活动数据失败:', data.message);
          }
        })
        .catch((error) => {
          console.error('获取活动数据时发生错误:', error);
          this.activities = [];
        });
    },
    goToPage(activityId) {
      uni.navigateTo({
        url: "../activitydetail/activitydetail?id=" + activityId,
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
      window.scrollTo({
        top: 0,
        behavior: 'smooth',
      });
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
/*顶部标题栏*/
.container {
  max-width: 100%;
  height:100%;
  margin: 0 auto;
  padding: 1rem;
  background-color: #fff3e0;
  box-shadow: 0 0.625rem 0.625rem rgba(0, 0, 0, 0.1);
  line-height: 1.875rem;
  background-image: url('../../static/3.jpg');
  
}
.header text {
  font-size: 1.5rem;
  color: #333;
  font-weight: bold;
  display: grid;
   place-items: center; /* 同时水平和垂直居中 */
   }


/*搜索框*/
.search-area {
    display: flex; /* 使用 flexbox 布局，使输入框和按钮并排 */
    justify-content: center; /* 水平居中 */
    margin-bottom: 2.5vw; /* 与下面内容的间距，使用相对单位 */
}

.search-input {
	width: 15%;
	height: 3vw; /* 使用相对单位 */
    margin-top: 2.5vw; /* 与上面的内容的间距，使用相对单位 */
    width: 70vw; /* 增加输入框的宽度，使用相对单位 */
    padding: 3.75vw; /* 增加内边距，使输入框更大，使用相对单位 */
    border: 0.75vw solid #888; /* 边框样式，使用相对单位 */
    border-radius: 20px 0px 0px 20px; /* 圆角，左侧圆角，保留绝对单位 */
    font-size: 4vw; /* 字体大小，使用相对单位 */
    outline: none; /* 去掉聚焦边框 */
    transition: border-color 0.3s; /* 边框颜色过渡 */
}

.search-input:focus {
    border-color: #faea3f; /* 聚焦时改变边框颜色 */
}

.search-button {
    width: 18%;
    height: 14.5vw; /* 使用相对单位 */
    margin-top: 2.5vw; /* 与上面的内容的间距，使用相对单位 */
    padding: 2.5vw 2.5vw; /* 内边距，使用相对单位 */
    border: none; /* 去掉默认边框 */
    background-color: #faea3f; /* 按钮背景色 */
    color: rgb(107, 102, 102); /* 按钮文字颜色 */
    border-radius: 0 20px 20px 0; /* 圆角，右侧圆角，保留绝对单位 */
    cursor: pointer; /* 鼠标悬停时指针变化 */
    transition: background-color 0.3s; /* 背景颜色过渡 */
    font-size: 4.5vw; /* 字体大小，使用相对单位 */
    font-weight: bold; /* 使用相对单位 */
	line-height: 7.5vw;
	text-decoration: none;
	transition: background-color 0.3s;
	border: 0.75vw solid #888;
}

.search-button:hover {
    background-color: #ffd700; /* 鼠标悬停时按钮颜色 */
}

/*分类栏 */
/* 分类和状态按钮高亮样式 */
button.active {
  background-color: #ffd700;
  color: #000;
}


.classification,
{
	position: fixed;
	bottom: 10vh;
	left: 0;
	right: 0;
	display: flex;
	align-items: center;
	background-color: transparent;
}

.activities {
  margin: 1.25rem 0;
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-header text {
  font-size: 1rem;
  font-weight: bold;
  color: #338;
}

/*链接栏*/
.activity-item {
	display: flex; /* 使用Flexbox布局 */
	flex-wrap: wrap; /* 如果一行放不下，自动换行 */
	gap: 0; /* 按钮之间的间距 */
	line-height: 2.5rem;
}

.classification button:hover {
    background-color: #fff;
}

.classification button:focus {
    outline: none;
    background-color: #ccc;
}

.activity-item button {
	width: 18%;
	height: 2rem;
	background-color: #faea3f;
	border-radius: 10vw;
	margin-left: 1vw;
	cursor: pointer;
	margin-right: 0;
	padding: 0;
	text-decoration: none;
	border-radius: 5px;
	transition: background-color 0.3s;
	border: 1px solid #888;
	font-weight: bold;
	font-size: 3vw /* 改为vw单位 */
}

.activity-item button:last-child {
  margin-right: 0;
}

.activity-item {
  margin-bottom: 0.625rem;
  font-size: 0.9rem;
  font-weight: bold;
}

/*搜索结果*/
.activity-container {
	
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  line-height: 0.75rem;
}

.activity-item2 {
  flex: 0 0 calc(100% - 0.625rem);
  margin: 0.3125rem;
  border: 0.125rem solid #ddd;
  border-radius: 10px;
  background-color:  #f9f2e7;
}

/* 图片容器，固定尺寸 */
.activity-img {
  width: 100%;
  height: 12.5rem;
  overflow: hidden;
  margin-bottom: 0.3125rem;
}

.activity-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  display: block;
}

/* 详细信息部分 */
.activity-details h2 {
  font-size: 1.25rem;
  margin-bottom: 0.75rem;
  color: #333;
}

.activity-details p {
  font-size: 1rem;
  margin: 0.75rem 0;
  color: #666;
}

/* 返回顶部按钮的样式 */
.back-to-top {
  position: fixed;/* 固定定位，确保按钮始终在视口的指定位置，不会随着页面滚动而移动 */
  bottom: 16vw;/* 设置按钮在视口底部的距离为20px，确保按钮不贴近边缘 */ 
  /* 设置按钮在视口右侧的距离为20px，确保按钮不贴近边缘 */
  right: 2vw;
  
  background-color: #ffd700;
  color: black;
  padding: 1vw 1vw;
  font-size: 1vw;
  border-radius: 20px;
  cursor: pointer;
  z-index: 1000;
  padding: 2vw 3vw;
  transition: background-color 0.3s;
}

.back-to-top:hover {
  /* 改变背景颜色为较深的绿色，提供悬停时的视觉反馈 */
  background-color: #ffd700;
}
.activity-item button.active {
  background-color: #FFC300; /* 激活状态的背景颜色 */
  color: #000; /* 激活状态的文字颜色 */
  font-weight: bold; /* 激活状态的字体加粗 */
  border: 1px solid #888; 
}

</style>

