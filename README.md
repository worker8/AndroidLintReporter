# AndroidLintReporter
This is a Gradle Plugin to parse, format, report Android Lint result back to Github Pull Request using Github Actions. This is targetted for someone who's doing Android Development and using Github who wants to run lint on their pull request.

Here is how it works.


<details>
<summary>
1. When a Pull Request is created:
</summary>
<br>
<img width="1057" src="https://user-images.githubusercontent.com/1988156/77041343-13ea8380-69fd-11ea-9c94-2935aff4f542.png">
</details>

<details>
<summary>
2. Github Actions will be triggered:
</summary>
<br>
<img width="1057" src="https://user-images.githubusercontent.com/1988156/77041423-3a102380-69fd-11ea-8aa8-8026b4d1375c.png">
</details>

<details>
<summary>
3. The Github Actions will run:
</summary>
<br>
<code>
./gradlew lint && ./gradlew parseAndSendLintResult
</code>

Note: The task `parseAndSendLintResult` is provided by this plugin!
</details>

<details>
<summary>
4. This will produce this lint report and it will be reported back in the Pull Request:
</summary>
<br>
<img width="1057" src="https://user-images.githubusercontent.com/1988156/77041453-4a280300-69fd-11ea-84fa-4d41c666b219.png">
</details>

## How to Setup
There are many a couple of steps needed to setup everything.

**1. Github Actions**
First, we need to setup a a Github Action trigger to run lint and the gradle task provided by this plugin.

Add a file in `.github/workflows/run-lint.yml` in the root of your project:

```yml
name: Android Pull Request & Master CI

on:
  pull_request:
    branches:
      - 'master'
  push:
    branches:
      - 'master'

jobs:
  lint:
    name: Run Lint
    runs-on: ubuntu-18.04

    steps:
      - uses: actions/checkout@v1
      - name: setup JDK 1.8
        uses: actions/setup-java@v1
        with:
          java-version: 1.8
      - name: Run lint && parse lint-results.xml && send report to PR
        env:
          PR_NUMBER: ${{ github.event.number }}
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: |
          ./gradlew lint && ./gradlew parseAndSendLintResult -PgithubPullRequestId=$PR_NUMBER -PgithubToken=$GITHUB_TOKEN
```

<details>
<summary>
how to get GITHUB_TOKEN
</summary>
`GITHUB_TOKEN` can be obtained through these steps:
  
1. Go to Github's Settings --> Developer settings --> Generate new token.
2. Check for **Repo (all)** and **workflow**

After generating the token, paste it here:

![image](https://user-images.githubusercontent.com/1988156/77247261-a5166000-6c72-11ea-88b8-ab59c96c66e1.png)

</details>



2. In `app/build.gradle`, setup the plugin:

```
plugins {
    id('com.worker8.android_lint_reporter')
}
android_lint_reporter {
    lintFilePath = "./app/build/reports/lint-results.xml"
    githubToken = "" // obtain github personal token from Github
    githubUsername = "worker8"
    githubRepositoryName = "AndroidLintReporter"
}
```

`build.gradle.kts` (if you're using kotlin):

```kotlin
plugins {
    id('com.worker8.android_lint_reporter')
}
android_lint_reporter {
    lintFilePath = "./src/main/resources/lint-results.xml"
    githubToken = "<your personal github token>"
    githubUsername = "worker8"
    githubRepositoryName = "AndroidLintReporter"
}
```

<details>
<summary>
Github Token can be obtained by using these steps:
</summary><br>

1. Go to account settings in Github
2. Go to Developer Settings
<img width="253" alt="Screen Shot 2020-03-19 at 16 13 44" src="https://user-images.githubusercontent.com/1988156/77043730-7d6c9100-6a01-11ea-9436-bde64c9acff0.png">

3. Go to Personal Access Token, and click `Generate new token`:

<img width="1045" alt="Screen Shot 2020-03-19 at 16 14 18" src="https://user-images.githubusercontent.com/1988156/77043750-89585300-6a01-11ea-9214-735db0958aab.png">

4. It's better to make a bot account and use the token of the bot account
</details>
<br>

3. After adding this, you should be ready! Try making a pull request, and you should see the Github Actions running under "Check" tab. When it's done, you should see your lint report being posted back to your pull request.
