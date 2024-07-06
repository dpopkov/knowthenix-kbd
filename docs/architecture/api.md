# API

## Описание сущности Translation (Перевод)

Является __основной__ сущностью для проекта.

1. OriginalId (id первичного исходного перевода, из которого получен данный перевод)
2. ParentId (в случае если данный перевод является частью составного перевода)
3. Language (национальный язык)
4. Content (текстовое содержимое)
5. Format (Plain text / Markdown / HTML)
6. Type (Question / Answer / Article / Tutorial)
7. State (New / Edited / ToVerify / Verified)
8. Owner
9. Mutability (Mutable, Immutable)
10. UnifyingId (для связи с сущностями Question, Answer или Article, которые пока отсутствует)
11. Archived (True/False)

## Функции (endpoints)

1. CRUDS (Create, Read, Update, Delete, Search) для переводов (Translations)
2. Дополнительно, после сдачи основной части проекта:
    * Copy (опционально): создает копию перевода для последующего редактирования
    * RequestTranslation (опционально): запрашивает (авто)перевод на другой язык
    * Export (опционально): экспорт в другой формат (pdf)
