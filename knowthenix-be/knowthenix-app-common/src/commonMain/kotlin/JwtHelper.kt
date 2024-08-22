package io.dpopkov.knowthenixkbd.app.common

import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalModel
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserGroups
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val AUTH_HEADER = "x-jwt-payload"

private val jsMapper = Json {
    ignoreUnknownKeys = true
}

/**
 * Преобразует JWT строку, закодированную в base64, в объект Принципала.
 * Должен использоваться в контроллерах конкретного фреймворка при обработке каждой endpoint
 * для конвертации строки из <code>AUTH_HEADER</code> заголовка во внутреннюю модель Принципала,
 * которая далее сохраняется в Контексте текущего запроса.
 */
@OptIn(ExperimentalEncodingApi::class)
fun String?.jwt2principal(): KnthPrincipalModel = this?.let { jwtHeader: String ->
    val jwtJson: String = Base64.decode(jwtHeader).decodeToString()
    println("JWT JSON PAYLOAD: $jwtJson")
    val jwtObj: JwtPayload = jsMapper.decodeFromString(JwtPayload.serializer(), jwtJson)
    jwtObj.toPrincipal()
}
    ?: run {
        println("No jwt found in headers")
        KnthPrincipalModel.NONE
    }

@OptIn(ExperimentalEncodingApi::class)
fun KnthPrincipalModel.createJwtTestHeader(): String {
    val jwtObj: JwtPayload = fromPrincipal()
    val jwtJson: String = jsMapper.encodeToString(JwtPayload.serializer(), jwtObj)
    return Base64.encode(jwtJson.encodeToByteArray())
}

/**
 * Представление API (внешней) модели принципала.
 */
@Serializable
private data class JwtPayload(
    val aud: List<String>? = null,
    val sub: String? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("middle_name")
    val middleName: String? = null,
    val groups: List<String>? = null,
) {
    fun toPrincipal() = KnthPrincipalModel(
        id = sub?.let { KnthUserId(it) } ?: KnthUserId.NONE,
        fname = givenName ?: "",
        mname = middleName ?: "",
        lname = familyName ?: "",
        groups = groups?.mapNotNull { it.toPrincipalGroup() }?.toSet() ?: emptySet(),
    )
}

private fun String?.toPrincipalGroup(): KnthUserGroups? {
    val group = this?.trimStart('/')  // group может начинаться с '/'
    return when (group?.uppercase()) {
        "USER" -> KnthUserGroups.USER
        "ADMIN_TR" -> KnthUserGroups.ADMIN_TR
        "MODERATOR" -> KnthUserGroups.MODERATOR
        "TEST" -> KnthUserGroups.TEST
        "BAN_TR" -> KnthUserGroups.BAN_TR
        // TODO сделать обработку ошибок
        else -> null
    }
}

private fun KnthPrincipalModel.fromPrincipal(): JwtPayload = JwtPayload(
    sub = id.takeIf { it != KnthUserId.NONE }?.asString(),
    familyName = this.fname.takeIf { it.isNotBlank() },
    givenName = this.mname.takeIf { it.isNotBlank() },
    middleName = this.lname.takeIf { it.isNotBlank() },
    groups = this.groups.mapNotNull { it.fromPrincipalGroup() },
)

private fun KnthUserGroups?.fromPrincipalGroup(): String? = when (this) {
    KnthUserGroups.USER -> "USER"
    KnthUserGroups.ADMIN_TR -> "ADMIN_TR"
    KnthUserGroups.MODERATOR -> "MODERATOR"
    KnthUserGroups.TEST -> "TEST"
    KnthUserGroups.BAN_TR -> "BAN_TR"
    // TODO сделать обработку ошибок
    else -> null
}
