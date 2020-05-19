package android_lint_reporter.github

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubCommentResponse(val id: Long, val url: String)
