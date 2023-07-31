<template>
  <div class="my-profile" style="width: 960px;">
    <el-form :model="form" label-width="80px" ref="questionForm">
      <el-form-item label="问题">
        <el-input v-model="question" placeholder="请输入问题"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitQuestion">提交</el-button>
      </el-form-item>
    </el-form>
    <el-input
      placeholder="请输入内容"
      v-model="content"
      class="input-with-select"
    >
      <el-button
        slot="append"
        icon="el-icon-search"
        @click="serc()"
      ></el-button>
    </el-input>
    <el-card class="question" v-for="item of discussion" :key="item.id">
      <div class="question-header" style="">
        <span class="question-username">{{ item.userName }}</span>
        <div>
          <el-button
            class="question-status"
            plain
            :type="item.status === 1 ? 'info' : 'success'"
            size="mini"
            :disabled="item.status === 1"
            @click="confirmSolve(item.id)"
            >{{ item.status === 1 ? "已解决" : "确认解决" }}</el-button
          >
          <span class="question-time">{{ item.createDate }}</span>
        </div>
      </div>
      <div class="question-content ml-10">{{ item.question }}</div>
      <el-input
        style="margin:10px 0px"
        v-if="item.isAnswer"
        v-model="answer"
        placeholder="请输入问题"
      ></el-input>

      <el-button
        class="add-answer ml-10"
        type="primary"
        size="mini"
        @click="handleAddAnswer(item.id)"
        >回答</el-button
      >
      <el-button
        class="question-status"
        size="mini"
        v-if="item.answerList.length > 0"
        @click="showAnswer(item.id)"
        >{{ !item.isShowAnswer ? "查看回复" : "收起回复" }}</el-button
      >

      <div
        class="answer"
        v-if="item.answerList.length > 0 && item.isShowAnswer"
      >
        <div v-for="answer in item.answerList" :key="answer.id">
          <div class="answer-header">
            <span class="answer-username">{{ answer.userName }}</span>
            <span class="answer-time ">{{ answer.createDate }}</span>
          </div>
          <div class="answer-content ml-10">{{ answer.content }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import api from "@/request/api";
export default {
  name: "Discussion",
  data() {
    return {
      discussion: [],
      question: "",
      answer: "",
      content: ""
    };
  },
  created() {
    this.getDiscussion();
  },
  methods: {
    serc() {
      api({
        url: `/question/findQuestion`,
        method: "post",
        data: {
          page: 1,
          pageSize: 10,
          queryString: this.content
        }
      }).then(res => {
        console.log("问答数据", res.data);
        if (res && res.code == 200) {
          res.data.questionVoList.forEach(v => {
            v.isAnswer = false;
            v.isShowAnswer = false;
          });
          this.discussion = res.data.questionVoList;
        }
      });
    },
    getDiscussion() {
      // return;
      api({
        url: `/question/pageQuestion`,
        method: "post",
        data: {
          page: 1,
          pageSize: 1000
        }
      }).then(res => {
        console.log("问答数据", res.data);
        if (res && res.code == 200) {
          res.data.questionVoList.forEach(v => {
            v.isAnswer = false;
            v.isShowAnswer = false;
          });
          this.discussion = res.data.questionVoList;
        }
      });
    },
    // 提问按钮
    submitQuestion() {
      if (this.question === "") {
        this.$message("提问不能为空");
      } else {
        api({
          url: `/question/ask`,
          method: "post",
          data: {
            question: this.question
          }
        }).then(res => {
          console.log("提问", res.data);
          if (res && res.code == 200) {
            this.$message({
              type: "success",
              message: res.data,
              showClose: true
            });
            this.question = "";
            this.getDiscussion();
          }
        });
      }
    },

    handleAddAnswer(questionId) {
      // 实现添加回答的逻辑
      console.log("this.discussion", this.discussion);
      const current = this.discussion.find(v => {
        return v.id === questionId;
      });
      console.log("current", current);
      if (!current.isAnswer) {
        // 展开回答输入框
        this.discussion.forEach(v => {
          if (v.id === questionId) {
            v.isAnswer = true;
          } else {
            v.isAnswer = false;
          }
        });
      } else {
        if (this.answer === "") {
          this.$message("回答不能为空");
        } else {
          api({
            url: `/question/answer?questionId=${questionId}`,
            method: "post",
            data: {
              content: this.answer
            }
          }).then(res => {
            console.log("回答", res.data);
            if (res && res.code == 200) {
              this.$message({
                type: "success",
                message: res.data,
                showClose: true
              });
              this.answer = "";
              this.getDiscussion();
            }
          });
        }
      }
    },
    // 查看回复
    showAnswer(questionId) {
      // 展开
      this.discussion.forEach(v => {
        if (v.id === questionId) {
          v.isShowAnswer = !v.isShowAnswer;
        }
      });
    },
    // 确认解决
    confirmSolve(questionId) {
      console.log("确认解决", questionId);
      api({
        url: `/question/status?questionId=${questionId}`,
        method: "post"
      }).then(res => {
        console.log("回答", res.data);
        if (res && res.code == 200) {
          this.$message({
            type: "success",
            message: res.data,
            showClose: true
          });
          this.getDiscussion();
        }
      });
    }
  }
};
</script>
<style>
.my-profile {
  padding: 20px 40px;
  width: calc(100% - 80px);
  height: calc(100% - 40px);
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
