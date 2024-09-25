plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ru.azhdankov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://artifacts.camunda.com/artifactory/public/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.camunda:zeebe-bom:8.5.5")
    implementation("org.springframework.boot:spring-boot-starter:3.3.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("io.camunda.spring:spring-boot-starter-camunda:8.5.7")
    implementation("io.grpc:grpc-stub:1.65.1")
    implementation("io.grpc:grpc-core:1.65.1")
    implementation("io.grpc:grpc-api:1.65.1")
    implementation("io.grpc:grpc-netty:1.65.1")
    implementation("io.grpc:grpc-util:1.65.1")
    implementation("io.grpc:grpc-protobuf:1.65.1")


    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.camunda:spring-zeebe-test:8.5.7")
    testImplementation("commons-io:commons-io:2.17.0")
    testImplementation("org.camunda.community.process_test_coverage:camunda-process-test-coverage-junit5-platform-8:2.7.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    useJUnitPlatform()
}
