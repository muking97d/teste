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
import android.support.annotation.*;

public class PaperboyBuilder {
    private final Context               m_context;
    private final PaperboyConfiguration m_configuration;

    public PaperboyBuilder(@NonNull Context context) {
        m_context = context;
        m_configuration = new PaperboyConfiguration();
    }

    @NonNull
    public PaperboyBuilder setFile(@Nullable String file) {
        m_configuration.setFile(file);
        m_configuration.setFileRes(0);
        return this;
    }

    @NonNull
    public PaperboyBuilder setFileRes(@RawRes int fileRes) {
        m_configuration.setFile(null);
        m_configuration.setFileRes(fileRes);
        return this;
    }

    @NonNull
    public PaperboyBuilder setViewType(@ViewType int viewType) {
        m_configuration.setViewType(viewType);
        return this;
    }

    @NonNull
    public PaperboyBuilder setSectionLayout(@LayoutRes int sectionLayout) {
        m_configuration.setSectionLayout(sectionLayout);
        return this;
    }

    @NonNull
    public PaperboyBuilder setTypeLayout(@LayoutRes int typeLayout) {
        m_configuration.setTypeLayout(typeLayout);
        return this;
    }

    @NonNull
    public PaperboyBuilder setItemLayout(@LayoutRes int itemLayout) {
        m_configuration.setItemLayout(itemLayout);
        return this;
    }

    @NonNull
    public PaperboyBuilder setSortItems(boolean sortItems) {
        m_configuration.setSortItems(sortItems);
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

        if (m_configuration.getItemTypes().get(DefaultItemTypes.FEATURE) == null)
            DefaultItemTypes.createFeature(m_context, this);
        if (m_configuration.getItemTypes().get(DefaultItemTypes.BUG) == null)
            DefaultItemTypes.createBug(m_context, this);
        if (m_configuration.getItemTypes().get(DefaultItemTypes.IMPROVEMENT) == null)
            DefaultItemTypes.createImprovement(m_context, this);

        Bundle args = new Bundle(1);
        args.putString(PaperboyFragment.ARG_CONFIG, ConfigurationSerializer.write(m_configuration));
        fragment.setArguments(args);

        return fragment;
    }

    void addDefinition(@NonNull ItemType definition) {
        m_configuration.getItemTypes().put(definition.getId(), definition);
    }
}
