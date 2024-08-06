package io.dpopkov.knowthenixkbd.biz

import io.dpopkov.knowthenixkbd.biz.plugins.getLoggerProviderConf
import io.dpopkov.knowthenixkbd.biz.repo.*
import io.dpopkov.knowthenixkbd.biz.stubs.*
import io.dpopkov.knowthenixkbd.biz.validation.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
import io.dpopkov.knowthenixkbd.common.models.KnthState
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.cor.ICorExec
import io.dpopkov.knowthenixkbd.cor.dsl.rootChain

class KnthTranslationProcessor(
    val corSettings: KnthCorSettings = KnthCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
) {
    suspend fun exec(ctx: KnthContext) {
        businessChain.exec(
            ctx.also { it.corSettings = this@KnthTranslationProcessor.corSettings }
        )
    }

    /** Содержит только вызовы бизнес-процессов */
    private val businessChain: ICorExec<KnthContext> = rootChain {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория", corSettings)

        operation("Создание перевода", KnthCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешного создания", corSettings)
                stubValidationBadLanguage("Имитация ошибки валидации языка")
                stubValidationBadContent("Имитация ошибки валидации содержимого")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем для валидации") { translationValidating = translationRequest.deepCopy() }
                worker("Удаление id") { translationValidating.id = KnthTranslationId.NONE }
                worker("Очистка языка") { translationValidating.language = translationValidating.language.trim() }
                worker("Очистка содержимого") { translationValidating.content = translationValidating.content.trim() }
                validateLanguageNotEmpty("Проверка что язык не пуст")
                validateLanguageHasText("Проверка символов")
                validateContentNotEmpty("Проверка что содержимое не пусто")
                validateContentHasText("Проверка символов")

                finishTranslationValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка перевода для сохранения")
                repoCreate("Создание перевода в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получение перевода", KnthCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешного получения", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем для валидации") { translationValidating = translationRequest.deepCopy() }
                worker("Очистка id") { translationValidating.id = translationValidating.id.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishTranslationValidation("Завершение проверок")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение перевода из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == KnthState.RUNNING }
                    handle { translationRepoDone = translationRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }
        operation("Изменить перевод", KnthCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешного изменения", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLanguage("Имитация ошибки валидации языка")
                stubValidationBadContent("Имитация ошибки валидации содержимого")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем для валидации") { translationValidating = translationRequest.deepCopy() }
                worker("Очистка id") { translationValidating.id = translationValidating.id.trim() }
                worker("Очистка lock") { translationValidating.lock = translationValidating.lock.trim() }
                worker("Очистка языка") { translationValidating.language = translationValidating.language.trim() }
                worker("Очистка содержимого") { translationValidating.content = translationValidating.content.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateLanguageNotEmpty("Проверка что язык не пуст")
                validateLanguageHasText("Проверка символов")
                validateContentNotEmpty("Проверка что содержимое не пусто")
                validateContentHasText("Проверка символов")

                finishTranslationValidation("Завершение проверок")
            }
            chain {
                title = "Логика изменения перевода"
                repoRead("Чтение перевода из БД")
                repoPrepareUpdate("Подготовка перевода для изменения")
                repoUpdate("Обновление перевода в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить перевод", KnthCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешного удаления", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем для валидации") { translationValidating = translationRequest.deepCopy() }
                worker("Очистка id") { translationValidating.id = translationValidating.id.trim() }
                worker("Очистка lock") { translationValidating.lock = translationValidating.lock.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")

                finishTranslationValidation("Завершение проверок")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение перевода из БД")
                repoPrepareDelete("Подготовка перевода для удаления")
                repoDelete("Удаление перевода из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск переводов", KnthCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешного поиска", corSettings)
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем для валидации") { translationFilterValidating = translationFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishTranslationFilterValidation("Завершение проверок")
            }
            repoSearch("Поиск переводов в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}
