package com.freetimeworks.cdc.accountservice.client

import java.util.*

data class User(
        val id: UUID,
        val name: String,
        val email: String
)