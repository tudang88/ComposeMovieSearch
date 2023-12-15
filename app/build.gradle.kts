plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize") // add plugin to use Parcelize
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.composebootcamp.moviesearch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.composebootcamp.moviesearch"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        // the movies db api key
        buildConfigField("String", "TMDB_API_KEY", "${properties["tmdb_api_key"]}")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // add this for compose option
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

dependencies {
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    // Dependencies for using Room DB => START
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // To use Kotlin annotation processing tool (kapt)
    ksp("androidx.room:room-compiler:$roomVersion")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    // Dependencies for using Room DB => END

    // Dependency for Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // Dependencies for Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.7.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // Dependencies for koroutines
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Dependencies for Timber log
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Dependency for Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Dependency for koin
    implementation("io.insert-koin:koin-android:3.5.2-RC1")

    // Java Compatibility
    implementation("io.insert-koin:koin-android-compat:3.5.2-RC1")
    // Jetpack WorkManager
    implementation("io.insert-koin:koin-androidx-workmanager:3.5.2-RC1")
    // Navigation Graph
    implementation("io.insert-koin:koin-androidx-navigation:3.5.2-RC1")

    val nav_version = "2.7.5"
    // Java language implementation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    testImplementation("android.arch.core:core-testing:1.1.1")
    // Local test dependencies
    testImplementation("junit:junit:4.13.2")
    // Optional -- Robolectric environment
    testImplementation("androidx.test:core-ktx:1.5.0")
    // Optional -- Mockito framework
    testImplementation("org.mockito:mockito-core:5.7.0")
    // Optional -- mockito-kotlin
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    // Optional -- Mockk framework
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0-RC")
    testImplementation("org.robolectric:robolectric:4.11.1")
    // For Espresso
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
}