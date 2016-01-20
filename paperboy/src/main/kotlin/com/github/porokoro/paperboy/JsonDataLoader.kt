/*
 * Copyright (C) 2015-2016 porokoro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.porokoro.paperboy

import android.content.Context
import android.content.res.Resources
import android.os.AsyncTask
import android.util.SparseArray
import java.io.IOException
import java.util.*

internal class JsonDataLoader(context: Context, private val file: String?, private val fileRes: Int,
                              definitions: SparseArray<ItemType>, private val callback: JsonDataLoader.Callback)
: AsyncTask<Unit, Int, List<PaperboySection>>() {
    private val context = context.applicationContext
    private val reader = JsonDataReader(definitions)

    override fun doInBackground(vararg params: Unit?): List<PaperboySection>? =
            if (fileRes != 0) {
                openFileRes(fileRes)?.let { reader.read(it) } ?: listOf()
            } else {
                (openFile(file ?: "paperboy/changelog-%s.json") ?: openFile("paperboy/changelog.json"))
                        ?.let { reader.read(it) } ?: listOf()
            }

    override fun onPostExecute(result: List<PaperboySection>?) {
        callback.finishedLoading(result!!)
    }

    private fun openFile(fileName: String) =
            try {
                val name = fileName.format(Locale.getDefault().language)
                context.assets.open(name)
            } catch(e: IOException) {
                null
            }

    private fun openFileRes(fileRes: Int) =
            try {
                context.resources.openRawResource(fileRes)
            } catch(e: Resources.NotFoundException) {
                null
            }

    internal interface Callback {
        fun finishedLoading(data: List<PaperboySection>)
    }
}
