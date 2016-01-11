/*
 * Copyright (C) 2015 porokoro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.porokoro.paperboy.builders

import android.content.Context
import android.support.v4.content.ContextCompat
import com.github.porokoro.paperboy.ItemType

fun buildItemType(context: Context, id: Int, name: String, shorthand: String,
                  func: ItemTypeBuilder.() -> Unit): ItemType {
    val itemType = ItemType(id, name, shorthand)
    val builder = ItemTypeBuilder()
    builder.func()

    itemType.titleSingular =
            if (builder.titleSingularRes > 0) context.getString(builder.titleSingularRes)
            else builder.titleSingular
    itemType.titlePlural =
            if (builder.titlePluralRes > 0) context.getString(builder.titlePluralRes)
            else builder.titlePlural
    itemType.color =
            if (builder.colorRes > 0) ContextCompat.getColor(context, builder.colorRes)
            else builder.color
    itemType.icon = builder.icon
    itemType.sortOrder =
            if (builder.sortOrderRes > 0) context.resources.getInteger(builder.sortOrderRes)
            else builder.sortOrder

    return itemType
}

class ItemTypeBuilder {
    var titleSingular = ""
    var titleSingularRes = 0
    var titlePlural = ""
    var titlePluralRes = 0
    var color = 0
    var colorRes = 0
    var icon = 0
    var sortOrder = 0
    var sortOrderRes = 0
}
