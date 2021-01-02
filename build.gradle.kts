plugins {
  kotlin("multiplatform") version "1.4.30-M1"
}

group = "com.lorenzoog"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

kotlin {
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux" -> linuxX64("native")
    isMingwX64 -> mingwX64("native")
    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }

  nativeTarget.apply {
    binaries {
      executable {
        entryPoint = "com.lorenzoog.nativelibrary.main"
      }
    }

    compilations["main"].cinterops {
      val runtime by creating {
        defFile = File("$projectDir/native-library/runtime.def")
        compilerOpts("-I/usr/local/include", "-I$projectDir/native-library")
        extraOpts("-libraryPath", "$projectDir/native-library/build")
        extraOpts("-staticLibrary", "libruntime.a")
      }
    }
  }
  sourceSets {
    val nativeMain by getting
    val nativeTest by getting
  }
}
