# Модуль 'knowthenix-common'

Модуль содержит только объекты, которые используются всеми остальными модулями проекта. 
**Запрещено** сюда добавлять зависимости, которые характерны для какой-то специфичной реализации 
(Postgres, WebsocketSession). 
Следует снизить количество зависимостей для этого модуля. 
Допускается добавлять зависимости на базовые библиотеки типа kotlinx.datetime, kotlinx.coroutines и подобные.
