# Twitter Character Count SDK

[![](https://jitpack.io/v/MinaSafwat2411/twitter_character_count.svg)](https://jitpack.io/#MinaSafwat2411/twitter_character_count)

A specialized Android Library for counting Twitter characters according to the official Twitter text rules.

## Installation

### 1. Add the JitPack repository to your build file
Add it in your `settings.gradle.kts` at the end of repositories:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add the dependency
Add the library to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.MinaSafwat2411:twitter_character_count:1.0.1")
}
```

## Usage

### TwitterComposerScreen
The main entry point for the UI component:

```kotlin
TwitterComposerScreen(
    token = "your_twitter_api_token",
    maxLimit = 280,
    lang = "en", // Supports "en" and "ar"
    onBack = { /* handle back navigation */ }
)
```

## Features
- Real-time character count using `twitter-text`.
- Support for Arabic and English localizations.
- RTL support.
- Custom colors and icons for the Top Bar.
- Integrated "Post Tweet" functionality.
- "Copy Text" and "Clear Text" actions.
