import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.3.20"
    kotlin("plugin.serialization") version "2.3.20"
    kotlin("plugin.compose") version "2.3.20"
    id("org.jetbrains.compose") version "1.10.0"
}

group = "com.lagradost.desktop"
version = "4.8.0"

kotlin {
    applyDefaultHierarchyTemplate()

    jvm("desktop") {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-Xannotation-default-target=param-property"
        )
    }

    sourceSets {
        all {
            languageSettings {
                optIn("com.lagradost.cloudstream3.InternalAPI")
                optIn("com.lagradost.cloudstream3.Prerelease")
                optIn("com.lagradost.cloudstream3.UnsafeSSL")
            }
        }

        // ---- Stesse dipendenze del modulo `library` di CloudStream ----
        commonMain.dependencies {
            implementation("androidx.annotation:annotation:1.10.0")
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
            implementation("org.jsoup:jsoup:1.22.1")
            implementation("org.jetbrains.kotlinx:atomicfu:0.33.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
            implementation("com.fleeksoft.ksoup:ksoup:0.2.6")
            implementation("io.ktor:ktor-http:3.5.0")
            implementation("com.github.Blatzar:NiceHttp:0.4.18")
            implementation("org.mozilla:rhino:1.8.1")
            implementation("com.uwetrottmann.tmdb2:tmdb-java:2.13.0")
            implementation("dev.whyoleg.cryptography:cryptography-core:0.6.0")
            implementation("dev.whyoleg.cryptography:cryptography-provider-optimal:0.6.0")
            implementation("me.xdrop:fuzzywuzzy:1.4.0")
        }

        val jvmCommonMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.20")
                implementation("com.github.teamnewpipe:NewPipeExtractor:v0.26.3")
            }
        }

        // ---- Applicazione desktop ----
        val desktopMain by getting {
            dependsOn(jvmCommonMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.11.0")
                implementation("uk.co.caprica:vlcj:4.8.3") // player (richiede VLC 64 bit installato)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.lagradost.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi)
            packageName = "CloudStream"
            packageVersion = "4.8.0"
            description = "CloudStream per Windows 11"
            vendor = "CloudStream Desktop"
            includeAllModules = true

            windows {
                menuGroup = "CloudStream"
                shortcut = true
                dirChooser = true
                perUserInstall = true
                upgradeUuid = "6f1c3b8e-2a4d-4f57-9c11-5b7d0a9e4c21"
            }
        }
    }
}
