package android_lint_reporter.github

import android_lint_reporter.model.GithubComment
import android_lint_reporter.model.GithubCommit
import android_lint_reporter.model.GithubUser
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

    @POST("repos/{username}/{repoName}/pulls/{prId}/comments")
    fun postReviewComment(
            @Path("username") username: String,
            @Path("repoName") repoName: String,
            @Path("prId") prId: String,
            @Body body: RequestBody
    ): Call<GithubCommentResponse>

    @GET("repos/{username}/{repoName}/pulls/{prId}/files")
    fun getPullRequestFiles(
            @Path("username") username: String,
            @Path("repoName") repoName: String,
            @Path("prId") prId: String
    ): Call<List<GithubPullRequestFilesResponse>>

    @GET("repos/{username}/{repoName}/pulls/{prId}/comments")
    fun getPullRequestComments(
            @Path("username") username: String,
            @Path("repoName") repoName: String,
            @Path("prId") prId: String
    ): Call<List<GithubComment>>

    @GET("repos/{username}/{repoName}/pulls/{prId}/commits?per_page=250")
    fun getPullRequestCommits(
            @Path("username") username: String,
            @Path("repoName") repoName: String,
            @Path("prId") prId: String
    ): Call<List<GithubCommit>>

    @GET("user")
    fun getUser(): Call<GithubUser>
}
