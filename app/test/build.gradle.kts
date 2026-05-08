plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.koshertech.su.test"

    defaultConfig {
        applicationId = "com.koshertech.su.test"
        versionCode = 1
        versionName = "1.0"
        proguardFile("proguard-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
}

setupTestApk()

dependencies {
    implementation(libs.test.runner)
    implementation(libs.test.rules)
    implementation(libs.test.junit)
    implementation(libs.test.uiautomator)
}
