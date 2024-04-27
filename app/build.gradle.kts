plugins {
    id("com.android.application")
<<<<<<< HEAD
}

android {
    namespace = "com.fitness.proyecto_tfc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fitness.proyecto_tfc"
=======
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.activiza.activiza"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.activiza.activiza"
>>>>>>> david
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
<<<<<<< HEAD
=======
    kotlinOptions {
        jvmTarget = "1.8"
    }
>>>>>>> david
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
<<<<<<< HEAD
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.fragment:fragment:1.6.2")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.0")
=======

    val navVersion = "2.7.0"
    //Navegación entre fragmentos
    implementation ("androidx.navigation:navigation-fragment-ktx:$$navVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$$navVersion")

    //Retrofit que se usa para la llamada a la api
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
>>>>>>> david
}