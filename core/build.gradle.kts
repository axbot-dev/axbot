plugins {
    id("java")
}

group = "com.github.axiangcoding.axbot"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17


allprojects {
    repositories {
        // maven(url = "https://maven.aliyun.com/repository/public/")
        mavenLocal()
        maven(url = "https://repo1.maven.org/maven2/")
        mavenCentral()
    }
}



