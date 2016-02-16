/*
 * Copyright (C) 2015-2016 Dominik Hibbeln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.porokoro.paperboy

import android.util.JsonReader
import android.util.JsonToken
import android.util.SparseArray
import com.github.porokoro.paperboy.extensions.firstOrNull
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal class JsonDataReader(private val definitions: SparseArray<ItemType>) {
    companion object {
        val SECTION_NAME = "name"
        val SECTION_ITEMS = "items"

        val ITEM_TYPE = "type"
        val ITEM_TITLE = "title"
        val ITEM_DESCRIPTION = "description"
    }

    fun read(input: InputStream): List<PaperboySection> =
            try {
                JsonReader(InputStreamReader(input, Charsets.UTF_8)).use {
                    readSectionArray(it)
                }
            } catch(e: IOException) {
                e.printStackTrace()
                mutableListOf()
            }

    private fun readSectionArray(reader: JsonReader) =
            reader.readArray { readSection(it) }

    private fun readSection(reader: JsonReader): PaperboySection {
        val section = PaperboySection()
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName().toLowerCase()) {
                SECTION_NAME -> section.name = reader.nextString()
                SECTION_ITEMS -> section.items = readItemArray(reader)
                else -> reader.skipValue()
            }
        }

        reader.endObject()
        return section
    }

    private fun readItemArray(reader: JsonReader) =
            reader.readArray { readItem(it) }

    private fun readItem(reader: JsonReader): PaperboyItem {
        val item = PaperboyItem()
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName().toLowerCase()) {
                ITEM_TYPE -> {
                    item.type = when (reader.peek()) {
                        JsonToken.NUMBER -> definitions.get(reader.nextInt())?.id ?: DefaultItemTypes.NONE
                        JsonToken.STRING -> reader.nextString().toLowerCase().let {
                            definitions.firstOrNull { def ->
                                def.second.shorthand.toLowerCase() == it || def.second.name.toLowerCase() == it
                            }?.second?.id ?: DefaultItemTypes.NONE
                        }
                        else -> DefaultItemTypes.NONE
                    }
                }
                ITEM_TITLE -> item.title = reader.nextString()
                ITEM_DESCRIPTION -> item.description = reader.nextString()
                else -> reader.skipValue()
            }
        }

        reader.endObject()
        return item
    }

    private fun <T> JsonReader.readArray(read: (JsonReader) -> T): List<T> {
        val array = mutableListOf<T>()
        beginArray()

        while (hasNext())
            array.add(read(this))

        endArray()
        return array
    }
}
