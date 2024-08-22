package io.dpopkov.knowthenixkbd.common

import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalModel
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserPermissions
import io.dpopkov.knowthenixkbd.common.repo.IRepoTranslation
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
import io.dpopkov.knowthenixkbd.common.ws.IKnthWsSession
import kotlinx.datetime.Instant

/**
 * Контекст - это контейнер для внутренних моделей, настроек и параметров обработки запроса
 * (флагов, статусов и переменных).
 * Участвует в шаблоне бизнес-логики "Chain or Responsibility" как центральная сущность,
 * которая передается между обработчиками.
 * Контекст являеется mutable, потому что обработчики последовательные, а не параллельные.
 * Формируется в точке входа (контроллере), где происходит преобразование входной транспортной модели
 * путем вызова маппера. Далее контекст передается в бизнес-логику, где проходит через цепь обработчиков.
 * В точке выхода последний обработчик возвращает обогащенный контекст, где обратный маппер создает
 * выходную транспортную модель.
 *
 * Все поля контекста должны быть non-nullable и иметь default значения.
 * Контекст ничего не знает о моделях других уровней (транспортные и модели хранения),
 * это зона ответственности мапперов.
 */
data class KnthContext(
    /** Команда указывается контроллером в момент формирования контекста. */
    var command: KnthCommand = KnthCommand.NONE,
    /** Состояние обработки */
    var state: KnthState = KnthState.NONE,
    /** Ошибки добавляемые обработчиками */
    val errors: MutableList<KnthError> = mutableListOf(),

    var corSettings: KnthCorSettings = KnthCorSettings(),
    /** Режим работы приложения для переключения между разными видами запросов */
    var workMode: KnthWorkMode = KnthWorkMode.PROD,
    /** Стаб используемый только в режиме стабов */
    var stubCase: KnthStubs = KnthStubs.NONE,

    /** Принципал осуществляющий запрос */
    var principal: KnthPrincipalModel = KnthPrincipalModel.NONE,
    /** Разрешения для принципала определенные на основании его групп (ролей) */
    val permissionsChain: MutableSet<KnthUserPermissions> = mutableSetOf(),
    /** Итоговый флаг разрешения выполнения принципалом запрашиваемой операции */
    var permitted: Boolean = false,

    /** Информация о текущей websocket сессии, если она есть. */
    var wsSession: IKnthWsSession = IKnthWsSession.NONE,

    /** Идентификатор запроса */
    var requestId: KnthRequestId = KnthRequestId.NONE,
    /** Момент времени начала формирования контекста */
    var timeStart: Instant = Instant.NONE,
    /** Входной запрос */
    var translationRequest: KnthTranslation = KnthTranslation(),
    /** Критерии фильтрации переводов  */
    var translationFilterRequest: KnthTranslationFilter = KnthTranslationFilter(),

    /** Промежуточная копия запроса для текущих проверок во время валидации */
    var translationValidating: KnthTranslation = KnthTranslation(),
    /** Промежуточная копия критериев фильтрации для текущих проверок во время валидации */
    var translationFilterValidating: KnthTranslationFilter = KnthTranslationFilter(),

    /** Провалидированная копия запроса, либо пустой объект, если валидация не пройдена */
    var translationValidated: KnthTranslation = KnthTranslation(),
    /** Провалидированная копия критериев фильтрации, либо пустой фильтр, если валидация не пройдена */
    var translationFilterValidated: KnthTranslationFilter = KnthTranslationFilter(),

    /** Рабочий репозиторий, инициализируется в зависимости от режима workMode */
    var translationRepo: IRepoTranslation = IRepoTranslation.NONE,
    /** То, что прочитали из репозитория */
    var translationRepoRead: KnthTranslation = KnthTranslation(),
    /** То, что готовим для сохранения в БД */
    var translationRepoPrepare: KnthTranslation = KnthTranslation(),
    /** Результат, полученный из БД */
    var translationRepoDone: KnthTranslation = KnthTranslation(),
    /** Результат - список, полученный из БД */
    var translationsRepoDone: MutableList<KnthTranslation> = mutableListOf(),

    /** Формируемый ответ содержащий единичный перевод */
    var translationResponse: KnthTranslation = KnthTranslation(),
    /** Формируемый ответ содержащий список переводов */
    var translationsResponse: MutableList<KnthTranslation> = mutableListOf(),
) {

    private fun addError(error: KnthError) {
        this.errors.add(error)
    }

    private fun addErrors(errors: Collection<KnthError>) {
        this.errors.addAll(errors)
    }

    fun fail(error: KnthError) {
        addError(error)
        this.state = KnthState.FAILING
    }

    fun fail(errors: Collection<KnthError>) {
        addErrors(errors)
        this.state = KnthState.FAILING
    }

    fun errorsAsString() = buildString {
        append("Errors in context:\n")
        errors.forEachIndexed { index, err ->
            append("${index}: $err\n")
        }
    }
}
