plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("kotlin-parcelize")
}

android {
    namespace = "ghazimoradi.soheil.recipeapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "ghazimoradi.soheil.recipeapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    //Paging
    implementation(libs.androidx.paging.runtime)

    //KotlinMetaData
    ksp(libs.kotlinMetadata)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //fragment ktx
    implementation(libs.androidx.fragment.ktx)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Room components
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //DataStore
    implementation(libs.androidx.datastore.preferences)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //OkHTTP client
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Image Loading
    implementation(libs.coil)
    implementation(libs.coil.network.okhttp)

    //Gson
    implementation(libs.gson)

    //Calligraphy
    implementation(libs.calligraphy3)
    implementation(libs.viewpump)

    //Other
    implementation(libs.shimmer)
    implementation(libs.shimmerrecyclerview)
    implementation(libs.dynamicsizes)
    implementation(libs.lottie)
    implementation(libs.readmore.textview) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-android-extensions-runtime")
    }
}