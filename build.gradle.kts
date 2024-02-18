import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("io.ktor.plugin") version "2.3.8"
}

group = "org.example"
version = "1.0.0"
val dockerImage = "ktor-auth"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}


application {
    mainClass.set("com.example.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-netty:2.3.8")
    implementation("io.ktor:ktor-server-auth:2.3.8")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("io.ktor:ktor-server-test-host:2.3.8")
    implementation("commons-codec:commons-codec:1.16.1")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

val shadowJars = tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "com.example.ApplicationKt"
    }
    archiveClassifier.set("")
    mergeServiceFiles()
}

val dockerBuild = tasks.register<Exec>("docker") {
    commandLine(
        "sh", "-c", "docker build -t $dockerImage ."
    )
    dependsOn(shadowJars)
}
