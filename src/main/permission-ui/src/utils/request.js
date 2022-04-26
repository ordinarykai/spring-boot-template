import axios from 'axios'
import { Notification, Message } from 'element-ui'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  // baseURL: process.env.VUE_APP_BASE_API,
  baseURL: 'http://localhost:8081/api',
  // 超时
  timeout: 10000
})

// request拦截器
service.interceptors.request.use(config => {
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.params = {}
    config.url = url
  }
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
  // 状态码
  const code = res.data.code
  // 返回信息
  const message = res.data.message
  // 二进制数据则直接返回
  if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
    return res.data
  }
  if (code === 200) {
    return res.data
  } else {
    Notification.error({
      title: message
    })
    // eslint-disable-next-line prefer-promise-reject-errors
    return Promise.reject('error')
  }
},
error => {
  console.log('err' + error)
  let { message } = error
  if (message === 'Network Error') {
    message = '后端接口连接异常'
  } else if (message.includes('timeout')) {
    message = '系统接口请求超时'
  } else if (message.includes('Request failed with status code')) {
    message = '系统接口' + message.substr(message.length - 3) + '异常'
  }
  Message({
    message: message,
    type: 'error',
    duration: 5 * 1000
  })
  return Promise.reject(error)
}
)

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams (params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    var part = encodeURIComponent(propName) + '='
    if (value !== null && typeof (value) !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']'
            var subPart = encodeURIComponent(params) + '='
            result += subPart + encodeURIComponent(value[key]) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&'
      }
    }
  }
  return result
}

export default service
