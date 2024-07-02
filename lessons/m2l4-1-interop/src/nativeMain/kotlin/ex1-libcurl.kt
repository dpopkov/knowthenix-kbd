@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import libcurl.*

/*
Для того чтобы содержимое header файла curl/curl.h было доступно в Kotlin коде,
может потребоваться выполнить task in Gradle - cinteropLibcurlLinuxX64.
 */
@OptIn(ExperimentalForeignApi::class)
fun main() {
    val curl = curl_easy_init()
    if (curl != null) { // проверка что инициализация корректно выполнена
        curl_easy_setopt(curl, CURLOPT_URL, "http://info.cern.ch")
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L)
        val res = curl_easy_perform(curl)   // выполнение запроса по указанному URL
        if (res != CURLE_OK) {
            println("curl_easy_perform() failed: ${curl_easy_strerror(res)?.toKString()}")
        } else {
            println("curl_easy_perform() succeeded: $res")
        }
        curl_easy_cleanup(curl) // корректное закрытие
    }
}
