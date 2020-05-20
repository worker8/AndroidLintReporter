

# AndroidLintReporter [![](https://img.shields.io/badge/latest-1.1.3-blue)](https://plugins.gradle.org/plugin/com.worker8.android_lint_reporter) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<img width="300" alt="Android Lint Reporter Logo" src="https://user-images.githubusercontent.com/1988156/79091365-fc968000-7d87-11ea-997d-2a0fa1f6ec5a.png">

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
./gradlew lint && ./gradlew parseAndSendLintResult -PgithubPullRequestId=<PR number> -PgithubToken=<Github Access Token>
</code>

Note: The task `parseAndSendLintResult` is provided by this plugin!
</details>

<details>
<summary>
4. This will produce this lint report and it will be reported back in the Pull Request:
  - then you can fix the lint warnings and push again
</summary>
<br>
<img width="1057" src="https://user-images.githubusercontent.com/1988156/77041453-4a280300-69fd-11ea-84fa-4d41c666b219.png">
</details>

## How to Setup
There are a couple of steps needed to setup everything.

#### 1. Github Actions

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
How to get Github Token
</summary><br>

1. Go to Github's `Settings --> Developer settings --> Generate` new token.

<img width="253" alt="Screen Shot 2020-03-19 at 16 13 44" src="https://user-images.githubusercontent.com/1988156/77043730-7d6c9100-6a01-11ea-9436-bde64c9acff0.png">

2. Go to Personal Access Token, and click `Generate new token`:
  - Check for **Repo (all)** and **workflow**
  
<img width="1045" alt="Screen Shot 2020-03-19 at 16 14 18" src="https://user-images.githubusercontent.com/1988156/77043750-89585300-6a01-11ea-9214-735db0958aab.png">

3. It's better to make a bot account and use the token of the bot account

</details>
<br>

<details>
<summary>
How to add GITHUB_TOKEN
</summary>

After generating the token, paste it under `Settings --> Secrets`:

![image](https://user-images.githubusercontent.com/1988156/77247261-a5166000-6c72-11ea-88b8-ab59c96c66e1.png)

</details>

### 2. Add repositories and classpath:

- `build.gradle`:

**Groovy**

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.worker8.android_lint_reporter:android_lint_reporter:<latest_version>"
  }
}
```

**Kotlin**

```
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("gradle.plugin.com.worker8.android_lint_reporter:android_lint_reporter:<latest_version>")
  }
}
```

Note: `latest_version` can be found here: https://plugins.gradle.org/plugin/com.worker8.android_lint_reporter

### 3. Add the plugin dependency:

- `app/build.gradle`:

**Groovy**

```
plugins {
    id "com.worker8.android_lint_reporter"
}
android_lint_reporter {
    lintFilePath = "./app/build/reports/lint-results.xml"
    githubUsername = "worker8"
    githubRepositoryName = "AndroidLintReporter"
}
```

**Kotlin**

```kotlin
plugins {
    id("com.worker8.android_lint_reporter")
}
android_lint_reporter {
    lintFilePath = "./app/build/reports/lint-results.xml"
    githubUsername = "worker8"
    githubRepositoryName = "AndroidLintReporter"
}
```

### 4. You are ready! 

Try making a pull request, and you should see the Github Actions running under "Check" tab. When it's done, you should see your lint report being posted back to your pull request.


## Development
For those who is interested to contribute or fork it. Here's a blog post I wrote explaining the source code of this repo:
https://bloggie.io/@_junrong/the-making-of-android-lint-reporter

IDE for development:
Download Intellij Community Edition for free and open this project.

To run the test, use this command:

```
./gradlew functionalTest
```

Testing Github API response using cURL:
```
curl -H "Authorization: token <GITHUB_TOKEN>" -H "Content-Type: application/json" --data '{"body":"test123abc"}' -X POST https://api.github.com/repos/<GITHUB_USER>/<GITHUB_PROJECT>/issues/<PR_OR_ISSUE_NUMBER>/comments
```

To deploy:
1. Download secrets from https://plugins.gradle.org/ after login.
2. up version in `build.gradle.kts`
3. then run `./gradlew publishPlugin`

## License

```
Copyright 2020 Tan Jun Rong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
