plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")

    id("com.google.devtools.ksp")
//    alias(libs.plugins.kotlin.compose)


}

android {
    namespace = "com.example.sejongapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sejongapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.sessions)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)





    // Основная библиотека Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Конвертер для работы с JSON (Gson)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.compose.material:material-icons-extended:1.6.1")


    /// ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    //    Coil (Coroutine Image Loader)
//    implementation("io.coil-kt:coil-compose:1.3.2")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-gif:2.4.0")

    // Lottie - animation
    implementation("com.airbnb.android:lottie-compose:6.0.0")


    // build.gradle (app)

        implementation ("com.google.accompanist:accompanist-pager:0.31.5-beta") // последняя версия
        implementation ("com.google.accompanist:accompanist-pager-indicators:0.31.5-beta")

    implementation("androidx.exifinterface:exifinterface:1.3.6") // не оригинал



    // room
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.compose.material:material-icons-extended")





    implementation("androidx.navigation:navigation-compose:2.7.7")


    implementation("androidx.compose.material:material-icons-extended")


    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    implementation("com.github.yalantis:ucrop:2.2.8")



    
}