import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.10.5"
    id("org.jetbrains.grammarkit") version "2022.3.1"
    id("checkstyle")
}

group = "org.openspg.idea"
version = "0.0.18"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation("com.alibaba:fastjson:2.0.57")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    implementation("io.opentelemetry:opentelemetry-api:1.31.0")
    implementation("io.opentelemetry:opentelemetry-context:1.31.0")

    intellijPlatform {
        intellijIdeaCommunity("2024.2")
        bundledPlugin("com.intellij.java")
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.Plugin.Java)
    }

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
}

val grammarKitRuntime: Configuration? = configurations.detachedConfiguration(
    dependencies.create("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7"),
    dependencies.create("io.opentelemetry:opentelemetry-api:1.31.0"),
    dependencies.create("io.opentelemetry:opentelemetry-context:1.31.0")
)

tasks {
    register<GenerateParserTask>("generateSchemaParser") {
        sourceFile.set(file("src/main/lexer/org/openspg/idea/schema/grammar/Schema.bnf"))
        targetRoot.set("src/main/gen")
        pathToParser.set("org/openspg/idea/schema/parser/_SchemaParser.java")
        pathToPsiRoot.set("org/openspg/idea/schema/psi")
        purgeOldFiles.set(true)
    }

    register<GenerateLexerTask>("generateSchemaLexer") {
        sourceFile.set(file("src/main/lexer/org/openspg/idea/schema/lexer/Schema.flex"))
        targetDir.set("src/main/gen/org/openspg/idea/schema/lexer")
        targetClass.set("_SchemaLexer")
        purgeOldFiles.set(true)
    }

    register<GenerateParserTask>("generateConceptRuleParser") {
        sourceFile.set(file("src/main/lexer/org/openspg/idea/conceptRule/grammar/ConceptRule.bnf"))
        targetRoot.set("src/main/gen")
        pathToParser.set("org/openspg/idea/conceptRule/parser/ConceptRuleParser.java")
        pathToPsiRoot.set("org/openspg/idea/conceptRule/psi")
        purgeOldFiles.set(true)
    }

    register<GenerateLexerTask>("generateConceptRuleLexer") {
        sourceFile.set(file("src/main/lexer/org/openspg/idea/conceptRule/lexer/ConceptRule.flex"))
        targetDir.set("src/main/gen/org/openspg/idea/conceptRule/lexer")
        targetClass.set("ConceptRuleLexer")
        purgeOldFiles.set(true)
    }

    register("generateGrammarSources") {
        group = "build"
        description = "Generate parsers and lexers from .bnf and .flex files"
        dependsOn(
            "generateSchemaParser",
            "generateSchemaLexer",
            "generateConceptRuleParser",
            "generateConceptRuleLexer"
        )
    }
}

checkstyle {
    toolVersion = "10.17.0"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxWarnings = 0
    isShowViolations = true
}

sourceSets {
    main {
        java {
            srcDir("src/main/gen")
        }
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("242")
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
    withType<GenerateParserTask>().configureEach {
        classpath = files(classpath, grammarKitRuntime)
    }

    withType<GenerateLexerTask>().configureEach {
        classpath = files(classpath, grammarKitRuntime)
    }

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    test {
        useJUnit()
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
    }

    withType<Checkstyle> {
        reports {
            xml.required.set(false)
            html.required.set(true)
        }
        exclude("**/gen/**")
    }

    named<Checkstyle>("checkstyleMain") {
        source = fileTree("src/main/java")
        include("**/*.java")
    }

    named<Checkstyle>("checkstyleTest") {
        source = fileTree("src/test/java")
        include("**/*.java")
    }
}
