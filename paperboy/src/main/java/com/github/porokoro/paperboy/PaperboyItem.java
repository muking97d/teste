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

import android.support.annotation.NonNull;

class PaperboyItem {
    private int    m_type;
    private String m_title;
    private String m_description;

    public PaperboyItem() {
        m_type = DefaultItemTypes.NONE;
        m_title = "";
        m_description = "";
    }

    public int getType() {
        return m_type;
    }

    public void setType(int type) {
        m_type = type;
    }

    @NonNull
    public String getTitle() {
        return m_title;
    }

    public void setTitle(@NonNull String title) {
        m_title = title;
    }

    @NonNull
    public String getDescription() {
        return m_description;
    }

    public void setDescription(@NonNull String description) {
        m_description = description;
    }
}
