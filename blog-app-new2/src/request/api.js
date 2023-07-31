// import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/request/token'
import router from '@/router'
const service = axios.create({
  baseURL: process.env.BASE_API,
  timeout: 10000
})

//request拦截器
service.interceptors.request.use(config => {

  if (store.state.token) {
    config.headers['Oauth-Token'] = getToken()
    config.headers['Authorization'] = getToken()
  }
  return config
}, error => {

  Promise.reject(error)
})

// respone拦截器

service.interceptors.response.use(
  response => {

    //全局统一处理 Session超时
    if (response.headers['session_time_out'] == 'timeout') {
      store.dispatch('fedLogOut')
    }

    const res = response.data;
    console.log("response", response);
    //0 为成功状态
    if (response.status !== 200) {

      //90001 Session超时
      if (res.code === 90001) {
        // return Promise.reject('error');
      }

      //20001 用户未登录
      if (res.code === 90002) {

        Message({
          type: 'warning',
          showClose: true,
          message: '未登录或登录超时，请重新登录哦'
        })
        router.push({ path: `/login` })

        // return Promise.reject('error');
      }

      //70001 权限认证错误
      if (res.code === 70001) {
        Message({
          type: 'warning',
          showClose: true,
          message: '你没有权限访问哦'
        })
        // return Promise.reject('error');
      }

      // return Promise.reject(res.msg);
    } else {
      // 接口请求成功
      // 业务不对---统一处理
      // 50001---未关注
      if (res.code != 200 || res.code == 50001) {
        Message({
          type: 'warning',
          showClose: true,
          message: res.msg
        })
        //20001 用户未登录
        if (res.code === 90002) {
          router.push({ path: `/login` })
        }
      }
      // return response.data;
    }
    // 业务正确
    return response.data;
  },
  error => {
    Message({
      type: 'warning',
      showClose: true,
      message: '连接失败'
    })
    // return Promise.reject('error')
  })
export default service
