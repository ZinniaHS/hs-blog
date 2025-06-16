# 指定基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制JAR包到镜像
COPY ./backend/hs-blog-0.0.1-SNAPSHOT.jar /app/app.jar

# 设置时区（避免日志时间错乱）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime

# 声明暴露端口（与Spring Boot端口一致）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]