<template>
  <div>
    <div v-if="arr.length != 0">
      <div v-for="(v, i) of arr" :key="i" class="flex box">
        <div class="flex-s">
          <!-- <el-avatar
            size="large"
            :src="v.avatar"

          ></el-avatar> -->
          <img
            :src="v.avatar"
            alt=""
            style="width:60px;height: 60px;border-radius: 50%;"
            mode="widthFix"
          />
          <span>{{ v.nickname }}</span>
        </div>
        <el-button
          v-if="type !== 'fans'"
          icon="el-icon-edit"
          @click="cancel(v.id)"
          >取消关注</el-button
        >
      </div>
    </div>
    <el-empty v-else description="暂无数据"></el-empty>
  </div>
</template>

<script>
import api from "@/request/api";
export default {
  props: {
    type: String
  },
  data() {
    return {
      arr: []
    };
  },
  created() {
    console.log("type", this.type);
    if (this.type === "fans") {
      this.getFans();
    } else {
      this.getCare();
    }
  },
  methods: {
    // 取消关注
    cancel(id) {
      api({
        url: `/follow/notAttentionTo/${id}`,
        method: "get"
      }).then(res => {
        console.log("res-取消关注", res);
        if (res && res.code == 200) {
          this.$message({
            type: "success",
            message: res.data,
            showClose: true
          });
          this.getCare();
        }
      });
    },
    // 粉丝数接口
    getFans() {
      api({
        url: `/follow/fansList`,
        method: "get"
      }).then(res => {
        console.log("res-粉丝数接口", res.data);
        if (res && res.code == 200) {
          this.arr = res.data;
        }
      });
    },
    // 关注列表
    getCare() {
      api({
        url: `/follow/followList`,
        method: "get"
      }).then(res => {
        console.log("res-关注列表", res.data);
        if (res && res.code == 200) {
          this.arr = res.data;
        }
      });
    }
  }
};
//
</script>

<style scoped>
.flex {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.flex-s {
  display: flex;
  align-items: center;
  justify-content: start;
}
.box {
  margin: 10px;
}
.box img {
  margin-right: 20px;
}
</style>
