package android_lint_reporter.model

sealed class Issue(
        open val type: Type,
        open val line: Int?, // this is nullable, because some changes don't have line number, e.g. a .png file
        open val file: String,
        open val message: String,
        open val reportedBy: String
) {
    enum class Type {
        Warning, Error
    }

    class AndroidLintIssue(
            override val type: Type,
            override val line: Int?,
            override val file: String,
            override val message: String,
            override val reportedBy: String = "by Android Lint :robot: "
    ) : Issue(type, line, file, message, reportedBy)

    class DetektIssue(
            override val type: Type,
            override val line: Int?,
            override val file: String,
            override val message: String,
            override val reportedBy: String = "by Detekt :mag: "
    ) : Issue(type, line, file, message, reportedBy)
}