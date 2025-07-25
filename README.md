# Spring Boot Blog REST API

A simple Blog REST API using SpringBoot.

## Prerequisites
* JDK 24
* Docker and Docker Compose
* Your favourite IDE (Recommended: [IntelliJ IDEA](https://www.jetbrains.com/idea/))

Install JDK using [SDKMAN](https://sdkman.io/)

```shell
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk install java 24.0.1-tem
$ sdk install maven
```

## How to?

```shell
# Clone the repository
$ git clone https://github.com/sivaprasadreddy/spring-boot-blog-api.git
$ cd spring-boot-blog-api/blog

# Run tests
$ ./mvnw test

# Automatically format code using spotless-maven-plugin
$ ./mvnw spotless:apply

# Run/Debug application from IDE
Run `src/main/java/com/sivalabs/blog/BlogApiApplication.java` from IDE.

# Run application using Maven
./mvnw spring-boot:run
```

* Application: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui/index.html

## Generating certs

```shell
# create rsa key pair
openssl genrsa -out keypair.pem 2048

# extract public key
openssl rsa -in keypair.pem -pubout -out public.pem

# create private key in PKCS#8 format
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```
