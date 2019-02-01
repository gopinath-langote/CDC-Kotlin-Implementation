package com.freetimeworks.cdc.userservice.controller

import com.freetimeworks.cdc.userservice.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Int): User = User(id, "gopinath", "gopinath@gmail.com")
}