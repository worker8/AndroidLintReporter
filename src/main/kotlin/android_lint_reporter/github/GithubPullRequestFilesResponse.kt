package android_lint_reporter.github

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubPullRequestFilesResponse(
        val sha: String,
        val filename: String,
        val status: String,
        val additions: Int,
        val deletions: Int,
        val changes: Int,
        val blob_url: String?, // this can be null for changes in submodule
        val raw_url: String?, // this can be null for changes in submodule
        val contents_url: String,
        val patch: String = ""
)
