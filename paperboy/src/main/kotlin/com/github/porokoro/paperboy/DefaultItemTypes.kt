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

import android.content.Context
import com.github.porokoro.paperboy.builders.buildItemType

internal object DefaultItemTypes {
    val NONE = 0
    val FEATURE = 1
    val BUG = 2
    val IMPROVEMENT = 3

    fun createFeature(context: Context) =
            buildItemType(context, FEATURE, "Feature", "F") {
                titleSingularRes = R.string.paperboy_item_type_feature
                titlePluralRes = R.string.paperboy_item_type_features
                colorRes = R.color.paperboy_item_type_feature
                icon = R.drawable.ic_done_black_24dp
                sortOrder = 0
            }

    fun createBug(context: Context) =
            buildItemType(context, BUG, "Bug", "B") {
                titleSingularRes = R.string.paperboy_item_type_bug
                titlePluralRes = R.string.paperboy_item_type_bugs
                colorRes = R.color.paperboy_item_type_bug
                icon = R.drawable.ic_bug_report_black_24dp
                sortOrder = 1
            }

    fun createImprovement(context: Context) =
            buildItemType(context, IMPROVEMENT, "Improvement", "I") {
                titleSingularRes = R.string.paperboy_item_type_improvement
                titlePluralRes = R.string.paperboy_item_type_improvements
                colorRes = R.color.paperboy_item_type_improvement
                icon = R.drawable.ic_trending_up_black_24dp
                sortOrder = 2
            }
}
