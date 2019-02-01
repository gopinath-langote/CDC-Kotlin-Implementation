package com.freetimeworks.cdc.userservice.model

import java.util.*

data class User(
        val id: UUID,
        val name: String,
        val email: String
)