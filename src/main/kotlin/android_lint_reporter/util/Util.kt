package android_lint_reporter.util

internal fun printLog(s: String) {
    println("[Android Lint Reporter] $s")
}

internal fun Set<Int>.print() {
    map { it.toString() }
            .reduce { acc, s -> "$acc, $s" }
            .padStart(1, '[')
            .padEnd(1, ']')
}
