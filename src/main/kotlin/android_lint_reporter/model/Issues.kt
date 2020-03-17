package android_lint_reporter.model

data class Issues(
    val errorList: MutableList<Issue> = mutableListOf(),
    val warningList: MutableList<Issue> = mutableListOf()
) {
    fun hasErrorOnly() = errorList.isNotEmpty() && warningList.isEmpty()
    fun hasWarningOnly() = warningList.isNotEmpty() && errorList.isEmpty()
    fun hasBothErrorAndWarning() = errorList.isNotEmpty() && warningList.isNotEmpty()
    fun hasNoErrorAndWarning() = errorList.isNotEmpty() && warningList.isNotEmpty()
}
