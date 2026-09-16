package com.sivalabs.blog

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun main() {
    println(BCryptPasswordEncoder().encode("password"))
}
