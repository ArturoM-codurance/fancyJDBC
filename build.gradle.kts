plugins {
    id("java")
}

group = "org.fancyjdbc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.5")
    implementation("com.sparkjava:spark-core:2.9.4")
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    implementation("org.postgresql:postgresql:42.6.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}