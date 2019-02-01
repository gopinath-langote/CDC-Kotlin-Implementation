package com.freetimeworks.cdc.accountservice.client

import com.freetimeworks.cdc.accountservice.AccountServiceApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse
import org.mockserver.model.HttpStatusCode
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import java.util.*

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [AccountServiceApplication::class]
)

@ExtendWith(SpringExtension::class)
class UserServiceClientIntegrationTest {
    companion object {
        const val USER_SERVICE_HOST = "localhost"
        const val USER_SERVICE_PORT = 1081
    }

    lateinit var mockServer: ClientAndServer

    lateinit var userServiceClient: UserServiceClient


    @BeforeEach
    fun setUp() {
        mockServer = ClientAndServer.startClientAndServer(USER_SERVICE_PORT)

        userServiceClient = UserServiceClient(
                userServiceHost = USER_SERVICE_HOST,
                userServicePort = USER_SERVICE_PORT,
                restTemplate = RestTemplate()
        )
    }

    @AfterEach
    fun tearDown() {
        mockServer.reset()
    }

    @Test
    fun `should get user by id`() {
        val userId = UUID.randomUUID()
        val name = "gopinath"
        val email = "gopinath@gmail.com"

        mockServer.`when`(
                request("/users/$userId")
                        .withMethod("GET")
        )
                .respond(HttpResponse.response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "id": "$userId",
                                "name": "$name",
                                "email": "$email"
                            }
                        """.trimIndent())
                )

        val user = userServiceClient.getUser(userId)

        val expectedUser = User(userId, name, email)
        assertThat(user).isEqualTo(expectedUser)
    }
}