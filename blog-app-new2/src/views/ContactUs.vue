<template>
  <div class="my-profile" style="width: 960px;">
    <div class="contact-info">
      <div class="contact-item">
        <i class="el-icon-message"></i>
        <span>QQ：12345678</span>
      </div>
      <div class="contact-item">
        <i class="el-icon-chat-dot-square"></i>
        <span>微信：wechat123</span>
      </div>
      <div class="contact-item">
        <i class="el-icon-info"></i>
        <span>欢迎使用blog，有问题请联系我</span>
      </div>
    </div>
    <el-form :model="form" label-width="80px" ref="questionForm">
      <el-form-item label="反馈">
        <el-input v-model="question" placeholder="请输入反馈问题"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="feedback">反馈</el-button>
      </el-form-item>
    </el-form>
    <el-card class="question" v-for="item of feedbackList" :key="item.id">
      <div class="question-header">
        <span class="question-username">{{ item.userName }}</span>
        <div>
          <span class="question-time">{{ item.createDate }}</span>
        </div>
      </div>
      <div class="question-content ml-10">{{ item.content }}</div>

      <div class="answer" v-if="item.adminfeed">
        <div class="answer-header">
          <span class="answer-username">{{ item.adminfeed.adminName }}</span>
          <span class="answer-time ">{{ item.adminfeed.createDate }}</span>
        </div>
        <div class="answer-content ml-10">{{ item.adminfeed.content }}</div>
      </div>
    </el-card>
  </div>
</template>
<script>
import api from "@/request/api";
export default {
  data() {
    return {
      feedbackList: [],
      question: ""
    };
  },
  created() {
    this.getFeedback();
  },
  methods: {
    // 获得所有反馈
    getFeedback() {
      api({
        url: `/feedback/feedbackList`,
        method: "get"
      }).then(res => {
        console.log("显示反馈", res.data);
        if (res && res.code == 200) {
          this.feedbackList = res.data;
        }
      });
    },
    feedback() {
      if (this.question === "") {
        this.$message("反馈内容不能为空");
      } else {
        api({
          url: `/feedback`,
          method: "post",
          data: {
            content: this.question
          }
        }).then(res => {
          console.log("反馈内容不能为空", res.data);
          if (res && res.code == 200) {
            this.$message({
              type: "success",
              message: res.data,
              showClose: true
            });
            this.question = "";
            this.getFeedback();
          }
        });
      }
    }
    //
  }
};
</script>
<style scoped>
.my-profile {
  padding: 20px 40px;
  width: calc(100% - 80px);
  height: calc(100% - 40px);
}
.contact-info {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.el-card__body {
  padding: 10px;
}
.question {
  margin-bottom: 10px;
  width: 100%;
}
.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.question-username {
  font-weight: bold;
  font-size: 20px;
}
.question-content {
  margin-bottom: 10px;
}
.answer {
  margin-left: 30px;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
}

.add-answer {
  margin: 10px 0px;
}
.ml-10 {
  margin-left: 10px;
}
.answer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.answer-username {
  font-weight: bold;
  font-size: 18px;
}
.answer-content {
  margin-bottom: 10px;
}
</style>
