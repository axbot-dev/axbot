plugins {
    id("java")
}

group = "com.github.axiangcoding.axbot"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17


allprojects {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/public/")
        mavenCentral()
    }
}



