# HS-Blog 博客系统


### 📝 项目简介

hs-blog是一个线上博客平台，一个知识记录，分享平台。在这里，用户可以分享自己个人的学习笔记，经验；同时平台提供了一些电子书，以学习交流的目的供用户阅读与参考。

### 🔗 项目地址

<a href="https://github.com/ZinniaHS/hs-blog" class="github-link">https://github.com/ZinniaHS/hs-blog</a>

### 📸 整体效果

<!-- 使用GitHub Pages加载自定义样式 -->

<link rel="stylesheet" href="./.githubCSS/styles.css">


## 项目文档索引

1. [需求分析](docs/需求分析.md)
2. [架构设计](docs/架构设计.md)
3. [数据库设计](docs/数据库设计.md)
4. [实现文档](docs/实现文档.md)
5. [测试文档](docs/测试文档.md)
6. [部署文档](docs/部署文档.md)

## 开发流程说明

本项目完整遵循软件工程开发流程：

1. **需求分析**：明确功能需求和非功能需求
2. **设计阶段**：完成系统架构和数据库设计
3. **实现阶段**：采用Spring Boot技术栈实现
4. **测试阶段**：包含单元测试和集成测试
5. **部署维护**：提供完整部署方案和运维建议

## 快速开始

```bash
# 初始化数据库
mysql -uroot -p < src/main/java/com/hs/blog/sql/init.sql

# 启动应用
mvn spring-boot:run
```
