package app

import app.controller.TaskController
import app.data.Priority
import app.data.Task
import app.httperror.InvalidTaskException
import app.httperror.TaskNotFoundException
import app.service.TaskService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@WebMvcTest
class TaskControllerTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var taskController: TaskController

    @MockkBean
    lateinit var taskService: TaskService


    @Test
    fun addTask() {

        val task7 = Task(7, "task7", Priority.HIGH, false)

        @Language("JSON")
        val expectedMessage = """{"id":7,"name":"task7","priority":"HIGH","completed":false}""".trimIndent()

        every { taskService.addTask(task7) } returns task7

        val resultMvc = mvc.perform(
            post("/api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task7)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andReturn()

        val response = resultMvc.response.contentAsString

        assertEquals(expectedMessage, response)

        verify(exactly = 1) { taskService.addTask(task7) }

    }

    @Test
    fun getUncompletedTask() {

        every { taskService.getTasks(false) } returns listOf(
            Task(1, "task1", Priority.HIGH),
        )

        val resultMvc = mvc.perform(get("/api/v1/task")
            .param("status", "false"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = resultMvc.response.contentAsString

        assertEquals("""[{"id":1,"name":"task1","priority":"HIGH","completed":false}]""", responseBody)

        verify(exactly = 1) { taskService.getTasks(false) }
    }

    @Test
    fun getCompletedTask() {

        every { taskService.getTasks(true) } returns listOf()

        val resultMvc = mvc.perform(get("/api/v1/task")
            .param("status", "true"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = resultMvc.response.contentAsString

        assertEquals("""[]""", responseBody)

        verify(exactly = 1) { taskService.getTasks(true) }

    }

    @Test
    fun deleteTask() {

        every { taskService.deleteTask(1) } returns Unit

        mvc.perform(delete("/api/v1/task/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)

        verify(exactly = 1) { taskService.deleteTask(1) }
        }

    @Test
    fun deleteIncorrectTask(){

        every { taskService.deleteTask(-100) } throws InvalidTaskException()

        mvc.perform(delete("/api/v1/task/{id}", -100)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest)

        verify(exactly = 1) { taskService.deleteTask(-100) }

    }

    @Test
    fun deleteCompletedTask(){

        every { taskService.deleteTask(1) } throws TaskNotFoundException()

        mvc.perform(delete("/api/v1/task/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound)

        verify(exactly = 1) { taskService.deleteTask(1) }
    }

    @Test
    fun completeTask(){

        every { taskService.completeTask(4) } returns Task(4, "task4", Priority.LOW, true)

        mvc.perform(put("/api/v1/task/{4}", 4)
            .param("status", "true")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
    }
}