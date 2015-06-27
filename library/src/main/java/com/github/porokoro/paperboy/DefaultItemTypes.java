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
package com.github.porokoro.paperboy;

import android.content.Context;
import android.support.annotation.NonNull;

class DefaultItemTypes {
    public static final int NONE        = 0;
    public static final int FEATURE     = 1;
    public static final int BUG         = 2;
    public static final int IMPROVEMENT = 3;

    private DefaultItemTypes() {
    }

    public static void createFeature(@NonNull Context context, @NonNull PaperboyFragmentBuilder builder) {
        new ItemTypeBuilder(context, builder, FEATURE, "Feature", "F")
                .setTitleSingular(R.string.paperboy_item_type_feature)
                .setTitlePlural(R.string.paperboy_item_type_features)
                .setColorRes(R.color.paperboy_light_item_type_feature)
                .setIcon(R.drawable.paperboy_ic_done)
                .add();
    }

    public static void createBug(@NonNull Context context, @NonNull PaperboyFragmentBuilder builder) {
        new ItemTypeBuilder(context, builder, BUG, "Bug", "B")
                .setTitleSingular(R.string.paperboy_item_type_bug)
                .setTitlePlural(R.string.paperboy_item_type_bugs)
                .setColorRes(R.color.paperboy_light_item_type_bug)
                .setIcon(R.drawable.paperboy_ic_bug_report)
                .add();
    }

    public static void createImprovement(@NonNull Context context, @NonNull PaperboyFragmentBuilder builder) {
        new ItemTypeBuilder(context, builder, IMPROVEMENT, "Improvement", "I")
                .setTitleSingular(R.string.paperboy_item_type_improvement)
                .setTitlePlural(R.string.paperboy_item_type_improvements)
                .setColorRes(R.color.paperboy_light_item_type_improvement)
                .setIcon(R.drawable.paperboy_ic_trending_up)
                .add();
    }
}
