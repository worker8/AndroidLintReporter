package android_lint_reporter.parser

import android_lint_reporter.model.Issues
import android_lint_reporter.util.escapeForJson
import android_lint_reporter.util.removeNewLine
import java.io.File

object Renderer {
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
            warningBody += """| <details><summary>${issue.location.file.replace(
                "${currentPath}/",
                ""
            )} **L${issue.location.line}:${issue.location.column}**</summary> `${issue.errorLine1}`</details> | <details> <summary>${issue.message}</summary> <br>_${issue.explanation.removeNewLine()}_</details>|\n"""
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
