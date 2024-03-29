plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.github.axiangcoding"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenLocal()
    // maven(url = "https://maven.aliyun.com/repository/public/")
    maven(url = "https://repo1.maven.org/maven2/")
    mavenCentral()
    // maven {
    //     url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    //     mavenContent {
    //         snapshotsOnly()
    //     }
    // }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.session:spring-session-data-redis")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    // https://mvnrepository.com/artifact/com.alibaba.fastjson2/fastjson2
    implementation("com.alibaba.fastjson2:fastjson2:2.0.43")
    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    implementation("commons-codec:commons-codec:1.16.0")
    // https://mvnrepository.com/artifact/net.javacrumbs.shedlock/shedlock-spring
    implementation("net.javacrumbs.shedlock:shedlock-spring:5.10.2")
    // https://mvnrepository.com/artifact/net.javacrumbs.shedlock/shedlock-provider-redis-spring
    implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:5.10.2")
    // https://mvnrepository.com/artifact/love.forte.simbot.boot/simboot-core-spring-boot-starter
    implementation("love.forte.simbot.boot:simboot-core-spring-boot-starter:3.3.0")
    // https://mvnrepository.com/artifact/love.forte.simbot.component/simbot-component-kook-core
    implementation("love.forte.simbot.component:simbot-component-kook-core:3.2.0.0-alpha.8")
    // https://mvnrepository.com/artifact/love.forte.simbot.component/simbot-component-qq-guild-core
    implementation("love.forte.simbot.component:simbot-component-qq-guild-core:3.2.0.0")
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.17.1")
    // https://mvnrepository.com/artifact/io.opentelemetry.instrumentation/opentelemetry-instrumentation-annotations
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.32.0")

    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/adapter-rxjava2
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // https://mvnrepository.com/artifact/com.qiniu/qiniu-java-sdk
    implementation("com.qiniu:qiniu-java-sdk:7.14.0")
    // https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
    implementation("org.apache.commons:commons-pool2:2.12.0")
    // https://mvnrepository.com/artifact/org.ocpsoft.prettytime/prettytime
    implementation("org.ocpsoft.prettytime:prettytime:5.0.7.Final")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
