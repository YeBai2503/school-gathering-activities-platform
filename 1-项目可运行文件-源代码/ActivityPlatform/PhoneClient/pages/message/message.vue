<template>
  <div class="message-board">
    <!-- 留言板标题 -->
    <h2 class="title">留言板</h2>
    <!-- 留言列表 -->
    <uni-collapse class="a">
		  <uni-collapse-item class="div"
		    v-for="message in messages" 
		    :key="message.publisherid" 
		    :title="message.author + ': ' + message.content">
		    <p class="timestamp">{{ message.time }}</p>
		  		
		  		<!-- 如果当前用户是发布者，则显示撤回按钮 -->
		  		<button 
		  		  v-if="message.publisherid === userid" 
		  		  @click="retractMessage(message.id, message.publisherid)" 
		  		  class="retract-btn">撤回</button>
		  
		    <!-- 回复列表 -->
		    <div v-if="message.replies.length > 0" class="reply-list">
		      <p><strong>回复：</strong></p>
		      <div v-for="reply in message.replies" :key="reply.id" class="reply-item">
		        <p><strong>{{ reply.author }}:</strong> {{ reply.content }}</p>
		        <p class="timestamp">{{ reply.time }}</p>
		      </div>
		    </div>
		  		
		    <!-- 回复按钮 -->
		    <button @click="prepareReply(message)" class="requestbtn">回复</button>
		  </uni-collapse-item>
    </uni-collapse>

    <!-- 留言输入框 -->
    <div class="message-input">
      <input
        v-model="newMessage"
        type="text"
        :placeholder="inputPlaceholder"
        @keydown.enter="sendMessage"
      />
      <button @click="sendMessage" class="send-button">发送</button>
    </div>
  </div>
</template>

