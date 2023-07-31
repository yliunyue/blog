<template>
  <div class="my-profile">
    <div class="UpdateUser-box">
      <h1 style="margin-bottom:30px">个人设置</h1>
      <el-tabs type="card" v-model="activeName" @tab-click="handleClick">
        <!-- 修改用户信息 -->
        <el-tab-pane label="用户信息" name="0">
          <el-form ref="form" :model="updateUser" label-width="80px">
            <el-form-item label="头像：">
              <el-upload
                class="avatar-uploader"
                action=""
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="updateUser.avatar"
                  :src="updateUser.avatar"
                  class="avatar"
                />

                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="用户名：">
              <el-input v-model="updateUser.nickname"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateUserFuc"
                >确认修改</el-button
              >
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 修改邮箱信息 -->
        <el-tab-pane label="邮箱信息" name="1"
          ><h3 style="margin:20px">邮箱：{{ user.email }}</h3>
          <el-button @click="updateEmail('old', user.email)"
            >修改邮箱</el-button
          >
        </el-tab-pane>
      </el-tabs>
    </div>
    <!-- 输入验证码弹出框 -->
    <el-dialog
      title="验证邮箱"
      :visible.sync="emailCodeVisible"
      width="30%"
      :before-close="handleClose"
    >
      <div>
        请输入验证码：<el-input
          v-model="eCode"
          placeholder="请输入验证吗"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="emailCodeVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEmailCode('old', eCode)"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!-- 新邮箱验证弹框 -->
    <el-dialog
      title="填写新邮箱"
      :visible.sync="newEmailVisible"
      width="30%"
      :before-close="handleClose"
    >
      <div>
        请输入新邮箱：
        <el-button
          size="mini"
          :loading="loadingBtn"
          @click="updateEmail('new', NewE)"
          >获取验证码</el-button
        >
        <el-input v-model="NewE" placeholder="请输入新邮箱"></el-input>
        <div>
          请输入验证码：<el-input
            v-model="NewECode"
            placeholder="请输入验证吗"
          ></el-input>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newEmailVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEmailCode('new', NewECode)"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import api from "@/request/api";
import * as qiniu from "qiniu-js";
// import * as xxx from "xxx"
export default {
  data() {
    return {
      activeName: "1",
      user: {},
      updateUser: {
        avatar: "",
        nickname: ""
      },
      emailCodeVisible: false, //旧邮箱验证码
      eCode: "", //旧验证码输入框
      getCode: "", //接口获得的验证码
      //新邮箱相关
      newEmailVisible: false,
      loadingBtn: false,
      NewE: "", //新邮箱地址
      NewECode: "" //新验证码
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    handleClose() {},
    // 获取个人信息接口
    getUserInfo() {
      api({
        url: `/sysUser/userMessage`,
        method: "get"
      }).then(res => {
        console.log("res-获取个人信息接口", res.data);
        if (res && res.code == 200) {
          this.user = res.data;
          this.updateUser = res.data;
        }
      });
    },
    // 修改个人信息接口
    updateUserFuc() {
      api({
        url: `/sysUser/updataMes?avatar=${this.updateUser.avatar}&nickname=${this.updateUser.nickname}`,
        method: "post"
      }).then(res => {
        console.log("修改个人信息接口", res);
        if (res && res.code == 200) {
          this.$message({
            type: "success",
            message: res.data,
            showClose: true
          });
        }
      });
    },
    // 修改邮箱按钮 发送验证码接口
    updateEmail(type, email) {
      if (email == "") {
        this.$message("请输入邮箱");
        return;
      }
      if (type == "new") {
        this.loadingBtn = true;
      } else {
        this.emailCodeVisible = true;
      }

      api({
        url: `/login/sendMsg?email=${email}`,
        method: "post"
      }).then(res => {
        if (type == "new") {
          this.loadingBtn = false;
        } else {
        }

        if (res && res.code == 200) {
          this.getCode = res.data.code;
          this.$message({
            type: "success",
            message: res.data.msg,
            showClose: true
          });
        }
      });
    },
    //确认验证验证码
    submitEmailCode(type, email) {
      if (email == "") {
        this.$message("请输入验证码");
        return;
      }

      // 后台获取的验证码和前端输入email的一样
      if (this.getCode == email) {
        if (type === "old") {
          this.newEmailVisible = true;
        } else {
          // 提交接口
          this.postEmail();
        }
      } else {
        // 后台获取的验证码和前端输入的不一样
        this.$message({
          type: "info",
          message: "验证码错误",
          showClose: true
        });
      }
    },
    // 提交邮箱接口
    postEmail() {
      api({
        url: `/sysUser/updataEmail?code=${this.NewECode}&email=${this.NewE}`,
        method: "post"
      }).then(res => {
        if (res && res.code == 200) {
          this.$message({
            type: "success",
            message: res.data,
            showClose: true
          });
          this.newEmailVisible = false;
          this.emailCodeVisible = false;
        }
      });
    },
    //

    handleAvatarSuccess(observable, file) {
      let _this = this;
      observable.subscribe({
        next(res) {
          console.log("上传中", res.total.percent);
        },
        error(err) {
          console.log("上传错误", err);
        },
        complete(res) {
          _this.updateUser.avatar =
            "http://ruizmypmv.hn-bkt.clouddn.com/" + res.key;
        }
      });
    },

    beforeAvatarUpload(file) {
      console.log("file", file);
      api({
        url: `upload/getToken`,
        method: "get"
      }).then(res => {
        console.log("七牛云", res.data);
        if (res && res.code == 200) {
          // this.user = res.data;
          const qiniuToken = res.data;
          const key = "my-image-" + Date.now();
          const putExtra = {
            params: {},
            mimeType: ["image/png", "image/jpeg", "image/gif"]
          };
          const config = {
            useCdnDomain: true,
            region: qiniu.region.z2
          };
          const observable = qiniu.upload(
            file,
            key,
            qiniuToken,
            putExtra,
            config
          );
          this.handleAvatarSuccess(observable, file);
        }
      });
      // axios.get("http://localhost:8889/upload/getToken").then(res => {
      //   console.log("res", res);
      //   if (res.data.success) {
      //     this.uploadData.key = file.uid + file.name;
      //     this.uploadData.token = res.data.data;
      //   } else {
      //     this.uploadData.key = "";
      //     this.uploadData.token = 1;
      //     return true;
      //   }
      // });
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
.UpdateUser-box {
  margin-top: 60px;
  padding: 20px;
  background: #fff;
}
.UpdateUser-box .el-form {
  width: 30%;
}

.UpdateUser-box .el-tabs__item {
  font-size: 24px !important;
}
/* 用户头像 */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
