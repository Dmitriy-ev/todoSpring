package spring.service.controller

import spring.service.data.Task
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spring.service.TaskService

@RestController
@RequestMapping("api/v1/task")
class TaskController(private val taskService: TaskService) {

    @PostMapping
    fun addTask(@RequestBody task: Task): Task {
        return taskService.addTask(task)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable("id") id: Int) {
        taskService.deleteTask(id)
    }

    @PutMapping
    fun changeTaskStatus(@PathVariable("id") id: Int, @RequestParam("status") status: Boolean): Task {
        return if (status) {
            taskService.completeTask(id)
        } else {
            taskService.uncompleteTask(id)
        }
    }

    @GetMapping
    fun getTask(@RequestParam("status") status: Boolean): List<Task> {
        return taskService.getTasks(status)
    }
}