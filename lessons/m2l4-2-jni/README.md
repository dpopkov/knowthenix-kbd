## Работа с JNI

1. Создать Kotlin-класс
2. Сгенерировать из котлин-класса заголовочный файл языка C. 
   Этот файл будет описывать обертку, которую нам нужно написать.
   Генерация будет произоводиться с помощью Gradle task описанной в build.gradle.kts.
3. Написать код на C библиотеки-обертки, соответствующий сгенерированному заголовку. 
   В этом коде можно вызывать целевую нативную библиотеку.

Т.е. еще раз. Для интеграции с JVM требуется написать библиотеку-обертку, которую будет вызывать JRE. 
В библиотеке-обертке вы выполните вызов вашей целевой нативной библиотеки.
