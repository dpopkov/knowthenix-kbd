package io.dpopkov.knowthenixkbd.common.models

/**
 * Перечисление разрешений для клиента.
 * Формируется в бизнес-логике и отдается через маппер наружу,
 * чтобы клиентское приложение могло подстроить свой UI под разрешенные
 * действия.
 */
enum class KnthTranslationPermissionClient {
    READ,
    UPDATE,
    DELETE,
    MAKE_VISIBLE_PUBLIC,
    MAKE_VISIBLE_REGISTERED,
    MAKE_VISIBLE_OWNER,
}
