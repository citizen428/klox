plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "net.citizen428"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.run.configure {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
    compilerOptions {
        freeCompilerArgs.add("-Xwhen-guards")
    }
}

application {
    mainClass = "net.citizen428.klox.Lox"
}