<script>
export default {
  onLoad() {
    const userId = uni.getStorageSync('userid'); 
	console.log(userId);
    if (userId) {
      this.userid = userId;
      this.getUserInfo(); 
      this.getAllPosts(); 
    } else {
      console.log('用户ID未找到');
    }
  },
  data() {
    return {
      userid: uni.getStorageSync('userid'), // 用户ID
      nickname: '', // 用户昵称
	  requestnickname: '',
      newMessage: '', // 新的留言内容
      messages: [], // 留言列表
	  publisherid: uni.getStorageSync('userid'),
      requestid: 0, // 如果是回复，则为回复的目标发布者的publisherid
      inputPlaceholder: '输入...', 
	  permission: 0,
    };
  },
  methods: {
    // 准备回复的逻辑
    prepareReply(message) {
	  this.nickname = message.publishnickname;
	  this.publisherid = message.publisherid; // 设置为所回复帖子的发布者ID
	  console.log(this.publisherid);
	  this.requestid = this.userid;  
	  console.log(this.requestid);
      this.inputPlaceholder = `回复 ${message.author}`; // 修改输入框提示文字
    },
    
    // 获取用户信息
    getUserInfo() {
      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/auth/showuser/' + this.userid,
        method: 'GET',
        header:{
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
            that.requestnickname = res.data.data.nickname;
			that.permission = res.data.data.permission;
          } else {
            uni.showToast({
              title: res.data.message || '获取信息错误',
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
	
	retractMessage(id, publisherid){
		const that = this;
		console.log(id);
		uni.request({
		  url: 'http://192.168.1.43:8080/messages/delete/' + id, 
		  method: 'DELETE',
		  header: {
		    'Content-Type': 'application/x-www-form-urlencoded'
		  },
		  data:{
			  messageId: id,
			  userId: uni.getStorageSync('userid'),
			  userPermission: this.permission
		  },
		  success(res) {
		    if (res.statusCode === 200) {
			  uni.showToast({
			    title: '删除成功',
			    icon: 'success'
			  });
			   that.getAllPosts();
		    } else {
		      uni.showToast({
		        title: res.data.message || '删除失败',
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
    
    // 获取所有帖子及其对应的回复
    getAllPosts() {
      const that = this;
      uni.request({
        url: 'http://192.168.1.43:8080/messages/list', 
        method: 'GET',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success(res) {
          if (res.statusCode === 200) {
	         // 获取所有帖子
			 const posts = res.data.data.filter(post => post.requestid === 0 && post.publisherid !== 0);
			 
			 // 获取所有回复
			 const replies = res.data.data.filter(reply => reply.requestid !== 0 && reply.publisherid !== 0);
	 
			 // 为每个帖子找出它的回复
			 posts.forEach(post => {
			   // 将回复归类到该帖子中
			   post.replies = replies.filter(reply => reply.publisherid === post.publisherid);
			 });
			 
            that.messages = posts.map(post => ({
			  id: post.id,
              publisherid: post.publisherid,
              author: post.publishnickname,
              content: post.content,
              time: post.timestamp,
              replies: post.replies.map(reply => ({
                id: reply.publisherid,
                author: reply.requestnickname,
                content: reply.content,
                time: reply.timestamp
              }))
            }));
          } else {
            uni.showToast({
              title: res.data.message || '获取帖子失败',
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

    // 发送消息的逻辑
    sendMessage() {
      const content = this.newMessage.trim();
      if (content) {
        const postData = {
		  publisherid: this.publisherid,       
          publishnickname: this.nickname,    
          content: content,                  
          timestamp: new Date().toISOString(),
          requestid: this.requestid,
		  requestnickname: this.requestnickname
        };

        uni.request({
          url: 'http://192.168.1.43:8080/messages/add',
          method: 'POST',
          header: {
            'Content-Type': 'application/json'
          },
          data: postData, 
          success: (res) => {
            if (res.statusCode === 200 && res.data.code === 200) {
              uni.showToast({
                title: '发布成功',
                icon: 'success'
              });
              this.newMessage = ''; 
              this.requestid = 0; // 重置 requestid，表示下次发帖
              this.inputPlaceholder = '输入...'; // 重置输入框提示文字
			  this.getAllPosts();
            } else {
              uni.showToast({
                title: res.data.message || '发布失败',
                icon: 'none'
              });
            }
          },
          fail: (error) => {
            uni.showToast({
              title: '发布请求失败',
              icon: 'none'
            });
          }
        });
      } else {
        uni.showToast({
          title: '请填写帖子内容',
          icon: 'none'
        });
      }
    }
  }
};
</script>



<style scoped>
	.a{
		flex: 0 0 calc(100% - 0.625rem);
		
		border: 0.2rem solid #faea3f;
		border-radius: 10px;
		background-color: #f1f5f9;
	}
	.div{
		padding-bottom: 0.3rem;
		background-color:  #f9f2e7;
	}
	
	.message-board {
	  padding: 5vw;
	  background-color: #f9f2e7;
	  min-height: 100vh;
	  box-sizing: border-box;
	  padding-bottom: 30vh; /* 给底部留出空间，防止被输入框覆盖 */
	  background-image: url('../../static/3.jpg');
	}

	.title {
	  text-align: center;
	  left: 40%;
	  font-size: 1.5rem; /* 相对字体大小 */
	  margin-bottom: 5vw;
	}


	.message-list {
	  margin-bottom: 5vw;
	}

	.message-item {
	  background-color: #fff;
	  padding: 4vw;
	  margin-bottom: 3vw;
	  border-radius: 2vw;
	  box-shadow: 0 2vw 4vw rgba(0, 0, 0, 0.1);
	}

	.timestamp {
	  font-size: 3vw; /* 相对字体大小 */
	  color: #888;
	  margin-bottom: 2vw;
	  background-color:  #f9f2e7;
	}

	.message-input {
	  position: fixed;
	  bottom: 9vh;
	  left: 0;
	  right: 0;
	  display: flex;
	  align-items: center;
	  padding: 3vw;
	  background-color: transparent;
	}

	.message-input input {
	  flex: 1;
	  padding: 3vw;
	  border: 2px solid #ccc;
	  border-radius: 10vw;
	  background-color: #f9f2e7;
	}

	.send-button {
	  background-color: #faea3f;
	  padding: 1vw 5vw;
	  border-radius: 10vw;
	  margin-left: 3vw;
	  cursor: pointer;
	  font-size: 4vw;
	}
	
	.retract-btn.requestbtn{
		width: 60%;
		border-radius: 1.5rem;
		background-color: #faea3f;
	}
	
	.reply-item{
		padding-top: 0.3rem;
		border: 2rem;
	}
	
	.reply-item p {
		padding: 0.1rem;
	}
	.reply-list{
		background-color:  #f9f2e7;;
	}
</style>