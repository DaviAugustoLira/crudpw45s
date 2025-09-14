package br.edu.utfpr.pw45s

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform