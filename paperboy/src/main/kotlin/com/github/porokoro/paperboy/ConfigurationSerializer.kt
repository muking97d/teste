/*
 * Copyright (C) 2015-2016 porokoro
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
import android.util.JsonWriter
import android.util.SparseArray
import com.github.porokoro.paperboy.extensions.iterator
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter

internal object ConfigurationSerializer {

    fun write(config: PaperboyConfiguration): String {
        val stringWriter = StringWriter()

        try {
            JsonWriter(stringWriter).use {
                writeConfiguration(it, config)
                stringWriter.flush()
            }
        } catch(e: IOException) {
            e.printStackTrace()
        }

        return stringWriter.toString()
    }

    private fun writeConfiguration(writer: JsonWriter, config: PaperboyConfiguration) {
        writer.beginObject()
                .name("file").value(config.file)
                .name("fileRes").value(config.fileRes)
                .name("viewType").value(config.viewType)
                .name("sectionLayout").value(config.sectionLayout)
                .name("typeLayout").value(config.typeLayout)
                .name("itemLayout").value(config.itemLayout)
                .name("sortItems").value(config.sortItems)
                .name("itemTypes").writeItemTypes(config.itemTypes)
                .endObject()
    }

    private fun JsonWriter.writeItemTypes(itemTypes: SparseArray<ItemType>): JsonWriter {
        beginArray()

        for ((key, value) in itemTypes) {
            beginObject()
                    .name("key").value(key)
                    .name("value").writeItemType(value)
                    .endObject()
        }

        return endArray()
    }

    private fun JsonWriter.writeItemType(itemType: ItemType) =
            beginObject()
                    .name("id").value(itemType.id)
                    .name("name").value(itemType.name)
                    .name("shorthand").value(itemType.shorthand)
                    .name("titleSingular").value(itemType.titleSingular)
                    .name("titlePlural").value(itemType.titlePlural)
                    .name("color").value(itemType.color)
                    .name("icon").value(itemType.icon)
                    .name("sortOrder").value(itemType.sortOrder)
                    .endObject()

    fun read(input: String): PaperboyConfiguration =
            try {
                JsonReader(StringReader(input)).use {
                    readConfiguration(it)
                }
            } catch(e: IOException) {
                e.printStackTrace()
                PaperboyConfiguration()
            }

    private fun readConfiguration(reader: JsonReader): PaperboyConfiguration {
        val config = PaperboyConfiguration()
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "file" -> config.file = reader.nextStringOrNull()
                "fileRes" -> config.fileRes = reader.nextInt()
                "viewType" -> config.viewType = ViewTypes.fromValue(reader.nextInt())
                "sectionLayout" -> config.sectionLayout = reader.nextInt()
                "typeLayout" -> config.typeLayout = reader.nextInt()
                "itemLayout" -> config.itemLayout = reader.nextInt()
                "sortItems" -> config.sortItems = reader.nextBoolean()
                "itemTypes" -> config.itemTypes = readItemTypes(reader)
                else -> reader.skipValue()
            }
        }

        reader.endObject()
        return config
    }

    private fun readItemTypes(reader: JsonReader): SparseArray<ItemType> {
        val itemTypes = SparseArray<ItemType>()
        reader.beginArray()

        while (reader.hasNext()) {
            var key = 0
            var value: ItemType? = null
            reader.beginObject()

            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "key" -> key = reader.nextInt()
                    "value" -> value = readItemType(reader)
                    else -> reader.skipValue()
                }
            }

            itemTypes.put(key, value)
            reader.endObject()
        }


        reader.endArray()
        return itemTypes
    }

    private fun readItemType(reader: JsonReader): ItemType {
        val itemType = ItemType()
        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> itemType.id = reader.nextInt()
                "name" -> itemType.name = reader.nextString()
                "shorthand" -> itemType.shorthand = reader.nextString()
                "titleSingular" -> itemType.titleSingular = reader.nextString()
                "titlePlural" -> itemType.titlePlural = reader.nextString()
                "color" -> itemType.color = reader.nextInt()
                "icon" -> itemType.icon = reader.nextInt()
                "sortOrder" -> itemType.sortOrder = reader.nextInt()
                else -> reader.skipValue()
            }
        }

        reader.endObject()
        return itemType
    }

    private fun JsonReader.nextStringOrNull(): String? =
            if (peek() == JsonToken.STRING)
                nextString()
            else {
                nextNull()
                null
            }
}
