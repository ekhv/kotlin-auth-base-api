plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}


application {
    mainClass.set("io.ktor.server.netty.EngineMain")
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
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}