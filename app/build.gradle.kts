plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.activiza.activiza"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.activiza.activiza"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    val picasso_version = "2.71828"
    val navVersion = "2.7.0"

    //galeria para seleccionar imagen
    implementation("androidx.activity:activity:1.9.0")

    //Picasso
    implementation ("com.squareup.picasso:picasso:$picasso_version")

    //Navegación entre fragmentos
    implementation ("androidx.navigation:navigation-fragment-ktx:$$navVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$$navVersion")

    //Retrofit que se usa para la llamada a la api
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}