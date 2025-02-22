plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.dss"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dss"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.google.android.material:material:1.5.0-alpha01")
    implementation ("com.airbnb.android:lottie:4.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation ("com.github.aabhasr1:OtpView:v1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.hbb20:ccp:2.5.0")
    implementation ("com.hbb20:ccp:2.5.2")

}