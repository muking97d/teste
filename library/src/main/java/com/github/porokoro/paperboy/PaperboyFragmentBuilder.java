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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.SparseArray;

public class PaperboyFragmentBuilder {
    private final Context                         m_context;
    private final Bundle                          m_arguments;
    private final SparseArray<ItemTypeDefinition> m_definitions;

    public PaperboyFragmentBuilder(@NonNull Context context) {
        m_context = context;
        m_arguments = new Bundle();
        m_definitions = new SparseArray<>();
    }

    @NonNull
    public PaperboyFragmentBuilder setFile(@Nullable String file) {
        m_arguments.putString(PaperboyFragment.ARG_FILE, file);
        return this;
    }

    @NonNull
    public PaperboyFragmentBuilder setViewType(@ViewType int viewType) {
        m_arguments.putInt(PaperboyFragment.ARG_VIEW_TYPE, viewType);
        return this;
    }

    @NonNull
    public PaperboyFragmentBuilder setSortItems(boolean sortItems) {
        m_arguments.putBoolean(PaperboyFragment.ARG_SORT_ITEMS, sortItems);
        return this;
    }

    @NonNull
    public ItemTypeBuilder withDefinition(int id, @NonNull String name, @NonNull String shorthand) {
        return new ItemTypeBuilder(m_context, this, id, name, shorthand);
    }

    @NonNull
    public ItemTypeBuilder withDefinition(int id, @StringRes int name, @StringRes int shorthand) {
        return new ItemTypeBuilder(m_context, this, id, m_context.getString(name), m_context.getString(shorthand));
    }

    @NonNull
    public PaperboyFragment build() {
        PaperboyFragment fragment = new PaperboyFragment();

        if (m_definitions.get(DefaultItemTypes.FEATURE) == null)
            DefaultItemTypes.createFeature(m_context, this);
        if (m_definitions.get(DefaultItemTypes.BUG) == null)
            DefaultItemTypes.createBug(m_context, this);
        if (m_definitions.get(DefaultItemTypes.IMPROVEMENT) == null)
            DefaultItemTypes.createImprovement(m_context, this);

        m_arguments.putSparseParcelableArray(PaperboyFragment.ARG_DEFINITIONS, m_definitions);
        fragment.setArguments(m_arguments);

        return fragment;
    }

    void addDefinition(@NonNull ItemTypeDefinition definition) {
        m_definitions.put(definition.getId(), definition);
    }
}
