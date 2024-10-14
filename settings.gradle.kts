pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*") // Include all Android plugins
                includeGroupByRegex("com\\.google.*") // Include all Google plugins
                includeGroupByRegex("androidx.*") // Include all AndroidX plugins
            }
        }
        mavenCentral() // Central repository for other dependencies
        gradlePluginPortal() // Repository for Gradle plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Fail if project repos are used
    repositories {
        google() // Google's repository for Android dependencies
        mavenCentral() // Central repository for other dependencies
    }
}

rootProject.name = "salary.cal" // Set the root project name
include(":app") // Include the app module
