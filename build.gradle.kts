plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.11.0"
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    kotlin("kapt") version "1.3.61"
}

repositories {
    jcenter()
}

object Constant {
    val pluginName = "AndroidLintReporterPlugin"
    val id = "com.worker8.android_lint_reporter"
    val implementationClass = "android_lint_reporter.AndroidLintReporterPlugin"
    val version = "2.1.0"
    val website = "https://github.com/worker8/AndroidLintReporter"
    val displayName = "Android Lint Reporter"
    val description = "Gradle Plugin to parse, format, report Android Lint result back to Github Pull Request using Github Actions"
    val tags = listOf("android", "lint", "github-actions")
}

object Version {
    val retrofit = "2.8.2"
    val moshi = "1.9.2"
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("com.squareup.moshi:moshi-kotlin:${Version.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Version.moshi}")

    implementation("com.squareup.retrofit2:retrofit:${Version.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Version.retrofit}")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations.getByName("functionalTestImplementation").extendsFrom(configurations.getByName("testImplementation"))

// Add a task to run the functional tests
val functionalTest by tasks.creating(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

val check by tasks.getting(Task::class) {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

gradlePlugin {
    plugins {
        create(Constant.pluginName) {
            id = Constant.id
            implementationClass = Constant.implementationClass
            version = Constant.version
        }
    }
}

pluginBundle {
    // These settings are set for the whole plugin bundle
    website = Constant.website
    vcsUrl = Constant.website
    // tags and description can be set for the whole bundle here, but can also
    // be set / overridden in the config for specific plugins
    //description = "Just a friendly description for my learning!"

    // The plugins block can contain multiple plugin entries.
    //
    // The name for each plugin block below (greetingsPlugin, goodbyePlugin)
    // does not affect the plugin configuration, but they need to be unique
    // for each plugin.

    // Plugin config blocks can set the id, displayName, version, description
    // and tags for each plugin.

    // id and displayName are mandatory.
    // If no version is set, the project version will be used.
    // If no tags or description are set, the tags or description from the
    // pluginBundle block will be used, but they must be set in one of the
    // two places.

    (plugins) {

        // first plugin
        Constant.pluginName {
            // id is captured from java-gradle-plugin configuration
            description = Constant.description
            tags = Constant.tags
            version = Constant.version
            displayName = Constant.displayName
        }
    }
}
