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
import com.github.porokoro.paperboy.ItemType;
import com.github.porokoro.paperboy.PaperboyFragment;
import com.github.porokoro.paperboy.ViewTypes;
import com.github.porokoro.paperboy.builders.ItemTypeBuilder;
import com.github.porokoro.paperboy.builders.ItemTypeBuilderKt;
import com.github.porokoro.paperboy.builders.PaperboyBuilder;
import com.github.porokoro.paperboy.builders.PaperboyBuilderKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class JavaSample {
    private JavaSample() {
    }

    @NotNull
    static PaperboyFragment buildDefault(@NotNull final Context context) {
        return PaperboyBuilderKt.buildPaperboy(context, new Function1<PaperboyBuilder, Unit>() {
            @Override
            public Unit invoke(PaperboyBuilder paperboyBuilder) {
                return null;
            }
        });
    }

    @NotNull
    static PaperboyFragment buildCustom(@NotNull final Context context) {
        return PaperboyBuilderKt.buildPaperboy(context, new Function1<PaperboyBuilder, Unit>() {
            @Override
            public Unit invoke(PaperboyBuilder paperboyBuilder) {
                paperboyBuilder.setViewType(ViewTypes.HEADER);
                paperboyBuilder.setSectionLayout(R.layout.view_section_custom);
                paperboyBuilder.setTypeLayout(R.layout.view_type_custom);
                paperboyBuilder.setItemLayout(R.layout.view_item_custom);
                paperboyBuilder.setSortItems(true);
                List<ItemType> itemTypes = new ArrayList<>();
                itemTypes.add(ItemTypeBuilderKt.buildItemType(context, 1000, "Custom", "C",
                        new Function1<ItemTypeBuilder, Unit>() {
                            @Override
                            public Unit invoke(ItemTypeBuilder itemTypeBuilder) {
                                itemTypeBuilder.setColorRes(R.color.item_type_custom);
                                itemTypeBuilder.setTitleSingularRes(R.string.item_type_custom);
                                itemTypeBuilder.setTitlePluralRes(R.string.item_type_customs);
                                itemTypeBuilder.setIcon(R.drawable.ic_build_black_24dp);
                                itemTypeBuilder.setSortOrder(0);
                                return null;
                            }
                        }));
                paperboyBuilder.setItemTypes(itemTypes);
                return null;
            }
        });
    }
}
