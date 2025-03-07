# Moviedb Light

A modern Android application following a clean architecture approach with a structured multimodule organization. This project provides a solid foundation for building scalable and maintainable Android applications.

## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [Custom Gradle Tasks](#custom-gradle-tasks)
- [Precompiled Script Plugins](#precompiled-script-plugins)
- [Dependency Management](#dependency-management)
- [Network Layer](#network-layer)
- [Dependency Injection](#dependency-injection)
- [Getting Started](#getting-started)

## Architecture Overview

This project implements a clean architecture approach with a multimodule structure organized by features. The application is divided into the following main module types:

- **App**: The main application module that connects all the features
- **Core**: Contains shared functionality across features
- **Feature**: Feature-specific modules divided into data, domain, and presentation layers

Each feature follows the principles of clean architecture with:
- **Presentation Layer**: Contains UI components, ViewModels, and state management
- **Domain Layer**: Contains business logic, use cases, and entity models
- **Data Layer**: Handles data operations, repositories, and external service interactions

## Module Structure

### Core Modules

Core modules contain functionality shared across multiple features:

- **core:data**: Network, database access, and common data utilities
- **core:database**: Database setup, DAOs, and entities
- **core:injection**: Dependency injection setup using Koin
- **core:ui**: Common UI components, themes, and navigation utilities

### Feature Modules

Each feature is isolated in its own module group with three sub-modules:

- **feature:[feature-name]:data**: Implements repositories, network services, and data sources
- **feature:[feature-name]:domain**: Contains business logic, entities, and use cases
- **feature:[feature-name]:presentation**: UI components, ViewModels, and UI states

### Module Dependency

Core module dependencies:
- **core:data** depends on **core:database**
- **core:ui** is independent
- **core:injection** depends on all other modules to provide dependency injection

Feature module dependencies:
- **feature:[name]:data** depends on **feature:[name]:domain** and **core:data**
- **feature:[name]:domain** is independent
- **feature:[name]:presentation** depends on **feature:[name]:domain** and **core:ui**

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

The project uses precompiled script plugins in the buildSrc directory to share common build configurations across modules.

### base-library.gradle.kts

This plugin configures basic Android library modules:

```kotlin
plugins {
    id("base-library")
}
```

It applies:
- Android library plugin
- Kotlin Android plugin
- Common Android configurations (SDK versions, JVM target, etc.)
- Base dependencies

### base-feature.gradle.kts

This plugin configures feature presentation modules with Compose support:

```kotlin
plugins {
    id("base-feature")
}
```

It applies:
- Android library plugin
- Kotlin Android plugin
- Compose plugin
- UI-related dependencies
- Core UI module dependency

### base-data.gradle.kts

This plugin configures data modules with network support:

```kotlin
plugins {
    id("base-data")
}
```

It applies:
- base-library plugin
- Kotlinx Serialization plugin
- Network-related dependencies
- Core data module dependency

## Dependency Management

Dependency management is centralized in the buildSrc directory using Kotlin DSL.

### Structure

- **ProjectConfigs.kt**: Contains project-level configurations (SDK versions, app ID, etc.)
- **Libraries.kt**: Provides access to all library dependencies
- **DependencyGroups.kt**: Organizes dependencies into logical groups
- **ProjectExt.kt**: Extension functions for dependency declarations

### Library Versions

Dependencies are declared in the `gradle/libs.versions.toml` file, which maintains a centralized list of library versions. This ensures consistent versions across all modules and makes updates easier.

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

// Apply coroutines-related dependencies
dependencies {
    coroutines()
}

// Apply unit test dependencies
dependencies {
    unitTest()
}
```

For example, the `base()` function in DependencyGroups.kt adds:
- Koin for dependency injection
- Timber for logging
- Kotlin Result for functional error handling

## Network Layer

### ResultCallAdapterFactory

The project includes a custom Retrofit CallAdapter that transforms API responses into a `Result<T, Failure>` type using the kotlin-result library. This provides a cleaner way to handle network responses and errors.

#### How It Works

1. **ResultCallAdapterFactory**: Creates a custom CallAdapter for Retrofit that handles API responses.
2. **ResultCallAdapter**: Adapts the Call object to return Result type.
3. **ResultCall**: Custom Call implementation that transforms responses into Result.

The adapter handles different types of errors:
- HTTP error codes (4xx, 5xx)
- Network failures
- SSL errors
- Parsing errors

Each error is transformed into a user-friendly message using the `StringResProvider`.

#### Creating and Using a Service

NetworkResult is a typealias Result<T, Failure>

1. Define your API service interface:

```kotlin
interface MyService {
    @GET("endpoint")
    suspend fun getData(): NetworkResult<ResponseDto>
}
```

2. Create the service instance using Retrofit with the ResultCallAdapterFactory (typically in a Koin module):

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

## Dependency Injection

The project uses Koin for dependency injection.

### Modules Structure

- **networkModule**: Provides network-related dependencies (Retrofit, OkHttp, etc.)
- **databaseModule**: Provides database-related dependencies (Room database, DAOs)
- **uiModule**: Provides UI-related dependencies (Navigator)
- **Feature modules**: Provide feature-specific dependencies

### Setting Up a Module

```kotlin
val myFeatureModule = module {
    // Provide service
    single { get<Retrofit>().create(MyService::class.java) }
    
    // Provide repository
    single<MyRepository> { MyRepositoryImpl(get()) }
    
    // Provide ViewModel
    viewModelOf(::MyViewModel)
}
```

### Using Injection

In Composables:

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinViewModel()
) {
    // Use the ViewModel
}
```

In Android components:

```kotlin
class MyActivity : ComponentActivity() {
    private val navigator: Navigator by inject()
    
    // Use navigator
}
```

### Registering Modules

All modules are registered in the Application class:

```kotlin
class MovieDbLight : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@MovieDbLight)
            modules(
                homeModule,
                uiModule,
                networkModule,
                databaseModule,
                moviesModule
                // Add new modules here
            )
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

### Setting Up a New Feature

1. Create the feature modules:

```bash
./gradlew createFeatureModule -PfeatureName=profile
```

2. Define the domain model in the domain module:

```kotlin
data class Profile(
    val id: String,
    val name: String,
    // other properties
)
```

3. Define the repository interface in the domain module:

```kotlin
interface ProfileRepository {
    suspend fun getProfile(): Profile
}
```

4. Define the API service in the data module:

```kotlin
interface ProfileService {
    @GET("profile")
    suspend fun getProfile(): NetworkResult<ProfileResponse>
}
```

5. Implement the repository in the data module:

```kotlin
class ProfileRepositoryImpl(
    private val service: ProfileService
) : ProfileRepository {
    override suspend fun getProfile(): Profile {
        return service.getProfile().map {
            it.toProfile()
        }
    }
}
```

6. Create a ViewModel in the presentation module:

```kotlin
class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {
    // Implementation
}
```

7. Create the dependency injection module:

```kotlin
val profileModule = module {
    single { get<Retrofit>().create(ProfileService::class.java) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    viewModelOf(::ProfileViewModel)
}
```

8. Add your module to the Koin setup in the application class.

This structured approach ensures a clean separation of concerns and makes your codebase more maintainable and testable.
