package spring.service

import spring.service.data.Task
import spring.service.data.TasksRepository
import spring.service.data.TasksRepositoryMemory
import org.springframework.stereotype.Service
import spring.service.httperror.*

@Service
class TaskService(private val taskRepo: TasksRepository) {

    fun addTask(task: Task): Task {
        if (task.completed) {
            throw AddCompletedTaskException()
        }
        val id = taskRepo.addTask(task)
        task.id = id
        return task
    }

    fun deleteTask(id: Int) {
        if (id < 0) {
            throw InvalidTaskException()
        }
        taskRepo.getTasks(true).find { it.id == id }?.let {
            throw DeleteCompletedTaskException()
        }
        taskRepo.getTasks(false).find { it.id == id }?.let {
            taskRepo.completeTask(id)
        } ?: throw TaskNotFoundException()
    }

    fun completeTask(id: Int): Task {
        if (id < 0) {
            throw InvalidTaskException()
        }
        taskRepo.getTasks(true).find { it.id == id }?.let {
            throw TaskCompletingError()
        }
        taskRepo.getTasks(false).find { it.id == id }?.let {
            taskRepo.completeTask(id)
        } ?: throw TaskNotFoundException()
        return taskRepo.getTasks(true).find { it.id == id }!!
    }

    fun uncompleteTask(id: Int): Task {
        if (id < 0) {
            throw InvalidTaskException()
        }
        taskRepo.getTasks(false).find { it.id == id }?.let {
            throw TaskCompletingError()
        }
        taskRepo.getTasks(true).find { it.id == id }?.let {
            taskRepo.uncompleteTask(id)
        } ?: throw TaskNotFoundException()
        return taskRepo.getTasks(false).find { it.id == id }!!

    }

    fun getTasks(completed: Boolean): List<Task> {
        return taskRepo.getTasks(completed).sortedByDescending { it.priority }
    }
}