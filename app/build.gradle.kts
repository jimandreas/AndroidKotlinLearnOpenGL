plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.androidkotlin.opengl"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.androidkotlin.opengl"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}


// https://github.com/sauravjha/kotlin-application-multiple-test-config/blob/master/build.gradle.kts
tasks.withType<Test> {
    useJUnitPlatform()

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            val suiteName = suite.name
            println(suiteName)
            if (suiteName.contains("Test Executor")) { // root suite
                println(
                    "Test summary: ${result.testCount} tests, " +
                            "${result.successfulTestCount} succeeded, " +
                            "${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped"
                )

            }
        }
    })
}
// a good guide to structuring dependencies in the brave new toml world
// https://github.com/RedMadRobot/gradle-version-catalogs/blob/main/versions-androidx/libs.versions.toml
dependencies {
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.fragment)

    implementation(libs.constraintlayout)
    implementation(libs.appcompat)

    // lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.timber)

    debugImplementation(libs.androidx.fragment.testing)
    testImplementation(libs.junit)
    testImplementation(libs.jupiter)
    debugImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.espresso.core)
}