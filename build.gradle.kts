plugins {
    id("java")
    id("application")
}

group = "net.dingletherat.torgrays_trials"
version = "Alpha-2.3"

application {
    mainClass.set("net.dingletherat.torgrays_trials.main.Main")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20250107") // Json Json 2025
    implementation("com.github.UsUsStudios:Torgrays-Datagen:v1.1")
    implementation("org.slf4j:slf4j-api:2.0.9");
    runtimeOnly("ch.qos.logback:logback-classic:1.5.13")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceSets["main"].java.srcDirs("src/main/src")
}

tasks.register<Jar>("buildFatJar") {
    archiveBaseName.set("Torgrays-Trials")
    from(sourceSets["main"].output)

    manifest.from(file("src/main/resources/META-INF/MANIFEST.MF"))

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}


tasks.register<JavaExec>("generateData") {
    mainClass.set("net.dingletherat.torgrays_trials.datagen.DataGenerator")
    classpath = sourceSets.main.get().runtimeClasspath
}
