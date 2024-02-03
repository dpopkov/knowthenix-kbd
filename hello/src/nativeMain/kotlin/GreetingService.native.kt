package io.dpopkov

actual class GreetingService actual constructor() {
    actual fun format(greeting: Greeting): String {
        return "${greeting.text} ${greeting.name} in native!"
    }

}
