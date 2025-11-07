// 从 vue-router 中，导入 createRouter, createWebHistory 函数
import {createRouter, createWebHistory} from "vue-router";

// 定义变量
let router = createRouter({
    // 路由历史
    history: createWebHistory(),

    // 配置路由，是一个数组，里面可以配置多个路由
    routes: [
        {
            // 路由路径，这里设置为"/"，代表项目先进入以下vue页面
            path: "/",
            // 路由路径所对应的页面（此文件目录为：~/src/view/LoginView.vue）
            component: () => import("../view/LoginView.vue"),
            // children: [
            //     {
            //         // 子路由里面的每一个组件，都是和路由一样，path 和 component
            //         // 唯一的区别是，子路由的 path 不能带斜杠
            //         path: "index",
            //         component: () => import("../view/IndexView.vue"),
            //     },
            // ]
        },
        {
            // 路由路径
            path: "/dashboard",
            // 路由路径所对应的页面（此文件目录为：~/src/view/DashboardView.vue）
            component: () => import("../view/DashboardView.vue"),
            redirect: "/dashboard/index", // 自动跳转，默认不进入 /dashboard 页面，直接跳转到首页
            // 子路由，子路由可以是多个，所以是数组
            children: [
                {
                    path: "index",
                    component: () => import("../view/IndexView.vue"),
                },
                {
                    path: "product/category",
                    component: () => import("../view/product/CategoryView.vue"),
                },
                {
                    path: "admin/sysUsers",
                    component: () => import("../view/admin/SysUsersView.vue"),
                },
                {
                    path: "admin/sysUser/:id",
                    component: () => import("../view/admin/SysUserDetailView.vue"),
                },
                {
                    path: "admin/roles",
                    component: () => import("../view/admin/SysRolesView.vue"),
                },
                {
                    path: "admin/role/:id",
                    component: () => import("../view/admin/SysRoleDetailView.vue"),
                },
                {
                    path: "product/brand",
                    component: () => import("../view/product/BrandView.vue"),
                },
            ]
        },
        // 这里添加之后的路由，格式如上，如{}，{}，在大括号里写具体的路径对应
    ],
})
// 导出创建的路由对象
export default router;