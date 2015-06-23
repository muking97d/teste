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
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class PaperboyFragment extends Fragment implements JsonDataLoader.Callback {
    public static final String ARG_FILE              = "file";
    public static final String ARG_FEATURE_COLOR     = "featureColor";
    public static final String ARG_BUG_COLOR         = "bugColor";
    public static final String ARG_IMPROVEMENT_COLOR = "improvementColor";
    public static final String ARG_VIEW_TYPE         = "viewType";
    public static final String ARG_SORT_ITEMS        = "sortItems";

    public static final int     DEFAULT_FEATURE_COLOR_RES     = R.color.paperboy_light_item_type_feature;
    public static final int     DEFAULT_BUG_COLOR_RES         = R.color.paperboy_light_item_type_bug;
    public static final int     DEFAULT_IMPROVEMENT_COLOR_RES = R.color.paperboy_light_item_type_improvement;
    public static final int     DEFAULT_VIEW_TYPE             = ViewTypes.NONE;
    public static final boolean DEFAULT_SORT_ITEMS            = false;

    private final SparseIntArray m_colors;

    @ViewType
    private int     m_viewType;
    private boolean m_sortItems;

    private PaperboyAdapter m_adapter;

    public PaperboyFragment() {
        m_colors = new SparseIntArray(3);
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

        m_colors.put(ItemTypes.FEATURE, getColor(a, R.attr.paperboyFeatureColor, DEFAULT_FEATURE_COLOR_RES));
        m_colors.put(ItemTypes.BUG, getColor(a, R.attr.paperboyBugColor, DEFAULT_BUG_COLOR_RES));
        m_colors.put(ItemTypes.IMPROVEMENT,
                     getColor(a, R.attr.paperboyImprovementColor, DEFAULT_IMPROVEMENT_COLOR_RES));

        a.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paperboy, container, false);
        Bundle arguments = getArguments();
        String file = null;

        if (arguments != null) {
            file = arguments.getString(ARG_FILE);
            m_viewType = ViewTypes.fromValue(arguments.getInt(ARG_VIEW_TYPE, DEFAULT_VIEW_TYPE));
            m_sortItems = arguments.getBoolean(ARG_SORT_ITEMS, DEFAULT_SORT_ITEMS);
        }

        m_colors.put(ItemTypes.FEATURE, getColor(arguments, ARG_FEATURE_COLOR, DEFAULT_FEATURE_COLOR_RES));
        m_colors.put(ItemTypes.BUG, getColor(arguments, ARG_BUG_COLOR, DEFAULT_BUG_COLOR_RES));
        m_colors.put(ItemTypes.IMPROVEMENT, getColor(arguments, ARG_IMPROVEMENT_COLOR, DEFAULT_IMPROVEMENT_COLOR_RES));

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        m_adapter = new PaperboyAdapter(getActivity(), m_colors, m_viewType, m_sortItems);
        recyclerView.setAdapter(m_adapter);

        loadData(file);

        return rootView;
    }

    private void loadData(String file) {
        new JsonDataLoader(getActivity(), this).execute(file);
    }

    private int getColor(@Nullable TypedArray a, int index, @ColorRes int defColorId) {
        int defColor = getResources().getColor(defColorId);

        if (a != null)
            return a.getColor(index, defColor);

        return defColor;
    }

    private int getColor(@Nullable Bundle arguments, @NonNull String key, @ColorRes int defColorId) {
        int defColor = getResources().getColor(defColorId);

        if (arguments != null)
            return arguments.getInt(key, defColor);

        return defColor;
    }

    @Override
    public void finishedLoading(@NonNull List<PaperboySection> data) {
        if (m_adapter != null)
            m_adapter.setData(data);
    }

    public static class Builder {
        private final Context m_context;
        private final Bundle  m_arguments;

        public Builder(@NonNull Context context) {
            m_context = context;
            m_arguments = new Bundle();
        }

        @NonNull
        public Builder setFile(@Nullable String file) {
            m_arguments.putString(ARG_FILE, file);
            return this;
        }

        @NonNull
        public Builder setFeatureColor(int color) {
            m_arguments.putInt(ARG_FEATURE_COLOR, color);
            return this;
        }

        @NonNull
        public Builder setFeatureColorRes(@ColorRes int color) {
            setFeatureColor(m_context.getResources().getColor(color));
            return this;
        }

        @NonNull
        public Builder setBugColor(int color) {
            m_arguments.putInt(ARG_BUG_COLOR, color);
            return this;
        }

        @NonNull
        public Builder setBugColorRes(@ColorRes int color) {
            setBugColor(m_context.getResources().getColor(color));
            return this;
        }

        @NonNull
        public Builder setImprovementColor(int color) {
            m_arguments.putInt(ARG_IMPROVEMENT_COLOR, color);
            return this;
        }

        @NonNull
        public Builder setImprovementColorRes(@ColorRes int color) {
            setImprovementColor(m_context.getResources().getColor(color));
            return this;
        }

        @NonNull
        public Builder setViewType(@ViewType int viewType) {
            m_arguments.putInt(ARG_VIEW_TYPE, viewType);
            return this;
        }

        @NonNull
        public Builder setSortItems(boolean sortItems) {
            m_arguments.putBoolean(ARG_SORT_ITEMS, sortItems);
            return this;
        }

        @NonNull
        public Builder setTheme(@Theme int theme) {
            switch (theme) {
                case Themes.DARK:
                    setFeatureColorRes(R.color.paperboy_dark_item_type_feature);
                    setBugColorRes(R.color.paperboy_dark_item_type_bug);
                    setImprovementColorRes(R.color.paperboy_dark_item_type_improvement);
                    break;
                case Themes.LIGHT:
                    setFeatureColorRes(R.color.paperboy_light_item_type_feature);
                    setBugColorRes(R.color.paperboy_light_item_type_bug);
                    setImprovementColorRes(R.color.paperboy_light_item_type_improvement);
                    break;
            }

            return this;
        }

        @NonNull
        public PaperboyFragment build() {
            PaperboyFragment fragment = new PaperboyFragment();
            fragment.setArguments(m_arguments);

            return fragment;
        }
    }
}
