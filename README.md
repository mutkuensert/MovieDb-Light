# Moviedb Light

A modern Android application following a clean architecture approach with a structured multimodule
organization. This project provides a solid foundation for building scalable and maintainable
Android applications.

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [Custom Gradle Tasks](#custom-gradle-tasks)
- [Precompiled Script Plugins](#precompiled-script-plugins)
- [Dependency Management](#dependency-management)
- [Network Layer](#network-layer)
- [Getting Started](#getting-started)

## Architecture Overview

This project implements a clean architecture approach with a multimodule structure organized by
features. The application is divided into the following main module types:

- **App**: The main application module that connects all the features
- **Core**: Contains shared functionality across features
- **Feature**: Feature-specific modules divided into data, domain, and presentation layers

Each feature follows the principles of clean architecture with:

- **Presentation Layer**: Contains UI components, ViewModels, and state management
- **Domain Layer**: Contains business logic, use cases, and entity models
- **Data Layer**: Handles data operations, repositories, and external service interactions

```mermaid
graph TD

    App[app]
    Feature-injection[feature-injection]
    Libraries[libraries]

    subgraph Core[Core]
        C1[core-database]
        C2[core-data]
        C3[core-domain]
        C4[core-ui]
    end

    subgraph Feature[feature]
        F1[feature-data]
        F2[feature-domain]
        F3[feature-presentation]
    end

%% Libraries module dependencies
    Feature --> Libraries
    Core --> Libraries
    App --> Libraries

%% Core module dependencies
    C2 --> C1
    C2 --> C3

%% Feature module dependencies
    F1 --> C2
    F2 --> C3
    F3 --> C4
    F1 --> F2
    F3 --> F2

%% feature-injection dependencies
    Feature-injection --> Core
    Feature-injection --> Feature
    Feature-injection --> Libraries

%% App dependencies
    App --> Core
    App --> Feature-injection
    App --> F3

```

## Module Structure

### Core Modules

Core modules contain functionality shared across multiple features:

- **core:data**: Network, database access, and common data utilities
- **core:database**: Database setup, DAOs, and entities
- **core:domain**: Common domain
- **core:feature-injection**: Dependency injection setup module for feature modules
- **core:ui**: Common UI components, themes, and navigation utilities

### Feature Modules

Each feature is isolated in its own module group with three sub-modules:

- **feature:[feature-name]:data**: Implements repositories, network services, and data sources
- **feature:[feature-name]:domain**: Contains business logic, repository interfaces use cases
- **feature:[feature-name]:presentation**: UI components, ViewModels, and UI states

### Module Dependency

Dependency injection is used for architecture. That means presentation and data modules depend
on domain modules. core.data module also depends on database.

## Custom Gradle Tasks

The project includes custom Gradle tasks to automate the creation of new modules.

### Creating a Core Module

To create a new core module, run:

```bash
./gradlew createCoreModule -PmoduleName=yourmodulename
```

This task:

- Creates a new core module with the specified name
- Sets up the necessary directory structure
- Creates a basic build.gradle.kts file
- Updates settings.gradle.kts to include the new module

If no module name is specified, it defaults to "newmodule":

```bash
./gradlew createCoreModule
```

### Creating a Feature Module

To create a new feature module with data, domain, and presentation layers, run:

```bash
./gradlew createFeatureModule -PfeatureName=yourfeaturename
```

This task:

- Creates a new feature module with data, domain, and presentation sub-modules
- Sets up the necessary directory structure for each sub-module
- Creates build.gradle.kts files with appropriate dependencies
- Updates settings.gradle.kts to include all the new modules

If no feature name is specified, it defaults to "newfeature":

```bash
./gradlew createFeatureModule
```

## Precompiled Script Plugins

The project uses precompiled script plugins in the buildSrc directory to share common build
configurations across modules.

### [base-library.gradle.kts](./buildSrc/src/main/kotlin/base-library.gradle.kts)

This plugin configures basic Android library modules:

```kotlin
plugins {
    id("base-library")
}
```

### [base-presentation.gradle.kts](./buildSrc/src/main/kotlin/base-presentation.gradle.kts)

```kotlin
plugins {
    id("base-presentation")
}
```

### [base-data.gradle.kts](./buildSrc/src/main/kotlin/base-data.gradle.kts)

```kotlin
plugins {
    id("base-data")
}
```

### [base-domain.gradle.kts](./buildSrc/src/main/kotlin/base-domain.gradle.kts)

```kotlin
plugins {
    id("base-domain")
}
```

## Dependency Management

Dependency management is centralized in the buildSrc directory using Kotlin DSL.

### Structure

- **ProjectConfigs.kt**: Contains project-level configurations (SDK versions, app ID, etc.)
- **DependencyGroups.kt**: Organizes dependencies into logical groups
- **ProjectExt.kt**: Extension functions for dependency declarations

### Library Versions

Dependencies are declared in the [gradle/libs.versions.toml](./gradle/libs.versions.toml) file,
which maintains a centralized list of library versions. This ensures consistent versions across all
modules and makes updates easier.

### Dependency Groups

The project defines dependency groups that can be applied together:

```kotlin
// Apply all base dependencies
dependencies {
    base()
}

// Apply Android-specific dependencies
dependencies {
    baseAndroid()
}

// Apply Compose-related dependencies
dependencies {
    compose()
}
//etc....
```

For example, the `base()` function
in [DependencyGroups.kt](./buildSrc/src/main/kotlin/DependencyGroups.kt) adds:

- Koin for dependency injection
- Timber for logging
- Kotlin Result for functional error handling

## Network Layer

### ResultCallAdapterFactory

The project includes a custom Retrofit CallAdapter that transforms API responses into a
`Result<T, Failure>` type using the kotlin-result library. This provides a cleaner way to handle
network responses and errors.

#### How It Works

1. **ResultCallAdapterFactory**: Creates a custom CallAdapter for Retrofit that handles API
   responses.
3. **ResultCall**: Custom Call implementation that transforms responses into Result.

The adapter handles different types of errors:

- HTTP error codes (4xx, 5xx)
- Network failures
- SSL errors
- Parsing errors

Each error is transformed into a user-friendly message using
the [StrResources](./libraries/src/main/kotlin/libraries/StrResources.kt).

#### Creating and Using a Service

NetworkResult is a typealias Result<T, Failure>

1. Define your API service interface:

```kotlin
interface MyService {
    @GET("endpoint")
    suspend fun getData(): NetworkResult<ResponseDto>
}
```

2. Create the service instance using Retrofit with
   the [ResultCallAdapterFactory](core/data/src/main/java/core/data/network/ResultCallAdapterFactory.kt) (
   typically in a Koin module):

```kotlin
single {
    get<Retrofit>().create(MyService::class.java)
}
```

3. Use the service in your repository:

```kotlin
class MyRepositoryImpl(
    private val service: MyService
) : MyRepository {
    override suspend fun getData(): Result<DomainModel, Failure> {
        return service.getData().map {
            it.toDomainModel()
        }
    }
}
```

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- JDK 17
- API key for TMDB (The Movie Database) set as an environment variable:
  ```
  API_KEY_TMDB=your_api_key
  ```

This structured approach ensures a clean separation of concerns and makes your codebase more
maintainable and testable.
