package io.dpopkov.knowthenixkbd.m1l5.builders

import kotlin.test.Test
import kotlin.test.assertEquals

class JavaBuilderTestCase {
    @Test
    fun `test building java-like breakfast`() {
        val breakfast: Meal.Breakfast = BreakfastBuilder()
            .withEggs(3)
            .withBacon(true)
            .withTitle("Simple")
            .withDrink(Drink.COFFEE)
            .build()

        assertEquals(3, breakfast.eggs)
        assertEquals(true, breakfast.bacon)
        assertEquals("Simple", breakfast.title)
        assertEquals(Drink.COFFEE, breakfast.drink)
    }

    private enum class Drink {
        WATER,
        COFFEE,
        TEA,
        NONE,
    }

    private abstract class Meal {
        data class Breakfast(
            val eggs: Int,
            val bacon: Boolean,
            val drink: Drink,
            val title: String,
        ) : Meal()

        data class Dinner(
            val title: String,
        ) : Meal()
    }

    private class BreakfastBuilder {
        private var _eggs = 0
        private var _bacon = false
        private var _drink = Drink.NONE
        private var _title = ""

        fun withEggs(eggs: Int): BreakfastBuilder {
            _eggs = eggs
            return this
        }

        fun withBacon(bacon: Boolean): BreakfastBuilder {
            _bacon = bacon
            return this
        }

        fun withTitle(title: String): BreakfastBuilder {
            _title = title
            return this
        }

        fun withDrink(drink: Drink): BreakfastBuilder {
            _drink = drink
            return this
        }

        fun build() = Meal.Breakfast(_eggs, _bacon, _drink, _title)
    }
}
