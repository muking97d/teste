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
package com.github.porokoro.paperboy.java

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.RawRes
import com.github.porokoro.paperboy.ItemType
import com.github.porokoro.paperboy.builders.PaperboyBuilder
import com.github.porokoro.paperboy.builders.buildPaperboy

class JavaPaperboyBuilder(private val context: Context) {
    private val builder = PaperboyBuilder()

    fun setFile(file: String?): JavaPaperboyBuilder {
        builder.file = file
        return this
    }

    fun setFileRes(@RawRes fileRes: Int): JavaPaperboyBuilder {
        builder.fileRes = fileRes
        return this
    }

    fun setViewType(viewType: Int): JavaPaperboyBuilder {
        builder.viewType = viewType
        return this
    }

    fun setSectionLayout(@LayoutRes sectionLayout: Int): JavaPaperboyBuilder {
        builder.sectionLayout = sectionLayout
        return this
    }

    fun setTypeLayout(@LayoutRes typeLayout: Int): JavaPaperboyBuilder {
        builder.typeLayout = typeLayout
        return this;
    }

    fun setItemLayout(@LayoutRes itemLayout: Int): JavaPaperboyBuilder {
        builder.itemLayout = itemLayout
        return this;
    }

    fun setSortItems(sortItems: Boolean): JavaPaperboyBuilder {
        builder.sortItems = sortItems
        return this;
    }

    fun addItemType(itemType: ItemType): JavaPaperboyBuilder {
        builder.itemTypes = builder.itemTypes + itemType
        return this;
    }

    fun buildFragment() = buildPaperboy(context) {
        file = builder.file
        fileRes = builder.fileRes
        viewType = builder.viewType
        sectionLayout = builder.sectionLayout
        typeLayout = builder.typeLayout
        itemLayout = builder.itemLayout
        sortItems = builder.sortItems
        itemTypes = builder.itemTypes
    }
}
