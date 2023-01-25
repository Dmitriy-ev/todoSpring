package app.httperror

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Adding a completed task")
    @ExceptionHandler(AddCompletedTaskException::class)
    fun handleException(e: AddCompletedTaskException) {}

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong task id")
    @ExceptionHandler(InvalidTaskException::class)
    fun handleException(e: InvalidTaskException) {}

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Deleting completed task not allowed")
    @ExceptionHandler(DeleteCompletedTaskException::class)
    fun handleException(e: DeleteCompletedTaskException) {}

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Task not found")
    @ExceptionHandler(TaskNotFoundException::class)
    fun handleException(e: TaskNotFoundException) {}

    @ResponseStatus(value = HttpStatus.ALREADY_REPORTED, reason = "Task alredy completed|uncompleted")
    @ExceptionHandler(TaskCompletingError::class)
    fun handleException(e: TaskCompletingError) {}
}

class AddCompletedTaskException : Exception()
class InvalidTaskException: Exception()
class DeleteCompletedTaskException: Exception()
class TaskNotFoundException: Exception()
class TaskCompletingError: Exception()