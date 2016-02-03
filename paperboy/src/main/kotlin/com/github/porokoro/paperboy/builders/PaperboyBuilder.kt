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
package com.github.porokoro.paperboy.builders

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.RawRes
import android.util.SparseArray
import com.github.porokoro.paperboy.*

fun buildPaperboy(context: Context, func: PaperboyBuilder.() -> Unit): PaperboyFragment {
    val builder = PaperboyBuilder()
    builder.func()

    return build(context, builder)
}

private fun build(context: Context, builder: PaperboyBuilder): PaperboyFragment {
    val config = PaperboyConfiguration()
    config.file = if (builder.fileRes > 0) null else builder.file
    config.fileRes = builder.fileRes
    config.viewType = builder.viewType
    config.sectionLayout = builder.sectionLayout
    config.typeLayout = builder.typeLayout
    config.itemLayout = builder.itemLayout
    config.sortItems = builder.sortItems
    config.itemTypes = builder.itemTypes.let {
        val sparseArray = SparseArray<ItemType>(it.size)
        it.forEach { sparseArray.put(it.id, it) }
        sparseArray
    }

    if (config.itemTypes.get(DefaultItemTypes.FEATURE) == null) {
        val itemType = DefaultItemTypes.createFeature(context)
        config.itemTypes.put(itemType.id, itemType)
    }

    if (config.itemTypes.get(DefaultItemTypes.BUG) == null) {
        val itemType = DefaultItemTypes.createBug(context)
        config.itemTypes.put(itemType.id, itemType)
    }

    if (config.itemTypes.get(DefaultItemTypes.IMPROVEMENT) == null) {
        val itemType = DefaultItemTypes.createImprovement(context)
        config.itemTypes.put(itemType.id, itemType)
    }

    val fragment = PaperboyFragment()
    val args = Bundle(1)
    args.putString(PaperboyFragment.ARG_CONFIG, ConfigurationSerializer.write(config))
    fragment.arguments = args

    return fragment
}

class PaperboyBuilder {
    var file: String? = null
    var fileRes = 0
    var viewType = 0
    var sectionLayout = 0
    var typeLayout = 0
    var itemLayout = 0
    var sortItems = false
    var itemTypes = listOf<ItemType>()
}

class PaperboyChainBuilder(private val context: Context) {
    private val builder = PaperboyBuilder()

    fun setFile(file: String?): PaperboyChainBuilder {
        builder.file = file
        return this
    }

    fun setFileRes(@RawRes fileRes: Int): PaperboyChainBuilder {
        builder.fileRes = fileRes
        return this
    }

    fun setViewType(viewType: Int): PaperboyChainBuilder {
        builder.viewType = viewType
        return this
    }

    fun setSectionLayout(@LayoutRes sectionLayout: Int): PaperboyChainBuilder {
        builder.sectionLayout = sectionLayout
        return this
    }

    fun setTypeLayout(@LayoutRes typeLayout: Int): PaperboyChainBuilder {
        builder.typeLayout = typeLayout
        return this;
    }

    fun setItemLayout(@LayoutRes itemLayout: Int): PaperboyChainBuilder {
        builder.itemLayout = itemLayout
        return this;
    }

    fun setSortItems(sortItems: Boolean): PaperboyChainBuilder {
        builder.sortItems = sortItems
        return this;
    }

    fun addItemType(itemType: ItemType): PaperboyChainBuilder {
        builder.itemTypes = builder.itemTypes + itemType
        return this;
    }

    fun buildFragment() = build(context, builder)
}

