import java.util.Locale

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.mozilla.rust-android-gradle.rust-android")
}

android {
    namespace = "com.example.simple_counter.shared"
    compileSdk = 34

    ndkVersion = "27.0.11902837"

    defaultConfig {
        minSdk = 33
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    sourceSets {
//        main.java.srcDirs += "${projectDir}/../../shared_types/generated/java"
        named("main") {
            java.srcDirs("${projectDir}/../../shared_types/generated/java")
        }
    }
}

dependencies {
    implementation("net.java.dev.jna:jna:5.14.0@aar")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

//apply plugin: "org.mozilla.rust-android-gradle.rust-android"
apply(plugin = "org.mozilla.rust-android-gradle.rust-android")
cargo {
    module = "../.."
    libname = "shared"
    // these are the four recommended targets for Android that will ensure your library works on all mainline android devices
    // make sure you have included the rust toolchain for each of these targets: \
    // `rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android x86_64-linux-android`
    targets = listOf("arm", "arm64", "x86", "x86_64")
    extraCargoBuildArguments = listOf("--package", "shared")
    pythonCommand = "python3"
}

afterEvaluate {
    // The `cargoBuild` task isn"t available until after evaluation.
    android.libraryVariants.configureEach {
        var productFlavor = ""
        productFlavors.forEach { flavor ->
            productFlavor += flavor.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
        val buildType = buildType.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        tasks.named("compileDebugKotlin") {
            dependsOn(tasks.named("typesGen"), tasks.named("bindGen"))
        }

        tasks.named("generate${productFlavor}${buildType}Assets") {
            dependsOn(tasks.named("cargoBuild"))
        }
    }
}

tasks.register<Exec>("bindGen") {
    val outDir = "${projectDir}/../../shared_types/generated/java"
    workingDir = File("../../")
    if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")) {
        commandLine(
            "cmd", "/c",
            "cargo build -p shared && " + "target\\debug\\uniffi-bindgen generate shared\\src\\shared.udl " + "--language kotlin " + "--out-dir " + outDir.replace(
                "/",
                "\\"
            )
        )
    } else {
        commandLine(
            "sh", "-c",
            """\
                cargo build -p shared && \
                target/debug/uniffi-bindgen generate shared/src/shared.udl \
                --language kotlin \
                --out-dir $outDir
                """
        )
    }
}

tasks.register<Exec>("typesGen") {
    workingDir = File("../../")
    if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")) {
        commandLine("cmd", "/c", "cargo build -p shared_types")
    } else {
        commandLine("sh", "-c", "cargo build -p shared_types")
    }
}
