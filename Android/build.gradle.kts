plugins {
    // Can"t update to 8.4.0+ yet, due to https://issuetracker.google.com/issues/342428022
    id("com.android.application") version ("8.3.2") apply false
    id("com.android.library") version ("8.3.2") apply false
    id("org.jetbrains.kotlin.android") version ("1.9.10") apply false
//    id("org.mozilla.rust-android-gradle.rust-android") version ("0.9.4") apply false
}