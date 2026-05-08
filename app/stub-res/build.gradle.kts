plugins {
    alias(libs.plugins.android.application)
}

setupCommon()

android {
    namespace = "com.koshertech.su"
    enableKotlin = false

    buildTypes {
        release {
            isShrinkResources = false
        }
    }
}
