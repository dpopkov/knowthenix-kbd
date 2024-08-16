package io.dpopkov.knowthenixkbd.biz.exception

import io.dpopkov.knowthenixkbd.common.models.KnthWorkMode

class KnthTranslationDbNotConfiguredException(
    val workMode: KnthWorkMode
) : Exception("Database is not configured properly for work mode $workMode")
