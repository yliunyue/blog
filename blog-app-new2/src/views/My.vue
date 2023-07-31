<template>
  <div class="my-profile" style="width: 960px;" v-loading="loading">
    <div>
      <div class="box-card user-box flex">
        <div class="avatar">
          <img
            style="width:140px;height: 140px;border-radius: 50%;"
            mode="widthFix"
            :src="user.avatar"
          />
        </div>
        <div class="user-info">
          <div class="username flex">
            <div>{{ user.nickname }}</div>
            <el-button @click="showEditProfileDialog">编辑资料</el-button>
          </div>
          <div class="flex-start">
            <div class="num-box" @click="showFans">
              <div class="num">
                {{ user.fans }}
              </div>
              <div>
                粉丝数：
              </div>
            </div>
            <div class="num-box" @click="showFollow">
              <div class="num">
                {{ user.followerNums }}
              </div>
              <div>
                关注数：
              </div>
            </div>
          </div>
          <div class="email">邮箱：{{ user.email }}</div>

          <div class="email">注册时间：{{ user.createDate }}</div>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <el-tabs type="border-card" v-model="activeName" @tab-click="changeTab">
        <el-tab-pane label="我的文章">
          <div class="article-list">
            <el-input
              placeholder="请输入内容"
              v-model="articleVal"
              class="input-with-select"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="serc('articleVal')"
              ></el-button>
            </el-input>
            <article-scroll-page
              v-if="activeName === '0'"
              :offset="200"
              :serc="articleValC"
              :isMyEssey="true"
            ></article-scroll-page>
          </div>
        </el-tab-pane>
        <el-tab-pane label="文章推送">
          <div class="article-list">
            <el-input
              placeholder="请输入内容"
              v-model="articlePostVal"
              class="input-with-select"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="serc('articlePostVal')"
              ></el-button>
            </el-input>

            <article-scroll-page
              v-if="activeName === '1'"
              :offset="400"
              :serc="articlePostValC"
              :isMyEssey="false"
            ></article-scroll-page>
          </div>
        </el-tab-pane>
        <el-tab-pane label="粉丝">
          <div class="article-list">
            <FansAttionList type="fans"></FansAttionList>
          </div>
        </el-tab-pane>
        <el-tab-pane label="关注">
          <div class="article-list">
            <FansAttionList type="care"></FansAttionList>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!--粉丝/关注 -->
    </div>
  </div>
</template>
<script>
import api from "@/request/api";
import ArticleScrollPage from "@/views/common/ArticleScrollPage";
import FansAttionList from "@/views/common/FansAttionList";
export default {
  components: {
    ArticleScrollPage,
    FansAttionList
  },
  data() {
    return {
      user: {},
      activeName: 0,
      pushedArticles: [
        {
          id: 3,
          title: "文章3",
          content: "这是文章3的内容",
          author: "小红"
        },
        {
          id: 4,
          title: "文章4",
          content: "这是文章4的内容",
          author: "小红"
        }
      ],
      articleVal: "",
      articlePostVal: "",
      // 传递给子组件的
      articleValC: "",
      articlePostValC: "",
      loading: true
    };
  },
  created() {
    this.getUserInfo();
    // this.getEssay();
  },
  methods: {
    changeTab(e) {
      console.log("e", this.activeName);
      // this.activeName = e.paneName;
    },
    // 搜索按钮
    serc(type) {
      if (type == "articlePostVal") {
        console.log("文章推送");
        this.articlePostValC = this.articlePostVal;

        // api({
        //   url: `/articles/FindPushArticle`,
        //   method: "post",
        //   data: {
        //     page: 1,
        //     pageSize: 5,
        //     queryString: this.articlePostVal
        //   }
        // }).then(res => {
        //   console.log("获取我的文章", res.data);
        //   if (res && res.code == 200) {
        //     // this.user = res.data;
        //   }
        // });
      } else {
        console.log("我的文章");
        this.articleValC = this.articleVal;
        // api({
        //   url: `/articles/FindMyListArticle`,
        //   method: "post",
        //   data: {
        //     page: 1,
        //     pageSize: 5,
        //     queryString: this.articleVal
        //   }
        // }).then(res => {
        //   console.log("获取我的文章", res.data);
        //   if (res && res.code == 200) {
        //     // this.user = res.data;
        //   }
        // });
      }
    },
    // 获取个人信息接口
    getUserInfo() {
      this.loading = true;
      api({
        url: `/sysUser/userMessage`,
        method: "get"
      }).then(res => {
        console.log("res-获取个人信息接口", res.data);
        if (res && res.code == 200) {
          this.user = res.data;
        }
        this.loading = false;
      });
    },
    // 获取我的文章接口
    getEssay() {
      api({
        url: `/articles/myListArticle`,
        method: "post",
        data: {
          page: 1,
          pageSize: 5
        }
      }).then(res => {
        console.log("获取我的文章", res.data);
        if (res && res.code == 200) {
          // this.user = res.data;
        }
      });
    },

    showEditProfileDialog() {
      // 显示编辑个人资料对话框
      this.$router.push({ path: `/UpdateUser` });
    },
    showFollow() {
      // 显示关注列表对话框
    },
    showFans() {
      // 显示粉丝列表对话框
    },
    editArticle(article) {
      // 编辑文章
    },
    deleteArticle(article) {
      // 删除文章
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
.my-profile .user-box {
  padding: 0px 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
.my-profile .flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.my-profile .flex-start {
  display: flex;
  justify-content: start;
  align-items: center;
}
.my-profile .num-box {
  border-right: 2px solid rgb(209, 208, 208);
  /* margin-right: 20px; */
  margin: 10px 10px;
  padding-right: 20px;
}
.my-profile .num {
  /* font-size: 30px; */
  text-align: center;
  color: rgb(120, 119, 119);
}
/* 头像 */
.my-profile .avatar {
  width: 140px;
  margin: 40px 80px;
}
/* 用户信息 */
.my-profile .user-info {
  flex: 1;
  font-size: 18px;
}
.my-profile .username {
  font-size: 40px;
  font-weight: bold;
  /* margin-bottom: 10px; */
  border-bottom: 2px solid rgb(217, 217, 217);
}
.my-profile .email {
  margin: 10px;
}

/* 文章列表 */
.my-profile .article-list {
  margin-top: 20px;
}
.my-profile .el-table .cell {
  white-space: pre-wrap;
}
.my-profile .el-table th {
  text-align: center;
  font-weight: bold;
}
.my-profile .el-table td {
  text-align: center;
}
.my-profile .el-table-column--selection {
  text-align: center;
}
.my-profile .el-button--danger {
  background-color: #ff5151;
  border-color: #ff5151;
}
.my-profile .el-button--danger:hover {
  background-color: #f44336;
  border-color: #f44336;
}
</style>
