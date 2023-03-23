package pe.grpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcSpringBootApplication

fun main(args: Array<String>) {
    runApplication<GrpcSpringBootApplication>(*args)
}
