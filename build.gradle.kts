import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.1.9.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("idea")
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
    id("com.palantir.docker") version "0.22.1"
}

group = "com.teheidoma"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

task<Copy>("unpack") {
    val bootJar = tasks.getByName<BootJar>("bootJar")
    dependsOn(bootJar)
    from(zipTree(bootJar.outputs.files.singleFile))
    into("build/dependency")
}

docker {
    val archiveBaseName = tasks.getByName<BootJar>("bootJar").archiveBaseName.get()
    name = "${project.group}/$archiveBaseName"
    copySpec.from(tasks.getByName<Copy>("unpack").outputs).into("dependency")
    buildArgs(mapOf("DEPENDENCY" to "dependency"))
}
