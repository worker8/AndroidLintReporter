package android_lint_reporter.parser

import android_lint_reporter.model.Issue
import android_lint_reporter.model.Issues
import android_lint_reporter.model.Location
import java.io.File
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

object Parser {
    fun parse(file: File): Issues {
        val xmlFile = file
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(xmlFile)
        document.getDocumentElement().normalize()
        val issuesNodeList = document.getElementsByTagName("issues")
        val issues = Issues()
        if (issuesNodeList != null && issuesNodeList.length > 0) {
            val issuesElement = issuesNodeList.item(0) as Element

            for (i in 0 until issuesElement.childNodes.length) {
                val child = issuesElement.childNodes.item(i)
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    val element = child as Element
                    val locationElement =
                        element.getElementsByTagName("location").item(0) as Element

                    val issue = Issue(
                        id = element.getAttribute("id"),
                        severity = element.getAttribute("severity"),
                        message = element.getAttribute("message"),
                        category = element.getAttribute("category"),
                        priority = element.getAttribute("priority"),
                        summary = element.getAttribute("summary"),
                        explanation = element.getAttribute("explanation"),
                        errorLine1 = element.getAttribute("errorLine1"),
                        errorLine2 = element.getAttribute("errorLine2"),
                        location = Location(
                            file = locationElement.getAttribute("file"),
                            line = locationElement.getAttribute("line"),
                            column = locationElement.getAttribute("column")
                        )
                    )
                    if (issue.severity == "Warning") {
                        issues.warningList.add(issue)
                    } else if (issue.severity == "Error") {
                        issues.errorList.add(issue)
                    }
                }
            }
        }
        return issues
    }
}
