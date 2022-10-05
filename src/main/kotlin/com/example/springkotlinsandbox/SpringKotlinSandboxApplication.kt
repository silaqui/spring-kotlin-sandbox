package com.example.springkotlinsandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringKotlinSandboxApplication

fun main(args: Array<String>) {
    runApplication<SpringKotlinSandboxApplication>(*args)
}
