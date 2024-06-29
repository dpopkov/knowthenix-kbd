package io.dpopkov.knowthenixkbd.m2l1.homework.hard

import io.dpopkov.knowthenixkbd.m2l1.homework.hard.dto.Dictionary
import io.dpopkov.knowthenixkbd.m2l1.homework.hard.dto.Meaning
import java.io.File
import kotlin.test.Test

class HWHard {
    @Test
    fun hardHw() {
        val dictionaryApi = DictionaryApi()
        val words: Set<String> = FileReader.readFile().split(" ", "\n").toSet()

        val dictionaries: List<Dictionary?> = findWords(dictionaryApi, words, Locale.EN)

        dictionaries.filterNotNull().map { dictionary ->
            print("For word ${dictionary.word} i found examples: ")
            println(
                dictionary.meanings
                    .mapNotNull { definition: Meaning ->
                        val r: List<String>? = definition.definitions
                            .mapNotNull { it.example.takeIf { it?.isNotBlank() == true } }
                            .takeIf { it.isNotEmpty() }
                        r
                    }
                    .takeIf { it.isNotEmpty() }
            )
        }
    }

    private fun findWords(
        dictionaryApi: DictionaryApi,
        words: Set<String>,
        @Suppress("SameParameterValue") locale: Locale
    ): List<Dictionary?> =
        // make some suspensions and async
        words.map {
            dictionaryApi.findWord(locale, it)
        }

    object FileReader {
        fun readFile(): String =
            File(
                this::class.java.classLoader.getResource("words.txt")?.toURI()
                    ?: throw RuntimeException("Can't read file")
            ).readText()
    }
}