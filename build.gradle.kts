// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies{
        classpath ("io.realm:realm-gradle-plugin:10.13.3-transformer-api")
    }
}
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    // Maps
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    // Hilt
    id ("com.google.dagger.hilt.android") version "2.45" apply false
    // Realm
    id ("io.realm.kotlin") version "1.6.1" apply false
}