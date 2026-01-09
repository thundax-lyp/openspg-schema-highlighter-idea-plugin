
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.10.0"
}

group = "org.openspg.idea"
version = "0.0.17"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation("com.alibaba:fastjson:2.0.57")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    intellijPlatform {
        intellijIdeaCommunity("2023.1")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("231")
//            untilBuild.set("252.*")
        }
    }

    signing {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    test {
        useJUnitPlatform()
    }
}
