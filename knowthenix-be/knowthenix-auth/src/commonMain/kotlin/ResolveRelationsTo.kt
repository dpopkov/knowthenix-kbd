package io.dpopkov.knowthenixkbd.auth

import io.dpopkov.knowthenixkbd.common.models.KnthTranslation
import io.dpopkov.knowthenixkbd.common.models.KnthTranslationId
import io.dpopkov.knowthenixkbd.common.models.KnthVisibility
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalModel
import io.dpopkov.knowthenixkbd.common.permissions.KnthPrincipalRelations

fun KnthTranslation.resolveRelationsTo(principal: KnthPrincipalModel): Set<KnthPrincipalRelations> =
    setOfNotNull(
        KnthPrincipalRelations.NONE,
        KnthPrincipalRelations.NEW.takeIf { this.id == KnthTranslationId.NONE },  // Это создание нового перевода
        KnthPrincipalRelations.OWN.takeIf { this.ownerId == principal.id },
        KnthPrincipalRelations.MODERATABLE.takeIf { this.visibility != KnthVisibility.VISIBLE_TO_OWNER },
        KnthPrincipalRelations.PUBLIC.takeIf { this.visibility == KnthVisibility.VISIBLE_PUBLIC },
    )
