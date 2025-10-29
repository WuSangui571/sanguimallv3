import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
    plugins: [vue()],
    server: {
        // 设置访问的 ip 地址
        host: '0.0.0.0',
        // 设置前端 Vue 服务启动端口
        port: 8080,
        // 设置为 true 代表服务启动时自动打开浏览器
        open: true,
    }
})