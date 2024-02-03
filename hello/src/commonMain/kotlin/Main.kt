package io.dpopkov

fun main() {
    val greeting = Greeting("World", "Hello")
    val message = GreetingService().format(greeting)
    println(message)
}
