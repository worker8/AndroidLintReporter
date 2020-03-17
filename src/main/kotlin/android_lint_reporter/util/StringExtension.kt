package android_lint_reporter.util

fun String.escapeForJson(): String {
    return replace(System.lineSeparator(), "\\n").replace("\"", "'")
}

fun String.removeNewLine(): String {
    return replace("\n", " ")
}
