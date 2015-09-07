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
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class JsonDataLoader extends AsyncTask<Void, Integer, List<PaperboySection>> {

    private final Context        m_context;
    private final String         m_file;
    private final int            m_fileRes;
    private final Callback       m_callback;
    private final JsonDataReader m_reader;

    public JsonDataLoader(@NonNull Context context, @Nullable String file, @RawRes int fileRes,
                          @NonNull SparseArray<ItemType> definitions, @NonNull Callback callback) {
        m_context = context.getApplicationContext();
        m_file = file;
        m_fileRes = fileRes;
        m_callback = callback;
        m_reader = new JsonDataReader(definitions);
    }

    @NonNull
    @Override
    protected List<PaperboySection> doInBackground(@NonNull Void... params) {
        if (m_fileRes != 0) {
            InputStream input = openFileRes(m_fileRes);

            if (input == null)
                return new ArrayList<>(0);

            return m_reader.read(input);
        }
        else {
            String file = m_file;

            if (file == null)
                file = "paperboy/changelog-%s.json";

            InputStream input = openFile(file);

            if (input == null)
                input = openFile("paperboy/changelog.json");
            if (input == null)
                return new ArrayList<>(0);

            return m_reader.read(input);
        }
    }

    @Override
    protected void onPostExecute(@NonNull List<PaperboySection> paperboySections) {
        m_callback.finishedLoading(paperboySections);
    }

    @Nullable
    private InputStream openFile(@NonNull String fileName) {
        fileName = String.format(fileName, Locale.getDefault().getLanguage());

        try {
            return m_context.getAssets().open(fileName);
        }
        catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private InputStream openFileRes(@RawRes int fileRes) {
        try {
            return m_context.getResources().openRawResource(fileRes);
        }
        catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public interface Callback {
        void finishedLoading(@NonNull List<PaperboySection> data);
    }
}
