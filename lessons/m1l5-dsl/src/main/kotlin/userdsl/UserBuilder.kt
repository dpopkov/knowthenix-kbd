package io.dpopkov.knowthenixkbd.m1l5.userdsl

import io.dpopkov.knowthenixkbd.m1l5.Action
import io.dpopkov.knowthenixkbd.m1l5.User
import java.time.LocalDateTime
import java.util.UUID

@UserDsl
class UserBuilder {
    private var id = UUID.randomUUID().toString()

    private var firstName = ""
    private var secondName = ""
    private var lastName = ""

    private var phone = ""
    private var email = ""

    private var actions: Set<Action> = emptySet()

    private var availability: List<LocalDateTime> = emptyList()

    fun name(block: NameContext.() -> Unit) {
        val ctx = NameContext().apply(block)
        firstName = ctx.first
        secondName = ctx.second
        lastName = ctx.last
    }

    fun contacts(block: ContactsContext.() -> Unit) {
        val ctx = ContactsContext().apply(block)
        phone = ctx.phone
        email = ctx.email
    }

    fun actions(block: ActionsContext.() -> Unit) {
        val ctx = ActionsContext().apply(block)
        actions = ctx.build()
    }

    fun availability(block: AvailabilityContext.() -> Unit) {
        val ctx = AvailabilityContext().apply(block)
        availability = ctx.build()
    }

    fun build(): User {
        return User(
            id = id,
            firstName = firstName,
            secondName = secondName,
            lastName = lastName,
            phone = phone,
            email = email,
            actions = actions,
            available = availability,
        )
    }
}
