<template>
  <el-card class="me-area" :body-style="{ padding: '16px' }">
    <div class="me-article-header">
      <div
        style="display: flex;justify-content: space-between;align-items: center"
      >
        <a @click="view(id)" class="me-article-title">{{ title }}</a>
        <div v-if="routerPath == '/My' && isMyEssey">
          <el-button icon="el-icon-edit" @click="editArticle()">编辑</el-button>
          <el-button icon="el-icon-delete" @click="delectArticle()"
            >删除</el-button
          >
        </div>

        <!-- el-icon-remove-outline -->
      </div>

      <el-button v-if="weight > 0" class="me-article-icon" type="text"
        >置顶</el-button
      >
      <span class="me-pull-right me-article-count">
        <i class="me-icon-comment"></i>&nbsp;{{ commentCounts }}
      </span>
      <span class="me-pull-right me-article-count">
        <i class="el-icon-view"></i>&nbsp;{{ viewCounts }}
      </span>
    </div>

    <div class="me-artile-description">
      {{ summary }}
    </div>
    <div class="me-article-footer">
      <span class="me-article-author">
        <i class="me-icon-author"></i>&nbsp;{{ author.nickname }}
      </span>

      <!-- <span class="me-article-author">
	    	<i class="me-icon-author"></i>&nbsp;{{category}}
	    </span> -->

      <el-tag v-for="t in tags" :key="t.tagName" size="mini" type="success">{{
        t.tagName
      }}</el-tag>

      <span class="me-pull-right me-article-count">
        <i class="el-icon-time"></i>&nbsp;{{ createDate | format }}
      </span>
    </div>
  </el-card>
</template>

<script>
import { formatTime } from "../../utils/time";
import api from "@/request/api";

export default {
  name: "ArticleItem",
  props: {
    id: Number,
    weight: Number,
    title: String,
    commentCounts: Number,
    viewCounts: Number,
    summary: String,
    author: String,
    tags: Array,
    createDate: String,
    isMyEssey: Boolean
    // category:String
  },
  data() {
    return {
      routerPath: ""
    };
  },
  created() {
    this.routerPath = this.$route.fullPath;
  },
  methods: {
    view(id) {
      this.$router.push({ path: `/view/${id}` });
    },
    // 编辑
    editArticle() {
      console.log("111", this.id);
      this.$router.push({ path: `/write/${this.id}` });
    },
    // 删除
    delectArticle() {
      this.$alert(`是否删除${this.title}`, "删除", {
        confirmButtonText: "确定",
        callback: action => {
          console.log("确定删除", action);
          // this.$message({
          //   type: "info",
          //   message: `action: ${action}`
          // });
          if (action == "confirm") {
            this.delFuc();
          }
          //
        }
      });
    },
    // 删除文章接口
    delFuc() {
      api({
        url: `/articles/delete/${this.id}`,
        method: "Delete"
      }).then(res => {
        console.log("删除文章接口", res.data);
        if (res && res.code == 200) {
          // this.user = res.data;
          this.$message({
            type: "success",
            message: res.data
          });
          this.$emit("del", true);
        }
      });
    }
  }
};
</script>

<style scoped>
.me-article-header {
  /*padding: 10px 18px;*/
  padding-bottom: 10px;
}

.me-article-title {
  font-weight: 600;
}

.me-article-icon {
  padding: 3px 8px;
}

.me-article-count {
  color: #a6a6a6;
  padding-left: 14px;
  font-size: 13px;
}

.me-pull-right {
  float: right;
}

.me-artile-description {
  font-size: 13px;
  line-height: 24px;
  margin-bottom: 10px;
}

.me-article-author {
  color: #a6a6a6;
  padding-right: 18px;
  font-size: 13px;
}

.el-tag {
  margin-left: 6px;
}
</style>
