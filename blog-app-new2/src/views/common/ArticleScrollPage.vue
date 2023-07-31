<template>
  <scroll-page
    :loading="loading"
    :offset="offset"
    :no-data="noData"
    v-on:load="load"
  >
    <article-item
      v-for="a in articles"
      :key="a.id"
      v-bind="a"
      :isMyEssey="isMyEssey"
      @del="del"
    ></article-item>
  </scroll-page>
</template>

<script>
import ArticleItem from "@/components/article/ArticleItem";
import ScrollPage from "@/components/scrollpage";
import { getArticles, getMyArticles, getArticlePush } from "@/api/article";

export default {
  name: "ArticleScrollPage",
  props: {
    offset: {
      type: Number,
      default: 100
    },
    page: {
      type: Object,
      default() {
        return {};
      }
    },
    query: {
      type: Object,
      default() {
        return {};
      }
    },
    isMyEssey: {
      type: Boolean,
      default() {
        return false;
      }
    },
    serc: {
      type: String,
      default() {
        return "";
      }
    }
  },
  watch: {
    serc: {
      handler(data, newdata) {
        console.log("serc", data, newdata, data === newdata);
        this.innerPage = {
          pageSize: 5,
          pageNumber: 1,
          name: "a.createDate",
          sort: "desc"
        };
        this.sercData = { queryString: data };
        // if (data === newdata) {
        this.getArticlesAll({ queryString: data });
        // }
      }
    },
    query: {
      handler(data) {
        this.noData = false;
        this.articles = [];
        this.innerPage.pageNumber = 1;
        this.getArticlesAll();
      },
      deep: true
    },
    page: {
      handler() {
        this.noData = false;
        this.articles = [];
        this.innerPage = this.page;
        this.getArticlesAll();
      },
      deep: true
    }
  },
  created() {
    this.routerPath = this.$route.fullPath;
    this.getArticlesAll();
  },
  data() {
    return {
      loading: false,
      noData: false,
      innerPage: {
        pageSize: 5,
        pageNumber: 1,
        name: "a.createDate",
        sort: "desc"
      },
      articles: [],
      routerPath: "",
      sercData: ""
    };
  },
  methods: {
    del(data) {
      console.log("data", data);
      if (data) {
        this.query = {
          page: 1,
          pageSize: 5,
          name: "a.createDate",
          sort: "desc"
        };
        this.innerPage = {
          pageSize: 5,
          pageNumber: 1,
          name: "a.createDate",
          sort: "desc"
        };
        this.getMyArticlesFuc();
      }
    },
    load(data) {
      // if (data > 400) {
        this.getArticlesAll(this.sercData);
      // }
    },
    view(id) {
      this.$router.push({ path: `/view/${id}` });
    },
    //我的文章
    getMyArticlesFuc(pData) {
      let that = this;
      that.loading = true;
      console.log("我的文章", that.query);
      getMyArticles(that.query, that.innerPage, pData)
        .then(data => {
          if (data.data.length == 0) {
            return;
          }

          let newArticles = data.data;
          if (newArticles && newArticles.length > 0) {
            that.innerPage.pageNumber += 1;
            if (!!pData) {
              that.articles = newArticles;
              return;
            }
            that.articles = that.articles.concat(newArticles);
          } else {
            that.articles = [];
            that.noData = true;
          }
        })
        .catch(error => {
          if (error !== "error") {
            that.$message({
              type: "error",
              message: "文章加载失败!",
              showClose: true
            });
          }
        })
        .finally(() => {
          that.loading = false;
        });
    },
    //查询文章

    // 文章推送
    getArticlePushFuc(pdata) {
      let that = this;
      that.loading = true;
      getArticlePush(that.query, that.innerPage, pdata)
        .then(data => {
          if (data.data.length == 0) {
            return;
          }
          let newArticles = data.data;
          if (newArticles && newArticles.length > 0) {
            if (!!pdata) {
              that.articles = newArticles;
              return;
            }
            that.innerPage.pageNumber += 1;
            that.articles = that.articles.concat(newArticles);
          } else {
            that.noData = true;
            that.articles = [];
          }
          console.log("文章推送", data.data);
        })
        .catch(error => {
          if (error !== "error") {
            that.$message({
              type: "error",
              message: "文章加载失败!",
              showClose: true
            });
          }
        })
        .finally(() => {
          that.loading = false;
        });
    },
    getArticlesAll(data) {
      console.log("isMyEssey", this.isMyEssey);
      if (this.routerPath == "/My") {
        if (this.isMyEssey) {
          this.getMyArticlesFuc(data);
        } else {
          this.getArticlePushFuc(data);
        }

        return;
      }
      let that = this;
      that.loading = true;

      getArticles(that.query, that.innerPage)
        .then(data => {
          let newArticles = data.data;
          if (newArticles && newArticles.length > 0) {
            that.innerPage.pageNumber += 1;
            that.articles = that.articles.concat(newArticles);
          } else {
            that.noData = true;
          }
        })
        .catch(error => {
          if (error !== "error") {
            that.$message({
              type: "error",
              message: "文章加载失败!",
              showClose: true
            });
          }
        })
        .finally(() => {
          that.loading = false;
        });
    }
  },
  components: {
    "article-item": ArticleItem,
    "scroll-page": ScrollPage
  }
};
</script>

<style scoped>
.el-card {
  border-radius: 0;
}

.el-card:not(:first-child) {
  margin-top: 10px;
}
</style>
