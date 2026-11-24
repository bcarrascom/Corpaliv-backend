plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    // JaCoCo mide qué líneas del código ejecutan las pruebas unitarias (cobertura).
    jacoco
}

android {
    namespace = "com.example.corpalivapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.corpalivapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Las pruebas unitarias de debug registran la cobertura en un archivo .exec.
            enableUnitTestCoverage = true
        }
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    testCoverage {
        jacocoVersion = libs.versions.jacoco.get()
    }
    testOptions {
        unitTests {
            // Si una prueba toca una clase de Android (por ejemplo Log), devuelve un valor por defecto
            // en vez de fallar: las pruebas unitarias corren en el PC, no en el teléfono.
            isReturnDefaultValues = true
        }
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

// Reporte de cobertura SOLO de la lógica (ViewModels, repositorios, validador y clima).
// Las pantallas de Compose no se cuentan: se prueban usando la app, no con pruebas unitarias.
// Uso: .\gradlew.bat reporteCobertura  ->  app/build/reports/jacoco/reporteCobertura/html/index.html
tasks.register<JacocoReport>("reporteCobertura") {
    dependsOn("testDebugUnitTest")

    reports {
        html.required.set(true)
        xml.required.set(true)
    }

    val clasesDeLogica = fileTree(
        layout.buildDirectory.dir("intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes")
    ) {
        include(
            "**/viewmodel/**",
            "**/data/repository/**",
            "**/validacion/**",
            "**/clima/**"
        )
    }

    classDirectories.setFrom(clasesDeLogica)
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("outputs/unit_test_code_coverage/debugUnitTest/*.exec")
        }
    )
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)
}
