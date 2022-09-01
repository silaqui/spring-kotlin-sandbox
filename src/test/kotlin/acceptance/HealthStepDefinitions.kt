package acceptance

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

class HealthStepDefinitions {

    @Suppress("SpringJavaAutowiredMembersInspection")
    @Autowired
    private lateinit var mockMvc: MockMvc

    private var healthCheckResponse: String? = null

    @When("I want to @Get health")
    @Throws(Exception::class)
    fun getAtHealth() {
        healthCheckResponse = mockMvc
            .perform(MockMvcRequestBuilders.get("/health"))
            .andReturn()
            .response
            .contentAsString
    }

    @Then("I received OK response")
    fun responseOK() {
        assertEquals("OK", healthCheckResponse)
    }
}