# kotlin-grpc-spring-boot-starter
kotlin-grpc using spring boot

### Requirements ###

* Spring Boot 2.7.5 + webflux
* Java 15 (feat.corretto-15)
* Grpc 1.53.0
* Protobuf 3.22.0
* Grpc Kotlin 1.3.0
* Gradle 7.4.2

### Building the artifact ###

```
gradle build
```

### Running the application from command line ###

```
gradle bootRun
````

### MongoDB container env ###

#### docker-compose(require) ####

```
기본 파일 (-d 백그라운드 실행시) 
docker-compose up -d
```
### Grpc Test Url ###

```
kotlin/pe/grpc/http/findOne.http

GRPC localhost:9090/pe.grpc.BannerService/FindOne
```
