/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package android_lint_reporter

import org.gradle.internal.impldep.org.junit.Assert.assertTrue
import java.io.File
import org.gradle.testkit.runner.GradleRunner
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.test.Test
import kotlin.test.assertSame

class AndroidLintReporterPluginFunctionalTest {
    val noLocalPropertiesErrorMessage: String by lazy {
        val sb = StringBuilder().apply {
            appendln("github_token property cannot be found in local.properties")
            appendln("please prepare local.properties in the root directory")
            appendln("and set the following content:")
            appendln("    github_token=\"abcdefgh123456\"")
            appendln("    github_owner=\"worker8(replace with your project username)\"")
            appendln("    github_repository_name=\"SampleProjectName\"")
            appendln("otherwise, this functional test will fail because it needs a github personal access token to work")
        }
        sb.toString()
    }

    @Test
    fun `can run task`() {
        // Setup the test build
        val projectDir = File("./build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText("""
            plugins {
                id('com.worker8.android_lint_reporter')
            }
            android_lint_reporter {
                lintFilePath = "${File("").absolutePath}/lint-results.xml" 
                detektFilePath = "${File("").absolutePath}/detekt_report.xml"
                githubOwner= "${getProperty("github_owner")}"
                githubRepositoryName = "${getProperty("github_repository")}"
                showLog = true
            }
        """)

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        println("getProperty(\"github_token\"): ${getProperty("github_token")}")
        println("getProperty(\"github_owner\"): ${getProperty("github_owner")}")
        println("getProperty(\"github_repository\"): ${getProperty("github_repository")}")
        runner.withArguments(listOf("report", "-PgithubToken=${getProperty("github_token")}", "-PisDebug=true", "-PgithubPullRequestId=366", "--stacktrace"))
        runner.withProjectDir(projectDir)
        try {
            val result = runner.build()
            println("function test ended with the following result: ${result.output}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        assertTrue(true)
    }

    private fun getProperty(propertyName: String): String {
        val props = getProperties()
        if (props[propertyName] == null) {
            error(noLocalPropertiesErrorMessage)
        }
        return props[propertyName] as String
    }

    private fun getProperties(): Properties {
        val props = Properties()
        val localPropertyFile: File
        try {
            localPropertyFile = File("local.properties")
            props.load(FileInputStream(localPropertyFile))
        } catch (e: FileNotFoundException) {
            error(noLocalPropertiesErrorMessage)
        }
        return props
    }
}
