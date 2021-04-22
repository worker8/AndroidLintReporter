package android_lint_reporter.parser

import android_lint_reporter.model.Issue
import org.w3c.dom.Document
import java.io.File
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.NumberFormatException
import javax.xml.parsers.DocumentBuilderFactory

object Parser {
    fun parseDetektXml(xmlFile: File): List<Issue> {
        val issues = mutableListOf<Issue>()
        val document = parseDocument(xmlFile)
        val issuesNodeList = document.getElementsByTagName("checkstyle")
        if (issuesNodeList != null && issuesNodeList.length > 0) {
            val issuesElement = issuesNodeList.item(0) as Element
            val fileList = issuesElement.getElementsByTagName("file")
            for (x in 0 until fileList.length) {
                val rawFileElement = fileList.item(x) as Element
                val errors = rawFileElement.getElementsByTagName("error")
                for (i in 0 until errors.length) {
                    val errorElement = errors.item(i) as Element
                    val severity = if (errorElement.getAttribute("severity").equals("warning", true)) {
                        Issue.Type.Warning
                    } else {
                        Issue.Type.Error
                    }
                    val issue = Issue.DetektIssue(
                            type = severity,
                            line = errorElement.getAttribute("line").toInt(),
                            file = rawFileElement.getAttribute("name"),
                            message = errorElement.getAttribute("message"),
                            rule = errorElement.getAttribute("source")
                    )
                    issues.add(issue)
                }
            }
        }

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
                    val line = try {
                        locationElement.getAttribute("line")?.toInt() ?: -1
                    } catch (e: NumberFormatException) {
                        -1
                    }
                    val issue = Issue.AndroidLintIssue(
                            type = severityType,
                            line = line,
                            file = locationElement.getAttribute("file"),
                            message = element.getAttribute("message"),
                            rule = element.getAttribute("id")
                    )
                    issues.add(issue)
                }
            }
        }
        return issues
    }

    private fun parseDocument(xmlFile: File): Document {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(xmlFile)
        document.documentElement.normalize()
        return document
    }
}
