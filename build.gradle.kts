val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val developmentMode: Boolean by project

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.2"
    kotlin("plugin.serialization") version "1.5.0"
}

group = "ukidelly"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {

    // ktor
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.3")
    implementation("io.ktor:ktor-server-resources:$ktor_version")

    // auth
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    // Di
    implementation("io.insert-koin:koin-ktor:3.4.1")
    implementation("io.insert-koin:koin-ksp-compiler:1.2.2")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.3")
    implementation("io.ktor:ktor-server-websockets-jvm:2.3.3")

    runtimeOnly("io.insert-koin:koin-annotations:1.2.2")

    // swagger
    implementation("io.ktor:ktor-server-swagger-jvm:$ktor_version")

    // json
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // database
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.2.0")

//    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("io.github.jan-tennert.supabase:storage-kt:1.3.0")
    implementation("com.zaxxer:HikariCP:5.0.1")

    // logging
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // validation
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")
    implementation("at.favre.lib:bcrypt:0.10.2")

    // status
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.3")

    // web socket
    implementation("io.ktor:ktor-server-websockets:$ktor_version")

    // test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")


//    implementation("org.litote.kmongo:kmongo:4.8.0")
//    implementation("io.ktor:ktor-serialization-jackson:2.3.1-eap-678")
//    implementation("io.ktor:ktor-serialization-jackson-jvm:2.3.1-eap-676")
//    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

}

//test
//tasks.withType<Test> {
//    useJUnitPlatform()
//}