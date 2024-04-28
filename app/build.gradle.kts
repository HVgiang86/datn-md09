plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "poly.manhnt.datn_md09"
    compileSdk = 34

    defaultConfig {
        applicationId = "poly.manhnt.datn_md09"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
        dataBinding = true

    }
}

dependencies {

    implementation(libs.glide)
    implementation(libs.logging)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("se.emilsjolander:StickyScrollViewItems:1.1.0")


}