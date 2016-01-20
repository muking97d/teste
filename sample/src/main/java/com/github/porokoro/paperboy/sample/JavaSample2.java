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
package com.github.porokoro.paperboy.sample;

import android.content.Context;
import com.github.porokoro.paperboy.PaperboyFragment;
import com.github.porokoro.paperboy.ViewTypes;
import com.github.porokoro.paperboy.java.JavaItemTypeBuilder;
import com.github.porokoro.paperboy.java.JavaPaperboyBuilder;
import org.jetbrains.annotations.NotNull;

class JavaSample2 {
    private JavaSample2() {
    }

    @NotNull
    static PaperboyFragment buildDefault(@NotNull Context context) {
        return new JavaPaperboyBuilder(context).buildFragment();
    }

    @NotNull
    static PaperboyFragment buildCustom(@NotNull Context context) {
        return new JavaPaperboyBuilder(context)
                .setViewType(ViewTypes.HEADER)
                .setSectionLayout(R.layout.view_section_custom)
                .setTypeLayout(R.layout.view_type_custom)
                .setItemLayout(R.layout.view_item_custom)
                .setSortItems(true)
                .addItemType(new JavaItemTypeBuilder(context, 1000, "Custom", "C")
                        .setColorRes(R.color.item_type_custom)
                        .setTitleSingular(R.string.item_type_custom)
                        .setTitlePlural(R.string.item_type_customs)
                        .setIcon(R.drawable.ic_build_black_24dp)
                        .setSortOrder(0)
                        .build())
                .buildFragment();
    }


}
