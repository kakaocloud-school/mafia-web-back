# 마피아 게임 전적 웹사이트

마피아 게임 전적 웹사이트

## 1. 프로젝트 구성

- Java 17
- Spring Framework 6.0.16
- Gradle

## 2. 프로젝트 실행
### (1) properties
https://www.notion.so/30315b961d684a9abfbb7e6b738a7be8
### (2) redis 구성
```shell
docker run -d  -p 6379:6379 --name=redis -e TZ=Asia/Seoul redis:7.2 redis-server --requirepass 1234
```