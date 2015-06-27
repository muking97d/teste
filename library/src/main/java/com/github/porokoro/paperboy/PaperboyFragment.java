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

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class PaperboyFragment extends Fragment implements JsonDataLoader.Callback {
    public static final String ARG_FILE        = "file";
    public static final String ARG_VIEW_TYPE   = "viewType";
    public static final String ARG_SORT_ITEMS  = "sortItems";
    public static final String ARG_DEFINITIONS = "definitions";

    public static final int     DEFAULT_VIEW_TYPE  = ViewTypes.NONE;
    public static final boolean DEFAULT_SORT_ITEMS = false;

    @ViewType
    private int     m_viewType;
    private boolean m_sortItems;

    private PaperboyAdapter m_adapter;

    public PaperboyFragment() {
        m_viewType = DEFAULT_VIEW_TYPE;
        m_sortItems = DEFAULT_SORT_ITEMS;
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = activity.obtainStyledAttributes(attrs,
                                                       R.styleable.PaperboyFragment,
                                                       R.attr.paperboyFragmentStyle,
                                                       R.style.Paperboy);

        a.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paperboy, container, false);
        Bundle arguments = getArguments();
        String file = null;
        SparseArray<ItemTypeDefinition> definitions = new SparseArray<>();

        if (arguments != null) {
            file = arguments.getString(ARG_FILE);
            definitions = arguments.getSparseParcelableArray(ARG_DEFINITIONS);
            m_viewType = ViewTypes.fromValue(arguments.getInt(ARG_VIEW_TYPE, DEFAULT_VIEW_TYPE));
            m_sortItems = arguments.getBoolean(ARG_SORT_ITEMS, DEFAULT_SORT_ITEMS);
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        m_adapter = new PaperboyAdapter(getActivity(), m_viewType, m_sortItems, definitions);
        recyclerView.setAdapter(m_adapter);

        loadData(file, definitions);

        return rootView;
    }

    private void loadData(String file, SparseArray<ItemTypeDefinition> definitions) {
        new JsonDataLoader(getActivity(), definitions, this).execute(file);
    }

    @Override
    public void finishedLoading(@NonNull List<PaperboySection> data) {
        if (m_adapter != null)
            m_adapter.setData(data);
    }
}
