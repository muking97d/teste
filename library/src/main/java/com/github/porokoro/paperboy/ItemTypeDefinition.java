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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

class ItemTypeDefinition implements Parcelable {
    private final int    m_id;
    private final String m_name;
    private final String m_shorthand;
    private       String m_titleSingular;
    private       String m_titlePlural;
    @ColorInt
    private       int    m_color;
    @DrawableRes
    private       int    m_icon;
    private       int    m_sortOrder;

    public ItemTypeDefinition(int id, @NonNull String name, @NonNull String shorthand) {
        m_id = id;
        m_name = name;
        m_shorthand = shorthand;
        m_titleSingular = "";
        m_color = 0;
        m_icon = 0;
        m_sortOrder = Integer.MAX_VALUE;
    }

    public int getId() {
        return m_id;
    }

    @NonNull
    public String getShorthand() {
        return m_shorthand;
    }

    @NonNull
    public String getName() {
        return m_name;
    }

    @NonNull
    public String getTitleSingular() {
        return m_titleSingular;
    }

    public void setTitleSingular(@NonNull String title) {
        m_titleSingular = title;
    }

    @NonNull
    public String getTitlePlural() {
        return m_titlePlural;
    }

    public void setTitlePlural(@NonNull String titlePlural) {
        m_titlePlural = titlePlural;
    }

    @ColorInt
    public int getColor() {
        return m_color;
    }

    public void setColor(@ColorInt int color) {
        m_color = color;
    }

    @DrawableRes
    public int getIcon() {
        return m_icon;
    }

    public void setIcon(@DrawableRes int icon) {
        m_icon = icon;
    }

    public int getSortOrder() {
        return m_sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        m_sortOrder = sortOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeInt(m_id);
        out.writeString(m_name);
        out.writeString(m_shorthand);
        out.writeString(m_titleSingular);
        out.writeInt(m_color);
        out.writeInt(m_icon);
        out.writeInt(m_sortOrder);
    }

    public static final Parcelable.Creator<ItemTypeDefinition> CREATOR = new Parcelable.Creator<ItemTypeDefinition>() {
        @Override
        public ItemTypeDefinition createFromParcel(@NonNull Parcel in) {
            return new ItemTypeDefinition(in);
        }

        @NonNull
        @Override
        public ItemTypeDefinition[] newArray(int size) {
            return new ItemTypeDefinition[size];
        }
    };

    private ItemTypeDefinition(Parcel in) {
        m_id = in.readInt();
        m_name = in.readString();
        m_shorthand = in.readString();
        m_titleSingular = in.readString();
        m_color = in.readInt();
        m_icon = in.readInt();
        m_sortOrder = in.readInt();
    }
}
