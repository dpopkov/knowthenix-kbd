package io.dpopkov.knowthenixkbd.common

import io.dpopkov.knowthenixkbd.common.models.*
import io.dpopkov.knowthenixkbd.common.stubs.KnthStubs
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

    /** Режим работы приложения для переключения между разными видами запросов */
    var workMode: KnthWorkMode = KnthWorkMode.PROD,
    /** Стаб используемый только в режиме стабов */
    var stubCase: KnthStubs = KnthStubs.NONE,

    /** Идентификатор запроса */
    var requestId: KnthRequestId = KnthRequestId.NONE,
    /** Момент времени начала формирования контекста */
    var timeStart: Instant = Instant.NONE,
    /** Входной запрос */
    var translationRequest: KnthTranslation = KnthTranslation(),
    /** Критерии фильтрации переводов  */
    var translationFilterRequest: KnthTranslationFilter = KnthTranslationFilter(),

    /** Формируемый ответ содержащий единичный перевод */
    var translationResponse: KnthTranslation = KnthTranslation(),
    /** Формируемый ответ содержащий список переводов */
    var translationsResponse: MutableList<KnthTranslation> = mutableListOf(),
)