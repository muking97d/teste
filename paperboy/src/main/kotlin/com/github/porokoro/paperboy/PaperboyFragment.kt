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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PaperboyFragment : Fragment(), JsonDataLoader.Callback {
    companion object {
        val ARG_CONFIG = "configuration"
    }

    private var adapter: PaperboyAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_paperboy, container, false) ?: return null
        val config = if (arguments != null)
            ConfigurationSerializer.read(arguments.getString(ARG_CONFIG))
        else
            PaperboyConfiguration()

        val recyclerView = rootView.findViewById(R.id.recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = PaperboyAdapter(activity, config)
        recyclerView.adapter = adapter

        loadData(config)

        return rootView;
    }

    private fun loadData(config: PaperboyConfiguration) =
            JsonDataLoader(activity, config.file, config.fileRes, config.itemTypes, this).execute()

    override fun finishedLoading(data: List<PaperboySection>) {
        adapter?.setData(data)
    }
}
