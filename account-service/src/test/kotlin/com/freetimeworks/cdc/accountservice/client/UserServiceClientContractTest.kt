package com.freetimeworks.cdc.accountservice.client

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.model.RequestResponsePact
import com.freetimeworks.cdc.accountservice.client.UserServiceClientContractTest.Companion.USER_SERVICE_HOST
import com.freetimeworks.cdc.accountservice.client.UserServiceClientContractTest.Companion.USER_SERVICE_PORT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate


@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "user-service", port = USER_SERVICE_PORT.toString(), hostInterface = USER_SERVICE_HOST)
class UserServiceClientContractTest {
    companion object {
        const val USER_SERVICE_HOST = "localhost"
        const val USER_SERVICE_PORT = 1081
    }

    private val userId = 1
    private val name = "gopinath"
    private val email = "gopinath@gmail.com"

    lateinit var userServiceClient: UserServiceClient

    @BeforeEach
    fun setUp() {
        userServiceClient = UserServiceClient(
                userServiceHost = USER_SERVICE_HOST,
                userServicePort = USER_SERVICE_PORT,
                restTemplate = RestTemplate()
        )
    }

    @Test
    fun `should get user by id`(mockServer: MockServer) {
        val userServiceClient = UserServiceClient(USER_SERVICE_HOST, USER_SERVICE_PORT, RestTemplate())

        val user = userServiceClient.getUser(userId)

        val expectedUser = User(userId, name, email)
        assertThat(user).isEqualTo(expectedUser)
    }

    @Pact(provider = "user-service", consumer = "account-service")
    fun createPact(builder: PactDslWithProvider): RequestResponsePact {

        val body = PactDslJsonBody()
                .integerType("id", userId)
                .stringType("name", name)
                .stringType("email", email)

        val headers = mapOf("Content-Type" to "application/json;charset=UTF-8")
        return builder.uponReceiving("a request to get an user")
                .matchPath("/users/$userId")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(body)
                .toPact()
    }

}