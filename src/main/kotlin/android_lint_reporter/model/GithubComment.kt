package android_lint_reporter.model
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubComment(
        val url: String,
        val pull_request_review_id: Long?,
        val id: Long,
        val node_id: String?,
        val diff_hunk: String?,
        val path: String,
        val position: Int?,
        val original_position: Int?,
        val commit_id: String?,
        val original_commit_id: String?,
        val body: String,
        val created_at: String?,
        val updated_at: String?,
        val html_url: String?,
        val pull_request_url: String?,
        val author_association: String?,
        val user: GithubUser,
        val start_line: Int?,
        val original_start_line: Int?,
        val start_side: Int?,
        val line: Int?,
        val original_line: Int?,
        val side: String
        // val links: ... not used ...
)