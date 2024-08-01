package io.dpopkov.knowthenixkbd.biz

import io.dpopkov.knowthenixkbd.biz.plugins.getLoggerProviderConf
import io.dpopkov.knowthenixkbd.biz.stubs.*
import io.dpopkov.knowthenixkbd.common.KnthContext
import io.dpopkov.knowthenixkbd.common.KnthCorSettings
import io.dpopkov.knowthenixkbd.common.models.KnthCommand
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

        operation("Создание перевода", KnthCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешного создания", corSettings)
                stubValidationBadLanguage("Имитация ошибки валидации языка")
                stubValidationBadContent("Имитация ошибки валидации содержимого")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Получение перевода", KnthCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешного получения", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
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
        }
        operation("Удалить перевод", KnthCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешного удаления", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск переводов", KnthCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешного поиска", corSettings)
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}
