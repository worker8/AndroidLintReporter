package android_lint_reporter.github

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GithubService(
    val service: GithubServiceInterface,
    val username: String,
    val repoName: String,
    val pullRequestId: String
) {
    fun postComment(bodyString: String): Call<GithubCommentResponse> {
        val requestBody = RequestBody.create(
            okhttp3.MediaType.parse("application/json"),
            "{\"body\":\"${bodyString}\"}"
        )
        return service.postComment(username, repoName, pullRequestId, requestBody)
    }

    companion object {
        const val GithubApiBaseUrl = "https://api.github.com"
        fun create(
            githubToken: String,
            username: String,
            repoName: String,
            pullRequestId: String
        ): GithubService {
            val interceptor = Interceptor { chain ->
                val newRequest =
                    chain.request().newBuilder().addHeader(
                        "Authorization",
                        "token ${githubToken}"
                    )
                        .build()
                chain.proceed(newRequest)
            }
            val okHttpClient = okhttp3.OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(GithubApiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
            return GithubService(
                service = retrofit.create(GithubServiceInterface::class.java),
                username = username,
                repoName = repoName,
                pullRequestId = pullRequestId
            )
        }
    }
}
