package android_lint_reporter.parser

import android_lint_reporter.model.AndroidLintIssue
import android_lint_reporter.model.GithubIssues
import android_lint_reporter.model.Issue
import android_lint_reporter.model.Location
import java.io.File
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.Exception
import java.lang.NumberFormatException
import javax.xml.parsers.DocumentBuilderFactory

object Parser {
    fun parseDetektXml(xmlFile: File): List<Issue> {
        val issues = mutableListOf<Issue>()
        return issues.toList()
    }

    fun parse(xmlFile: File): List<Issue> {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(xmlFile)
        document.documentElement.normalize()
        val issuesNodeList = document.getElementsByTagName("issues")
        val issues = mutableListOf<Issue>()
        if (issuesNodeList != null && issuesNodeList.length > 0) {
            val issuesElement = issuesNodeList.item(0) as Element

            for (i in 0 until issuesElement.childNodes.length) {
                val child = issuesElement.childNodes.item(i)
                if (child.nodeType == Node.ELEMENT_NODE) {
                    val element = child as Element
                    val locationElement =
                            element.getElementsByTagName("location").item(0) as Element
                    val severityType = if (element.getAttribute("severity").equals("warning", true)) {
                        Issue.Type.Warning
                    } else {
                        Issue.Type.Error
                    }
                    try {
                        set.add(value.location.line.toInt())
                    } catch (e: NumberFormatException) {
                        // for image files, like asdf.png, it doesn't have lines, so it will cause NumberFormatException
                        // add -1 in that case
                        set.add(-1)
                    }
                    val line = try {
                        locationElement.getAttribute("line")?.toInt() ?: -1
                    } catch (e: NumberFormatException) {
                        -1
                    }
                    val issue = Issue.AndroidLintIssue(
                            type = severityType,
                            line = line,
                            file = locationElement.getAttribute("file"),
                            message = element.getAttribute("message")
//                            id = element.getAttribute("id"),
//                            category = element.getAttribute("category"),
//                            priority = element.getAttribute("priority"),
//                            summary = element.getAttribute("summary"),
//                            explanation = element.getAttribute("explanation"),
//                            errorLine1 = element.getAttribute("errorLine1"),
//                            errorLine2 = element.getAttribute("errorLine2"),
//                            column = locationElement.getAttribute("column")
                    )
                    issues.add(issue)
//                    if (issue.severity == "Warning") {
//                        issues.warningList.add(issue)
//                    } else if (issue.severity == "Error") {
//                        issues.errorList.add(issue)
//                    }
                }
            }
        }
        return issues
    }
}
