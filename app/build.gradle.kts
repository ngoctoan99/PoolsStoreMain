import java.text.SimpleDateFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.io.FileInputStream
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id ("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
}

val projectProperties = Properties()
val projectPropertiesFile = rootProject.file("project.properties")
if (projectPropertiesFile.exists()) {
    projectProperties.load(FileInputStream(projectPropertiesFile))
}

android {
    namespace = "com.pools.store"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pools.store"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Get the current date
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = formatter.format(date)

        // Modify the output file name
        val newFileName = "Pool Store(${formattedDate})(${versionName})${System.getenv("BuildID") ?: ""}"

        // Set the new file name
        archivesName.set(newFileName)
    }

    signingConfigs {
        create("release") {

            keyPassword = projectProperties["keyPassword"].toString()
            keyAlias = projectProperties["keyAlias"].toString()
            storePassword = projectProperties["storePassword"].toString()
            storeFile = projectProperties["storeFile"]?.let {
                file(
                    it
                )
            }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true

    }
    lint {
        // Turns off checks for the issue IDs you specify.
        disable += "ContentDescription"
        // Turns on checks for the issue IDs you specify. These checks are in
        // addition to the default lint checks.
        //   enable += "RtlHardcoded" + "RtlCompat" + "RtlEnabled"
        // To enable checks for only a subset of issue IDs and ignore all others,
        // list the issue IDs with the 'check' property instead. This property overrides
        // any issue IDs you enable or disable using the properties above.
        // checkOnly += "NewApi" + "InlinedApi"
        // If set to true, turns off analysis progress reporting by lint.
        //  quiet = true
        // If set to true (default), stops the build if errors are found.
        abortOnError = false
        // If set to true, lint only reports errors.
        //  ignoreWarnings = true
        // If set to true, lint also checks all dependencies as part of its analysis.
        // Recommended for projects consisting of an app with library dependencies.
        checkDependencies = true
        checkReleaseBuilds = true


    }
    flavorDimensions += listOf("mode")
    productFlavors {
        create("Staging") {
            dimension = "mode"
            resValue(type = "string", name = "app_name", value = "Pools Store Staging")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://api-dev.aioscar.io/api/\""
            )
        }
        create("Development") {
            dimension = "mode"
            resValue(type = "string", name = "app_name", value = "Pools Store Development")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://api-dev.aioscar.io/api/\""
            )
        }
        create("Production") {
            dimension = "mode"
            resValue(type = "string", name = "app_name", value = "Pools Store")
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://api-dev.aioscar.io/api/\""
            )
        }
    }


}
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")

    //Gson
    implementation("com.google.code.gson:gson:2.10.1")

    //moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    //filament

    val filament_version = "1.49.2"
    implementation("com.google.android.filament:filament-android:$filament_version")
    implementation("com.google.android.filament:filament-utils-android:$filament_version")
    implementation("com.google.android.filament:gltfio-android:$filament_version")
    implementation("com.google.android.filament:filamat-android:$filament_version")

    // retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    // logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    //lottie-android
    implementation("com.airbnb.android:lottie:6.3.0")

    //Hilt
    implementation ("com.google.dagger:hilt-android:2.50")
    kapt ("com.google.dagger:hilt-compiler:2.48")
    // Circle Image View
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //    Navigation component
    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // swipe refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation (project(":pools_sso"))

    //circleindicator
     implementation ("me.relex:circleindicator:2.1.6")

    // Import the Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation ("com.google.firebase:firebase-messaging-ktx:23.1.2")



    // rating bar
    implementation ("me.zhanghai.android.materialratingbar:library:1.4.0")

    implementation ("com.google.android.flexbox:flexbox:3.0.0")


    //ads
    implementation ("com.google.android.gms:play-services-ads:22.1.0")
}