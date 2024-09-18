plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gestion_univ"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gestion_univ"
        minSdk = 26
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
    buildFeatures{
        viewBinding = true;
    }
}

dependencies {
    implementation (platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-firestore:23.0.3")
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("com.google.android.gms:play-services-auth:21.0.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("jp.wasabeef:glide-transformations:4.3.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation ("com.karumi:dexter:6.2.3")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
   // implementation ("com.google.zxing:core:3.4.1")
   implementation ("com.journeyapps:zxing-android-embedded:4.3.0")



    implementation("com.github.clans:fab:1.6.4")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.25")
    implementation(libs.zxing)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.annotation)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}