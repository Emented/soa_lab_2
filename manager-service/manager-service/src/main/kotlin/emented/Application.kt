package emented

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = ["emented", "emented.api", "emented.model"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
