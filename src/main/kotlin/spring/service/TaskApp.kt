package spring.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TaskApp

fun main() {
    runApplication<TaskApp>()
}
