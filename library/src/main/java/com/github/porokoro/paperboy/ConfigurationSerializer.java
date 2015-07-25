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
import android.util.JsonWriter;
import android.util.SparseArray;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

class ConfigurationSerializer {

    @NonNull
    public static String write(@NonNull PaperboyConfiguration config) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        try {
            writeConfiguration(writer, config);
            stringWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringWriter.toString();
    }

    private static void writeConfiguration(@NonNull JsonWriter writer, @NonNull PaperboyConfiguration config)
            throws IOException {
        writer.beginObject()
              .name("file").value(config.getFile())
              .name("viewType").value(config.getViewType())
              .name("itemLayout").value(config.getItemLayout())
              .name("sortItems").value(config.isSortItems())
              .name("itemTypes");

        writeItemTypes(writer, config.getItemTypes());

        writer.endObject();
    }

    private static void writeItemTypes(@NonNull JsonWriter writer, @NonNull SparseArray<ItemType> itemTypes)
            throws IOException {
        writer.beginArray();

        for (int i = 0; i < itemTypes.size(); i++) {
            writer.beginObject()
                  .name("key").value(itemTypes.keyAt(i))
                  .name("value");

            writeItemType(writer, itemTypes.valueAt(i));
            writer.endObject();
        }

        writer.endArray();
    }

    private static void writeItemType(@NonNull JsonWriter writer, @NonNull ItemType itemType) throws IOException {
        writer.beginObject()
              .name("id").value(itemType.getId())
              .name("name").value(itemType.getName())
              .name("shorthand").value(itemType.getShorthand())
              .name("titleSingular").value(itemType.getTitleSingular())
              .name("titlePlural").value(itemType.getTitlePlural())
              .name("color").value(itemType.getColor())
              .name("icon").value(itemType.getIcon())
              .name("sortOrder").value(itemType.getSortOrder())
              .endObject();
    }

    @NonNull
    public static PaperboyConfiguration read(@NonNull String input) {
        StringReader stringReader = new StringReader(input);
        JsonReader reader = new JsonReader(stringReader);

        try {
            return readConfiguration(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new PaperboyConfiguration();
    }

    @NonNull
    private static PaperboyConfiguration readConfiguration(@NonNull JsonReader reader) throws IOException {
        PaperboyConfiguration config = new PaperboyConfiguration();
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "file":
                    config.setFile(reader.nextString());
                    break;
                case "viewType":
                    config.setViewType(ViewTypes.fromValue(reader.nextInt()));
                    break;
                case "itemLayout":
                    config.setItemLayout(reader.nextInt());
                    break;
                case "sortItems":
                    config.setSortItems(reader.nextBoolean());
                    break;
                case "itemTypes":
                    config.setItemTypes(readItemTypes(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return config;
    }

    @NonNull
    private static SparseArray<ItemType> readItemTypes(@NonNull JsonReader reader) throws IOException {
        SparseArray<ItemType> itemTypes = new SparseArray<>();
        reader.beginArray();

        while (reader.hasNext()) {
            int key = 0;
            ItemType value = null;
            reader.beginObject();

            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "key":
                        key = reader.nextInt();
                        break;
                    case "value":
                        value = readItemType(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            itemTypes.put(key, value);
            reader.endObject();
        }

        reader.endArray();
        return itemTypes;
    }

    @NonNull
    private static ItemType readItemType(@NonNull JsonReader reader) throws IOException {
        ItemType itemType = new ItemType();
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "id":
                    itemType.setId(reader.nextInt());
                    break;
                case "name":
                    itemType.setName(reader.nextString());
                    break;
                case "shorthand":
                    itemType.setShorthand(reader.nextString());
                    break;
                case "titleSingular":
                    itemType.setTitleSingular(reader.nextString());
                    break;
                case "titlePlural":
                    itemType.setTitlePlural(reader.nextString());
                    break;
                case "color":
                    itemType.setColor(reader.nextInt());
                    break;
                case "icon":
                    itemType.setIcon(reader.nextInt());
                    break;
                case "sortOrder":
                    itemType.setSortOrder(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return itemType;
    }
}
