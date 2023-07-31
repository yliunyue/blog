webpackJsonp([10],{Kq0v:function(e,r){},tcoL:function(e,r,s){"use strict";Object.defineProperty(r,"__esModule",{value:!0});s("M9A7");var t=s("OMN4"),a=s.n(t),o={name:"Register",data:function(){return{userForm:{account:"",nickname:"",password:"",email:"",code:""},rules:{account:[{required:!0,message:"请输入用户名",trigger:"blur"},{max:10,message:"不能大于10个字符",trigger:"blur"}],nickname:[{required:!0,message:"请输入昵称",trigger:"blur"},{max:10,message:"不能大于10个字符",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"},{max:10,message:"不能大于10个字符",trigger:"blur"}],code:[{required:!0,message:"请输入验证码",trigger:"blur"},{min:0,message:"不能为空",trigger:"blur"}]}}},methods:{register:function(e){var r=this;this.$refs[e].validate(function(e){if(!e)return!1;r.$store.dispatch("register",r.userForm).then(function(){r.$message({message:"注册成功 快写文章吧",type:"success",showClose:!0}),r.$router.push({path:"/"})}).catch(function(e){"error"!==e&&r.$message({message:e,type:"error",showClose:!0})})})},getCode:function(){var e=this;this.userForm.code="";if(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(this.userForm.email)){this.msgFlag=!1;var r=new URLSearchParams;r.append("email",this.userForm.email),a.a.post("/login/sendMsg",r).then(function(r){r.data.success&&e.$message({message:"验证码发送成功，五分钟有效",type:"success"})}).catch(function(r){"Error: Request failed with status code 403"==r&&e.$message.error("失败")})}}}},i={render:function(){var e=this,r=e.$createElement,s=e._self._c||r;return s("div",{directives:[{name:"title",rawName:"v-title"}],attrs:{id:"register","data-title":"注册"}},[s("div",{staticClass:"me-login-box me-login-box-radius"},[s("h1",[e._v("注册")]),e._v(" "),s("el-form",{ref:"userForm",attrs:{model:e.userForm,rules:e.rules}},[s("el-form-item",{attrs:{prop:"account"}},[s("el-input",{attrs:{placeholder:"用户名"},model:{value:e.userForm.account,callback:function(r){e.$set(e.userForm,"account",r)},expression:"userForm.account"}})],1),e._v(" "),s("el-form-item",{attrs:{prop:"password"}},[s("el-input",{attrs:{placeholder:"密码",type:"password"},model:{value:e.userForm.password,callback:function(r){e.$set(e.userForm,"password",r)},expression:"userForm.password"}})],1),e._v(" "),s("el-form-item",{attrs:{prop:"email"}},[s("el-input",{attrs:{placeholder:"邮箱"},model:{value:e.userForm.email,callback:function(r){e.$set(e.userForm,"email",r)},expression:"userForm.email"}})],1),e._v(" "),s("el-form-item",{attrs:{prop:"code"}},[s("el-input",{attrs:{placeholder:"验证码"},model:{value:e.userForm.code,callback:function(r){e.$set(e.userForm,"code",r)},expression:"userForm.code"}})],1),e._v(" "),s("el-form-item",{staticClass:"me-login-button",attrs:{size:"small"}},[s("el-button",{attrs:{type:"primary"},on:{click:e.getCode}},[e._v("获取验证码")])],1),e._v(" "),s("el-form-item",{staticClass:"me-login-button",attrs:{size:"small"}},[s("el-button",{attrs:{type:"primary"},nativeOn:{click:function(r){return r.preventDefault(),e.register("userForm")}}},[e._v("注册")])],1)],1)],1)])},staticRenderFns:[]};var u=s("VU/8")(o,i,!1,function(e){s("Kq0v")},"data-v-2743b8a2",null);r.default=u.exports}});