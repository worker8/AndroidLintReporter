package android_lint_reporter.github

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface GithubServiceInterface {
    @POST("repos/{username}/{repoName}/issues/{prId}/comments")
    fun postComment(
        @Path("username") username: String,
        @Path("repoName") repoName: String,
        @Path("prId") prId: String,
        @Body body: RequestBody
    ): Call<GithubCommentResponse>
}
