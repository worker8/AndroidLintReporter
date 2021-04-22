package android_lint_reporter.parser

import android_lint_reporter.model.Issue
import android_lint_reporter.model.Issue.AndroidLintIssue
import android_lint_reporter.model.Issue.DetektIssue
import android_lint_reporter.util.escapeForJson
import android_lint_reporter.util.removeNewLine
import java.io.File

object Renderer {
    fun render(issue: Issue): String {
        val icon = when (issue.type) {
            Issue.Type.Error -> {
                "Error &nbsp; :no_entry_sign:"
            }
            Issue.Type.Warning -> {
                "Warning :warning: &nbsp;"
            }
        }
        return """
| |  **${icon}**  |
| :--- | :--- |
| :books: | ${issue.message} |
| :hammer_and_wrench: | `${issue.rule}` |
\n
<p align="right">${issue.reportedBy}</p>
""".trimIndent().escapeForJson()
    }

//    fun renderTable(githubIssues: GithubIssues): String {
//        var warningBody = """
//<details><summary> <strong>Warnings (XXwarning_numberXX) </strong> </summary>\n
//### Warnings :warning:\n
//| File | Explanation |
//| ---- | ----------- |
//"""
//
//        var errorBody = """
//<details><summary> <strong>Errors (XXerror_numberXX) </strong> </summary>\n
//### Errors :skull:\n
//| File | Explanation |
//| ---- | ----------- |
//"""
//
//        val currentPath = File("").getAbsolutePath()
//        githubIssues.warningList.forEach { issue ->
//            var locationString = ""
//            var errorLineString = ""
//            if (issue.location.line.isNotBlank() && issue.location.column.isNotBlank()) {
//                locationString = "**L${issue.location.line}:${issue.location.column}**"
//                errorLineString = "`${issue.errorLine1}`"
//            }
//            warningBody += """| <details><summary>${
//                issue.location.file.replace(
//                        "${currentPath}/",
//                        ""
//                )
//            } ${locationString}</summary> ${errorLineString}</details> | <details> <summary>${issue.summary}</summary> <br>_${issue.explanation.removeNewLine()}_</details>|\n"""
//        }
//
//        warningBody =
//                warningBody.replace("XXwarning_numberXX", githubIssues.warningList.count().toString())
//        errorBody = errorBody.replace("XXerror_numberXX", githubIssues.errorList.count().toString())
//        errorBody += "</details>"
//        var bodyString = ""
//        if (githubIssues.hasNoErrorAndWarning()) {
//            bodyString += "There is no warnings and errors by Android Lint! All good! :)"
//        } else if (githubIssues.hasWarningOnly()) {
//            bodyString += warningBody
//        } else if (githubIssues.hasErrorOnly()) {
//            bodyString += errorBody
//        } else if (githubIssues.hasBothErrorAndWarning()) {
//            bodyString += warningBody + "\n" + errorBody
//        }
//        return bodyString.escapeForJson()
//    }
}
