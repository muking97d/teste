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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.*;

public class ItemTypeBuilder {
    private final Context  m_context;
    private final ItemType m_definition;

    public ItemTypeBuilder(@NonNull Context context, int id, @NonNull String name, @NonNull String shorthand) {
        m_context = context;
        m_definition = new ItemType(id, name, shorthand);
    }

    public ItemTypeBuilder(@NonNull Context context, int id, @StringRes int name, @StringRes int shorthand) {
        m_context = context;
        m_definition = new ItemType(id, context.getString(name), context.getString(shorthand));
    }

    @NonNull
    public ItemTypeBuilder setTitleSingular(@NonNull String title) {
        m_definition.setTitleSingular(title);
        return this;
    }

    @NonNull
    public ItemTypeBuilder setTitleSingular(@StringRes int title) {
        m_definition.setTitleSingular(m_context.getString(title));
        return this;
    }

    @NonNull
    public ItemTypeBuilder setTitlePlural(@NonNull String title) {
        m_definition.setTitlePlural(title);
        return this;
    }

    @NonNull
    public ItemTypeBuilder setTitlePlural(@StringRes int title) {
        m_definition.setTitlePlural(m_context.getString(title));
        return this;
    }

    @NonNull
    public ItemTypeBuilder setColor(@ColorInt int color) {
        m_definition.setColor(color);
        return this;
    }

    @NonNull
    public ItemTypeBuilder setColorRes(@ColorRes int color) {
        m_definition.setColor(getColorCompat(color));
        return this;
    }

    @NonNull
    public ItemTypeBuilder setIcon(@DrawableRes int icon) {
        m_definition.setIcon(icon);
        return this;
    }

    @NonNull
    public ItemTypeBuilder setSortOrder(int sortOrder) {
        m_definition.setSortOrder(sortOrder);
        return this;
    }

    @NonNull
    public ItemTypeBuilder setSortOrderRes(@IntegerRes int sortOrder) {
        m_definition.setSortOrder(m_context.getResources().getInteger(sortOrder));
        return this;
    }

    @NonNull
    public ItemType build() {
        return m_definition;
    }

    @TargetApi(23)
    @SuppressWarnings("deprecation")
    private int getColorCompat(@ColorRes int color) {
        if (Build.VERSION.SDK_INT < 23)
            //noinspection deprecation
            return m_context.getResources().getColor(color);

        return m_context.getColor(color);
    }
}
