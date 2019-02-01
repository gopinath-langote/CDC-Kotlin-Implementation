package com.freetimeworks.cdc.accountservice.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestTemplate
import java.util.*

class UserServiceClient(
        @Value("\${USER_SERVICE_HOST}")
        private val userServiceHost: String,
        @Value("\${USER_SERVICE_POST}")
        private val userServicePort: Int,
        private val restTemplate: RestTemplate
) {
    fun getUser(userId: Int): User {
        val url = "http://$userServiceHost:$userServicePort/users/$userId"
        val responseEntity = restTemplate.getForEntity(url, User::class.java)
        return Optional.ofNullable(responseEntity.body)
                .map { it }
                .orElseThrow { RuntimeException("Failed got get user from userService") }
    }
}