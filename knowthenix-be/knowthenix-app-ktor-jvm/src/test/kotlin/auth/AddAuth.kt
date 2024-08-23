package io.dpopkov.knowthenixkbd.app.ktorjvm.auth

import io.dpopkov.knowthenixkbd.app.common.AUTH_HEADER
import io.dpopkov.knowthenixkbd.app.common.createJwtTestHeader
import io.dpopkov.knowthenixkbd.common.models.KnthUserId
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalModel
import io.dpopkov.knowthenixkbd.common.permissions.KnthUserGroups
import io.dpopkov.knowthenixkbd.stubs.KnthTranslationStubItems
import io.ktor.client.request.*

fun HttpRequestBuilder.addAuth(
    id: KnthUserId = KnthTranslationStubItems.TRANSLATION_EN.ownerId,
    groups: Collection<KnthUserGroups> = listOf(
        KnthUserGroups.TEST,
        KnthUserGroups.USER,
    )
) {
    val principal = KnthPrincipalModel(
        id = id,
        groups = groups.toSet(),
    )
    header(AUTH_HEADER, principal.createJwtTestHeader())
}
