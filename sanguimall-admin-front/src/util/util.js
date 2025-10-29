import {ElMessage, ElMessageBox} from "element-plus";

/**
 * 消息提示工具方法
 * @param msg 消息框的提示信息
 * @param type 消息框的类型，可选 "error"|"success"|"warning"|"primary"
 */
export function messageTip(msg,type){
    // messageTip 函数
    ElMessage({
        // true 代表提示信息可被关闭
        showClose: true,
        // true 代表显示居中
        center: true,
        // 设置提示信息的消失时间（单位：ms）
        duration: 3000,
        // 示例 msg 的值：'登录成功，欢迎回来！',
        message: msg,
        // 示例 type 的值：可选 "error"|"success"|"warning"|"primary"
        type: type,
    })
}

/**
 * 返回存储在 Session Storage 或 Local Storage 中的 token（jwt） 名字
 * @returns {string}
 */
export function getTokenName(){
    return "dlyk_token";
}

/**
 * 从本地存储和会话存储中移除 token。
 * 此函数调用 getTokenName() 来获取 token 的键名，然后删除两种存储（localStorage 和 sessionStorage）中的对应条目。
 *
 * @return {void} 此函数不返回任何值
 */
export function removeToken(){
    window.localStorage.removeItem(getTokenName());
    window.sessionStorage.removeItem(getTokenName());
}

/**
 * 消息确认函数
 * @param msg 消息信息详情
 * @param title 消息标题
 * @returns {Promise<MessageBoxData>}
 */
export function messageConfirm(msg,title){
    return ElMessageBox.confirm(
        msg,
        title,
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
}

/**
 * 返回方法，添加路由之后方可使用
 */
export function goBack(){
    this.$router.go(-1);
}

/**
 * 获取 token 的方法
 * @returns {string}
 */
export function getToken() {
    let token = window.sessionStorage.getItem(getTokenName());
    if (!token) { //前面加了一个！，表示token不存在，token是空的，token没有值，这个意思
        token = window.localStorage.getItem(getTokenName());
    }
    if (token) { // 表示 token 存在，token不是空的，token有值，这个意思
        return token;
    } else {
        messageConfirm("请求 token 为空，是否重新去登录？").then(() => { // 若点击"确定"按钮，走这个
            // 既然后端验证 token 未通过，token 是非法的，删除 token 先
            removeToken();
            // 跳到登录页
            window.location.href = ("/");
        })
            // 若点击"取消"按钮，走这个
            .catch(() => {
                messageTip("已取消去登录！", "warning")
            })
    }
}