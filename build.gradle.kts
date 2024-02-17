import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("io.ktor.plugin") version "2.3.8"
    id("org.graalvm.buildtools.native") version "0.9.19"
}

group = "org.example"
version = "1.0.0"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

//kotlin {
//    jvmToolchain(11)
//}


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

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "com.example.ApplicationKt"
    }
    archiveClassifier.set("")
    mergeServiceFiles()
}

graalvmNative {
    binaries {

        named("main") {
            fallback.set(false)
            verbose.set(true)

            buildArgs.add("--initialize-at-build-time=ch.qos.logback")
            buildArgs.add("--initialize-at-build-time=io.ktor,kotlin")
            buildArgs.add("--initialize-at-build-time=org.slf4j.LoggerFactory")

            buildArgs.add("-H:+InstallExitHandlers")
            buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
            buildArgs.add("-H:+ReportExceptionStackTraces")

            imageName.set("graalvm-server")
        }

        named("test"){
            fallback.set(false)
            verbose.set(true)

            buildArgs.add("--initialize-at-build-time=ch.qos.logback")
            buildArgs.add("--initialize-at-build-time=io.ktor,kotlin")
            buildArgs.add("--initialize-at-build-time=org.slf4j.LoggerFactory")

            buildArgs.add("-H:+InstallExitHandlers")
            buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
            buildArgs.add("-H:+ReportExceptionStackTraces")

            val path = "${projectDir}/src/test/resources/META-INF/native-image/"
            buildArgs.add("-H:ReflectionConfigurationFiles=${path}reflect-config.json")
            buildArgs.add("-H:ResourceConfigurationFiles=${path}resource-config.json")

            imageName.set("graalvm-test-server")
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

}