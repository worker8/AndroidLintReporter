package android_lint_reporter.parser

import android_lint_reporter.model.Issue
import android_lint_reporter.model.Issues
import android_lint_reporter.util.escapeForJson
import android_lint_reporter.util.removeNewLine
import java.io.File

object Renderer {
    fun render(issue: Issue): String {
        val icon = when {
            issue.severity.compareTo("error", true) == 0 -> {
                "Error &nbsp; :no_entry_sign:"
            }
            issue.severity.compareTo("warning", true) == 0 -> {
                "Warning :warning: &nbsp;"
            }
            else -> {
                issue.severity
            }
        }
        return """
|  **${icon}**  |
| - |
| ${issue.message} |
\n
<p align="right">by Android Lint :robot:</p>
""".trimIndent().escapeForJson()
    }

    fun render(issues: Issues): String {
        var warningBody = """
<details><summary> <strong>Warnings (XXwarning_numberXX) </strong> </summary>\n
### Warnings :warning:\n
| File | Explanation |
| ---- | ----------- |
"""

        var errorBody = """
<details><summary> <strong>Errors (XXerror_numberXX) </strong> </summary>\n
### Errors :skull:\n
| File | Explanation |
| ---- | ----------- |
"""

        val currentPath = File("").getAbsolutePath()
        issues.warningList.forEach { issue ->
            var locationString = ""
            var errorLineString = ""
            if (issue.location.line.isNotBlank() && issue.location.column.isNotBlank()) {
                locationString = "**L${issue.location.line}:${issue.location.column}**"
                errorLineString = "`${issue.errorLine1}`"
            }
            warningBody += """| <details><summary>${
                issue.location.file.replace(
                        "${currentPath}/",
                        ""
                )
            } ${locationString}</summary> ${errorLineString}</details> | <details> <summary>${issue.summary}</summary> <br>_${issue.explanation.removeNewLine()}_</details>|\n"""
        }

        warningBody =
                warningBody.replace("XXwarning_numberXX", issues.warningList.count().toString())
        errorBody = errorBody.replace("XXerror_numberXX", issues.errorList.count().toString())
        errorBody += "</details>"
        var bodyString = ""
        if (issues.hasNoErrorAndWarning()) {
            bodyString += "There is no warnings and errors by Android Lint! All good! :)"
        } else if (issues.hasWarningOnly()) {
            bodyString += warningBody
        } else if (issues.hasErrorOnly()) {
            bodyString += errorBody
        } else if (issues.hasBothErrorAndWarning()) {
            bodyString += warningBody + "\n" + errorBody
        }
        return bodyString.escapeForJson()
    }
}
