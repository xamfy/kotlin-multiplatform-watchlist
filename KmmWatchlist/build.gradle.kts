buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }

    val kotlinVersion = "1.4.10"
    val sqlDelightVersion: String by project

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha16")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.7.0")
    }
}
group = "com.xamfy.kmm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
