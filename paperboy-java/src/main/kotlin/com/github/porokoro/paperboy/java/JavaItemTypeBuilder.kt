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
package com.github.porokoro.paperboy.java

import android.content.Context
import android.support.annotation.*
import com.github.porokoro.paperboy.builders.ItemTypeBuilder
import com.github.porokoro.paperboy.builders.buildItemType

class JavaItemTypeBuilder(private val context: Context, private val id: Int, private val  name: String,
                          private val  shorthand: String) {
    constructor(context: Context, id: Int, @StringRes name: Int, @StringRes shorthand: Int)
    : this(context, id, context.getString(name), context.getString(shorthand))

    private val builder = ItemTypeBuilder()


    fun setTitleSingular(title: String): JavaItemTypeBuilder {
        builder.titleSingular = title
        return this
    }

    fun setTitleSingular(@StringRes title: Int): JavaItemTypeBuilder {
        builder.titleSingularRes = title
        return this
    }

    fun setTitlePlural(title: String): JavaItemTypeBuilder {
        builder.titlePlural = title
        return this
    }

    fun setTitlePlural(@StringRes title: Int): JavaItemTypeBuilder {
        builder.titlePluralRes = title
        return this
    }

    fun setColor(@ColorInt color: Int): JavaItemTypeBuilder {
        builder.color = color
        return this
    }

    fun setColorRes(@ColorRes color: Int): JavaItemTypeBuilder {
        builder.colorRes = color
        return this
    }

    fun setIcon(@DrawableRes icon: Int): JavaItemTypeBuilder {
        builder.icon = icon
        return this
    }

    fun setSortOrder(sortOrder: Int): JavaItemTypeBuilder {
        builder.sortOrder = sortOrder
        return this
    }

    fun setSortOrderRes(@IntegerRes sortOrder: Int): JavaItemTypeBuilder {
        builder.sortOrderRes = sortOrder
        return this
    }

    fun build() = buildItemType(context, id, name, shorthand) {
        titleSingular = builder.titleSingular
        titleSingularRes = builder.titleSingularRes
        titlePlural = builder.titlePlural
        titlePluralRes = builder.titlePluralRes
        color = builder.color
        colorRes = builder.colorRes
        icon = builder.icon
        sortOrder = builder.sortOrder
        sortOrderRes = builder.sortOrderRes
    }
}
