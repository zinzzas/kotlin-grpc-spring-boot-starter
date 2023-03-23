import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

object Version {
	const val grpc = "1.53.0"
	const val grpcKotlin = "1.3.0"
	const val protoc = "3.22.0"
	const val kotest = "5.5.5"
}

extra["kotlin-coroutines.version"] = "1.6.4"

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.google.protobuf") version "0.9.2"
	kotlin("jvm") version "1.8.0"
	kotlin("plugin.spring") version "1.8.0"
	kotlin("kapt") version "1.8.0"
	idea
}

group = "pe.grpc"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_15

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}


dependencies {
	//spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	//grpc & spring boot 연동
	implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")

	//grpc
	implementation("io.grpc:grpc-kotlin-stub:${Version.grpcKotlin}")
	implementation("com.google.protobuf:protobuf-kotlin:${Version.protoc}")
	implementation("io.grpc:grpc-netty:${Version.grpc}")

	//mapstruct
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")

	//test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("io.kotest:kotest-runner-junit5:${Version.kotest}")
	testImplementation("io.kotest:kotest-assertions-core:${Version.kotest}")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
	testImplementation("io.projectreactor:reactor-test")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "15"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

plugins.withType<ProtobufPlugin> {
	sourceSets {
		main {
			proto {
				srcDir("main")
			}
		}
	}

	protobuf {
		protoc {
			artifact = "com.google.protobuf:protoc:${Version.protoc}"
		}

		plugins {
			id("grpc") {
				artifact = "io.grpc:protoc-gen-grpc-java:${Version.grpc}"
			}
			id("grpckt") {
				artifact = "io.grpc:protoc-gen-grpc-kotlin:${Version.grpcKotlin}:jdk8@jar"
			}
		}

		generateProtoTasks {
			ofSourceSet("main").forEach{
				it.plugins {
					id("grpc")
					id("grpckt")
				}
				it.builtins {
					id("kotlin")
				}
			}
		}
	}
}