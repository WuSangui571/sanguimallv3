import axios from "axios";
import {getTokenName, messageConfirm, messageTip, removeToken} from "../util/util.js";

// 定义后端接口地址的前缀
axios.defaults.baseURL = "http://localhost:80";

export function doGet(url, params) {
    return axios({
        method: 'get',
        url: url,
        // params 参数形式：{name: "张三", age: 22}
        params: params,
        dataType: "json"
    })
}

export function doPost(url, data) {
    return axios({
        method: 'post',
        url: url,
        data: data,
        dataType: "json"
    })
}

export function doPut(url, data) {
    return  axios({
        method: 'put',
        url: url,
        data: data,
        dataType: "json"
    })
}

export function doDelete(url, params) {
    return axios({
        method: 'delete',
        url: url,
        params: params,
        dataType: "json"
    })
}

// 添加请求拦截器
axios.interceptors.request.use((config) => {
    // 在发送请求之前做些什么，在请求头中放一个token（jwt），传给后端接口
    let token = window.sessionStorage.getItem(getTokenName());
    if (!token) { //前面加了一个！，表示token不存在，token是空的，token没有值，这个意思
        token = window.localStorage.getItem(getTokenName());
        if (token) {
            config.headers['rememberMe'] = true;
        }
    }
    if (token) { // 表示 token 存在，token不是空的，token有值，这个意思
        config.headers['Authorization'] = token;
    }
    return config;
}, (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use((response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么，拦截 token 验证的结果，进行相应的提示和页面跳转
    if (response.data.code > 900) { // code 大于900说明是token验证未通过
        //给前端用户提示，并且跳转页面
        messageConfirm(response.data.msg + "，是否重新去登录？").then(() => { // 若点击"确定"按钮，走这个
            // 既然后端验证 token 未通过，token 是非法的，删除 token 先
            removeToken();
            // 跳到登录页
            window.location.href = ("/");
        })
            // 若点击"取消"按钮，走这个
            .catch(() => {
                // 既然后端验证 token 未通过，token 是非法的，删除 token 先
                removeToken();
                // 跳到登录页
                window.location.href = ("/");
            })
        return;
    }
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});