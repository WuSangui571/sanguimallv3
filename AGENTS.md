# Repository Guidelines

## 项目结构
- `common`：公共依赖与基础设施（Lombok、MapStruct、通用组件），被各服务复用。
- `gateway`：Spring Cloud Gateway 入口，负责路由与服务发现。
- `services`：微服务集合，包含 `service-admin`（后台管理）、`service-product`（商品/SPU-SKU）、`service-third-party`（OSS/Redis/Nacos 等第三方接入）。
- `sanguimall-admin-front`：Vite + Vue3 管理端前台，静态资源在 `public`，业务代码在 `src`。
- 配置主要放在各服务 `src/main/resources/application[-*].yml`，不要提交真实密钥。

## 构建与启动
```bash
# 后端聚合构建（含公共模块）
mvn -pl common,gateway,services -am clean install
# 分别本地启动
mvn -pl gateway spring-boot:run
mvn -pl services/service-admin spring-boot:run
mvn -pl services/service-product spring-boot:run
mvn -pl services/service-third-party spring-boot:run
# 前端
cd sanguimall-admin-front && npm install && npm run dev
```
启动前请准备好 MySQL、Redis、Nacos 并配置对应地址。

## 代码风格与命名
- Java 21，4 空格缩进，UTF-8。优先构造器注入，`@Slf4j` 统一日志，避免字段注入与通配符导入。
- 包路径 `com.sangui.*`；MapStruct 映射器命名 `*Mapper`，请求/响应体放 `vo`/`dto` 包。
- 前端：使用 `<script setup>`，视图文件用短横线命名，例如 `src/views/product-list.vue`，可复用逻辑放 `src/hooks`。

## 测试规范
- 后端依赖 `spring-boot-starter-test` 与 MyBatis 相关夹具，示例：`mvn -pl services/service-product test`。
- 推荐针对 service/mapper 做切片或集成测试，外部调用（OSS、Redis、Nacos）用 Mock/本地替身。
- 测试类命名 `*Test`，放在 `src/test/java`，与主代码包结构对应。

## 提交与 PR
- 当前提交格式：`YYYY-MM-DD-HH:MM-简述`，如 `2025-11-16-19:17-fix relation bug`。
- 每次提交聚焦单一改动并注明涉及模块（如 gateway、service-product）。
- PR 需包含：变更概要、依赖的启动条件（DB/Redis/Nacos）、测试结果；前端改动附主要界面截图。

## 配置与安全
- 数据库/OSS/令牌等敏感信息务必使用环境变量或本地私有配置，不要提交到仓库。
- Nacos 服务发现与网关路由需要对齐服务 ID，修改注册名时同步更新网关配置。
- 变更表结构或缓存策略时，在 PR 描述中注明并提供初始化/迁移脚本。
