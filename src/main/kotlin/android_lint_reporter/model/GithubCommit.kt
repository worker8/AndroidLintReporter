package android_lint_reporter.model
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubCommit(
        val url: String,
        val sha: String
)