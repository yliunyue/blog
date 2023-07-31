<template>
  <div id="register" v-title data-title="注册">
    <!--<video preload="auto" class="me-video-player" autoplay="autoplay" loop="loop">
          <source src="../../static/vedio/sea.mp4" type="video/mp4">
      </video>-->

    <div class="me-login-box me-login-box-radius">
      <h1>注册</h1>

      <el-form ref="userForm" :model="userForm" :rules="rules">
        <el-form-item prop="account">
          <el-input placeholder="用户名" v-model="userForm.account"></el-input>
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input placeholder="昵称" v-model="userForm.nickname"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input placeholder="密码" type="password" v-model="userForm.password"></el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input placeholder="邮箱" v-model="userForm.email"></el-input>
                <el-button size="mini" @click="getCode">获取邮箱验证</el-button>
        </el-form-item>
        <el-form-item prop="code">
          <el-input placeholder="验证码" v-model="userForm.code"></el-input>
        </el-form-item>

        <el-form-item size="small" class="me-login-button">
          <el-button type="primary"  plain v-on:click="register">注册</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>
<!-- 2735901152@qq.com -->
<script>
  import {register} from '@/api/login';
  import axios from 'axios';

  export default {
    name: 'Register',
    data() {
      return {
        userForm: {
          account: '',
          nickname: '',
          password: '',
          email: '',
          code: ''
        },
        rules: {
          account: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {max: 10, message: '不能大于10个字符', trigger: 'blur'}
          ],
          nickname: [
            {required: true, message: '请输入昵称', trigger: 'blur'},
            {max: 10, message: '不能大于10个字符', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {max: 10, message: '不能大于10个字符', trigger: 'blur'}
          ],
          code: [
            {required: true, message: '请输入验证码', trigger: 'blur'},
            {min: 0, message: '不能为空', trigger: 'blur'}
          ]
        }

      }
    },
    methods: {
      register() {
        axios.post("/register",this.userForm).then((res)=>{
          console.log("1")
          if (res.data.success){
            console.log("11")
                this.$message({
                    message: '验证码发送成功，五分钟有效',
                    type: 'success'
                 });
                 this.$router.push({path: '/login'})
          }
          console.log("2")
        }).catch((error) => {
          console.log("3")
                that.$message({message: error, type: 'error', showClose: true}); 
            })
        // let that = this
        // this.$refs[formName].validate((valid) => {
        //   if (valid) {
        //     that.$store.dispatch('register', that.userForm).then(() => {
        //       that.$message({message: '注册成功 快写文章吧', type: 'success', showClose: true});
        //       that.$router.push({path: '/'})
        //     }).catch((error) => {
        //       if (error !== 'error') {
        //         that.$message({message: error, type: 'error', showClose: true});
        //       }
        //     })

        //   } else {
        //     return false;
        //   }
        // });

      },
      getCode(){
        this.userForm.code = ''
        // 改为邮箱验证
        const regex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        if (regex.test(this.userForm.email)) {
            this.msgFlag = false
            var params = new URLSearchParams();
            params.append('email',this.userForm.email);
            axios.post("/login/sendMsg",params).then((res)=>{
              if (res.data.success){
                  this.$message({
                    message: '验证码发送成功，五分钟有效',
                    type: 'success'
                 });
             }
            }).catch((error)=>{
                    if (error == "Error: Request failed with status code 403"){
                        this.$message.error("失败");
                    }
                });
            }
      }
    }
  }
</script>

<style scoped>
  #login {
    min-width: 100%;
    min-height: 100%;
  }

  .me-video-player {
    background-color: transparent;
    width: 100%;
    height: 100%;
    object-fit: fill;
    display: block;
    position: absolute;
    left: 0;
    z-index: 0;
    top: 0;
  }

  .me-login-box {
    position: absolute;
    width: 300px;
    height: 420px;
    background-color: white;
    margin-top: 150px;
    margin-left: -180px;
    left: 50%;
    padding: 30px;
  }

  .me-login-box-radius {
    border-radius: 10px;
    box-shadow: 0px 0px 1px 1px rgba(161, 159, 159, 0.1);
  }

  .me-login-box h1 {
    text-align: center;
    font-size: 24px;
    margin-bottom: 20px;
    vertical-align: middle;
  }

  .me-login-design {
    text-align: center;
    font-family: 'Open Sans', sans-serif;
    font-size: 18px;
  }

  .me-login-design-color {
    color: #5FB878 !important;
  }

  .me-login-button {
    text-align: center;
  }

  .me-login-button button {
    width: 100%;
  }

</style>
