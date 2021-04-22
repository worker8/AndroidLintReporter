

# AndroidLintReporter [![](https://img.shields.io/badge/latest-2.1.0-blue)](https://plugins.gradle.org/plugin/com.worker8.android_lint_reporter) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<img width="300" alt="Android Lint Reporter Logo" src="https://user-images.githubusercontent.com/1988156/79091365-fc968000-7d87-11ea-997d-2a0fa1f6ec5a.png">

This is a Gradle Plugin to report Android Lint and Detekt result back to Github Pull Request. This is targeted for someone who's doing Android Development and using Github who wants to run Android Lint and Detekt on their pull request.
(Currently, this plugin assumes the usage of both Android Lint and Detekt)

Here is how it works using Github Actions (it can be used in other CI as well).

<details>
<summary>
( 1 ) When a Pull Request is created, a Github Actions will be triggered:
</summary>
<br>
<img width="1057" src="https://user-images.githubusercontent.com/1988156/115521839-83006e80-a2c6-11eb-9960-53fe714cbfee.png"/>
</details>
<br>
<br>
<details>
<summary>
( 2 ) This will produce this lint report, and it will be reported back in the Pull Request:

  - then you can fix the lint warnings and push again
</summary>

<img width="1057" src="https://user-images.githubusercontent.com/1988156/114693261-6313fc80-9d54-11eb-8dda-431c5adf9ca5.png"/>
<br>
<img width="1057" src="https://user-images.githubusercontent.com/1988156/115523163-c27b8a80-a2c7-11eb-99f9-ff7c108863c8.png"/>
</details>

## How to Setup
There are a couple of steps needed to set up everything.

#### 1. Github Actions

First, we need to set up a Github Action trigger to run Detekt and Android Lint.

Add a file in `.github/workflows/run-lint.yml` (you can name it anything) in the root of your project:

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
      - name: Run detekt
        run: ./gradlew detekt
        continue-on-error: true
      - name: Run Android Lint
        run: ./gradlew lint
      - name: Run Android Lint Reporter to report Lint and Detekt result to PR 
        env:
          PR_NUMBER: ${{ github.event.number }}
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: |
          ./gradlew report -PgithubPullRequestId=$PR_NUMBER -PgithubToken=$GITHUB_TOKEN
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
    githubOwner = "worker8"
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
    detekt
    githubOwner = "worker8"
    githubRepositoryName = "AndroidLintReporter"
}
```

### 4. You are ready! 

Try making a pull request, and you should see the Github Actions running under "Check" tab. When it's done, you should see your lint report being posted back to your pull request.


## Development

For those who is interested to contribute or fork it. Here's a blog post I wrote explaining the source code of this repo:
https://bloggie.io/@_junrong/the-making-of-android-lint-reporter

**Suggested IDE for development:**

Download Intellij Community Edition for free and open this project.

**Setup Github Personal Access Token(PAT)**

Prepare a file `local.properties` based on [local.properties.TEMPLATE](https://github.com/worker8/AndroidLintReporter/blob/master/local.properties.TEMPLATE) in the root directory of this project, with the following content:

```
github_token=<replace with your Github Personal Access Token (PAT)>
github_owner=<replace with your Github Owner Name>
github_repository=<replace with your Github Repository Name>
```

You can test your Github API using your PAT using cURL:
```
curl -H "Authorization: token <github_token>" -H "Content-Type: application/json" --data '{"body":"test123abc"}' -X POST https://api.github.com/repos/<github_token>/<GITHUB_PROJECT>/issues/<PR_OR_ISSUE_NUMBER>/comments
```

**run Functional Test**
The gist of this plugin is located in the class of `AndroidLintReporterPlugin.kt`.
After setting everything up, you can run the test using this command:

```
./gradlew functionalTest
```

To deploy:
1. Download secrets from https://plugins.gradle.org/ after logging in.
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
