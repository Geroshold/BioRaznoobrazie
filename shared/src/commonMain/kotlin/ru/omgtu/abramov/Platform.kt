package ru.omgtu.abramov

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform