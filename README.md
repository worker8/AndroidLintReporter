# AndroidLintReporter
This is a Gradle Plugin to parse, format, report Android Lint result back to Github Pull Request using Github Actions.

## How it works


## How to use 
Add this in your `build.gradle`:

```
to be filled
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

