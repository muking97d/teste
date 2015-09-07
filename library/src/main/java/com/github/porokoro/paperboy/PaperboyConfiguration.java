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

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.SparseArray;

class PaperboyConfiguration {
    private String                m_file;
    private int                   m_fileRes;
    private int                   m_viewType;
    private int                   m_sectionLayout;
    private int                   m_typeLayout;
    private int                   m_itemLayout;
    private boolean               m_sortItems;
    private SparseArray<ItemType> m_itemTypes;

    public PaperboyConfiguration() {
        m_file = "";
        m_viewType = ViewTypes.NONE;
        m_itemLayout = 0;
        m_sortItems = false;
        m_itemTypes = new SparseArray<>();
    }

    @Nullable
    public String getFile() {
        return m_file;
    }

    public void setFile(@Nullable String file) {
        m_file = file;
    }

    @RawRes
    public int getFileRes() {
        return m_fileRes;
    }

    public void setFileRes(@RawRes int fileRes) {
        m_fileRes = fileRes;
    }

    @ViewType
    public int getViewType() {
        return m_viewType;
    }

    public void setViewType(@ViewType int viewType) {
        m_viewType = viewType;
    }

    @LayoutRes
    public int getSectionLayout() {
        return m_sectionLayout;
    }

    public void setSectionLayout(@LayoutRes int sectionLayout) {
        m_sectionLayout = sectionLayout;
    }

    @LayoutRes
    public int getTypeLayout() {
        return m_typeLayout;
    }

    public void setTypeLayout(@LayoutRes int typeLayout) {
        m_typeLayout = typeLayout;
    }

    @LayoutRes
    public int getItemLayout() {
        return m_itemLayout;
    }

    public void setItemLayout(@LayoutRes int itemLayout) {
        m_itemLayout = itemLayout;
    }

    public boolean isSortItems() {
        return m_sortItems;
    }

    public void setSortItems(boolean sortItems) {
        m_sortItems = sortItems;
    }

    @NonNull
    public SparseArray<ItemType> getItemTypes() {
        return m_itemTypes;
    }

    public void setItemTypes(@NonNull SparseArray<ItemType> itemTypes) {
        m_itemTypes = itemTypes;
    }
}
