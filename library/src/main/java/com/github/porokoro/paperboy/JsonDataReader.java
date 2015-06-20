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
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class JsonDataReader {
    private static final String SECTION_NAME  = "name";
    private static final String SECTION_ITEMS = "items";

    private static final String ITEM_TYPE        = "type";
    private static final String ITEM_TITLE       = "title";
    private static final String ITEM_DESCRIPTION = "description";

    private static final String FILE_ENCODING = "UTF-8";

    @NonNull
    public static List<PaperboySection> read(@NonNull InputStream input) {
        JsonReader reader = null;

        try {
            reader = new JsonReader(new InputStreamReader(input, FILE_ENCODING));
            return readSectionArray(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null)
                    reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>(0);
    }

    @NonNull
    private static List<PaperboySection> readSectionArray(@NonNull JsonReader reader) throws IOException {
        List<PaperboySection> sections = new ArrayList<>();
        reader.beginArray();

        while (reader.hasNext())
            sections.add(readSection(reader));

        reader.endArray();
        return sections;
    }

    @NonNull
    private static PaperboySection readSection(@NonNull JsonReader reader) throws IOException {
        PaperboySection section = new PaperboySection();
        reader.beginObject();

        while (reader.hasNext()) {
            String name = toLowerCase(reader.nextName());

            switch (name) {
                case SECTION_NAME:
                    section.setName(reader.nextString());
                    break;
                case SECTION_ITEMS:
                    section.setItems(readItemArray(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return section;
    }

    @NonNull
    private static List<PaperboyItem> readItemArray(@NonNull JsonReader reader) throws IOException {
        List<PaperboyItem> items = new ArrayList<>();
        reader.beginArray();

        while (reader.hasNext())
            items.add(readItem(reader));

        reader.endArray();
        return items;
    }

    @NonNull
    private static PaperboyItem readItem(@NonNull JsonReader reader) throws IOException {
        PaperboyItem item = new PaperboyItem();
        reader.beginObject();

        while (reader.hasNext()) {
            String name = toLowerCase(reader.nextName());

            switch (name) {
                case ITEM_TYPE:
                    JsonToken type = reader.peek();
                    if (type == JsonToken.NUMBER)
                        item.setType(ItemTypes.fromValue(reader.nextInt()));
                    else if (type == JsonToken.STRING)
                        item.setType(ItemTypes.fromValue(toLowerCase(reader.nextString())));
                    break;
                case ITEM_TITLE:
                    item.setTitle(reader.nextString());
                    break;
                case ITEM_DESCRIPTION:
                    item.setDescription(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return item;
    }

    @NonNull
    private static String toLowerCase(@NonNull String value) {
        return value.toLowerCase(Locale.getDefault());
    }
}
