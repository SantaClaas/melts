# How to get SQLite with SQLCipher in Rust on Android

The instructions are specific to this repository at the commit this file was added.
1. Install cargo-ndk
2. Add rusqlite to Cargo.toml with the following features:
    ```toml
    rusqlite = { version = "0.31.0", features = [
       "bundled-sqlcipher-vendored-openssl",
       "bundled",
    ] }
    ```
   Since it needs a sqlite and openssl version that is specific to android this is the easiest way to get that.
3. Don't try to use the Mozilla Rust Android Gradle Plugin
   Won't build due to some error I don't understand
4. Only build with cargo-ndk
   ```cargo ndk --target aarch64-linux-android --platform 26 --output-dir ./Android/app/src/main/jniLibs/ -- build```  
   Things to note here:
   - The target might be different depending on where you want to run it. For me, it was an android emulator on an Apple Silicon MacBook
   - The file is output in the app main jniLibs directory. This is only to have it included in the app bundle.
   The code actually calling the library is in the "shared" android library project. This was just the easiest way to include it in the app as the gradle stuff I tried wasn't working.
5. Run the app build in Android Studio. There is some UniFFI bindings generation stuff that needs to be run but that is specific to this project.