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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;


public class PaperboyAdapter extends Adapter<ViewHolder> {
    private static final int ELEMENT_TYPE_SECTION_HEADER = 1;
    private static final int ELEMENT_TYPE_TYPE_HEADER    = 2;
    private static final int ELEMENT_TYPE_ITEM_NONE      = 3;
    private static final int ELEMENT_TYPE_ITEM_LABEL     = 4;
    private static final int ELEMENT_TYPE_ITEM_ICON      = 5;

    private static final int DEFAULT_COLOR = Color.BLACK;

    private final LayoutInflater m_inflater;
    private final List<Element>  m_dataset;
    private final SparseIntArray m_colors;
    @ViewType
    private final int            m_viewType;
    @ElementType
    private final int            m_elementType;
    private final boolean        m_sortItems;

    public PaperboyAdapter(@NonNull Context context, @NonNull SparseIntArray colors,
                           @ViewType int viewType, boolean sortItems) {
        m_inflater = LayoutInflater.from(context);
        m_dataset = new ArrayList<>();
        m_colors = colors;
        m_viewType = viewType;
        m_elementType = getElementType(viewType);
        m_sortItems = sortItems;
    }

    public void setData(@NonNull List<PaperboySection> dataset) {
        for (PaperboySection section : dataset) {
            if (m_sortItems || m_viewType == ViewTypes.HEADER) {
                Collections.sort(section.getItems(), new Comparator<PaperboyItem>() {
                    @Override
                    public int compare(@NonNull PaperboyItem lhs, @NonNull PaperboyItem rhs) {
                        return lhs.getType() - rhs.getType();
                    }
                });
            }

            Element sectionElement = new Element();
            sectionElement.type = ELEMENT_TYPE_SECTION_HEADER;
            sectionElement.data = section;
            m_dataset.add(sectionElement);

            int prevItemType = ItemTypes.NONE;

            for (PaperboyItem item : section.getItems()) {
                if (item.getType() != prevItemType && m_viewType == ViewTypes.HEADER) {
                    Element typeElement = new Element();
                    typeElement.type = ELEMENT_TYPE_TYPE_HEADER;
                    typeElement.data = item.getType();
                    m_dataset.add(typeElement);

                    prevItemType = item.getType();
                }

                Element itemElement = new Element();
                itemElement.type = m_elementType;
                itemElement.data = item;
                m_dataset.add(itemElement);
            }
        }

        notifyDataSetChanged();
    }

    private static class ViewHolderSection extends ViewHolder {
        TextView name;

        public ViewHolderSection(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }

    private static class ViewHolderType extends ViewHolder {
        TextView name;

        public ViewHolderType(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }

    private static class ViewHolderItemNone extends ViewHolder {
        TextView title;

        public ViewHolderItemNone(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

    private static class ViewHolderItemLabel extends ViewHolder {
        TextView type;
        TextView title;

        public ViewHolderItemLabel(@NonNull View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.txt_type);
            title = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

    private static class ViewHolderItemIcon extends ViewHolder {
        ImageView type;
        TextView  title;

        public ViewHolderItemIcon(@NonNull View itemView) {
            super(itemView);
            type = (ImageView) itemView.findViewById(R.id.img_type);
            title = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

    @IntDef({ ELEMENT_TYPE_SECTION_HEADER,
              ELEMENT_TYPE_TYPE_HEADER,
              ELEMENT_TYPE_ITEM_NONE,
              ELEMENT_TYPE_ITEM_LABEL,
              ELEMENT_TYPE_ITEM_ICON })
    @Retention(RetentionPolicy.SOURCE)
    private @interface ElementType {
    }

    private static class Element {
        @ElementType
        int type;
        Object data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, @ElementType int viewType) {
        switch (viewType) {
            case ELEMENT_TYPE_SECTION_HEADER: {
                View rootView = m_inflater.inflate(R.layout.list_item_paperboy_section, parent, false);
                return new ViewHolderSection(rootView);
            }
            case ELEMENT_TYPE_TYPE_HEADER: {
                View rootView = m_inflater.inflate(R.layout.list_item_paperboy_type, parent, false);
                return new ViewHolderType(rootView);
            }
            case ELEMENT_TYPE_ITEM_NONE: {
                View rootView = m_inflater.inflate(R.layout.list_item_paperboy_item_none, parent, false);
                return new ViewHolderItemNone(rootView);
            }
            case ELEMENT_TYPE_ITEM_LABEL: {
                View rootView = m_inflater.inflate(R.layout.list_item_paperboy_item_label, parent, false);
                return new ViewHolderItemLabel(rootView);
            }
            case ELEMENT_TYPE_ITEM_ICON: {
                View rootView = m_inflater.inflate(R.layout.list_item_paperboy_item_icon, parent, false);
                return new ViewHolderItemIcon(rootView);
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolderSection) {
            ViewHolderSection viewHolder = (ViewHolderSection) holder;
            PaperboySection data = (PaperboySection) m_dataset.get(position).data;

            viewHolder.name.setText(data.getName());
        }
        else if (holder instanceof ViewHolderType) {
            ViewHolderType viewHolder = (ViewHolderType) holder;
            @ItemType int data = (int) m_dataset.get(position).data;

            switch (data) {
                case ItemTypes.FEATURE:
                    viewHolder.name.setText(R.string.paperboy_item_type_features);
                    viewHolder.name.setTextColor(getColor(data));
                    break;
                case ItemTypes.BUG:
                    viewHolder.name.setText(R.string.paperboy_item_type_bugs);
                    viewHolder.name.setTextColor(getColor(data));
                    break;
                case ItemTypes.IMPROVEMENT:
                    viewHolder.name.setText(R.string.paperboy_item_type_improvements);
                    viewHolder.name.setTextColor(getColor(data));
                    break;
            }
        }
        else if (holder instanceof ViewHolderItemNone) {
            ViewHolderItemNone viewHolder = (ViewHolderItemNone) holder;
            PaperboyItem data = (PaperboyItem) m_dataset.get(position).data;

            viewHolder.title.setText(data.getTitle());
        }
        else if (holder instanceof ViewHolderItemLabel) {
            ViewHolderItemLabel viewHolder = (ViewHolderItemLabel) holder;
            PaperboyItem data = (PaperboyItem) m_dataset.get(position).data;

            switch (data.getType()) {
                case ItemTypes.FEATURE:
                    viewHolder.type.setText(R.string.paperboy_item_type_feature);
                    viewHolder.type.setBackgroundResource(R.drawable.paperboy_label_background);
                    viewHolder.type.getBackground().setColorFilter(getColor(data.getType()),
                                                                   PorterDuff.Mode.SRC_ATOP);
                    break;
                case ItemTypes.BUG:
                    viewHolder.type.setText(R.string.paperboy_item_type_bug);
                    viewHolder.type.setBackgroundResource(R.drawable.paperboy_label_background);
                    viewHolder.type.getBackground().setColorFilter(getColor(data.getType()),
                                                                   PorterDuff.Mode.SRC_ATOP);
                    break;
                case ItemTypes.IMPROVEMENT:
                    viewHolder.type.setText(R.string.paperboy_item_type_improvement);
                    viewHolder.type.setBackgroundResource(R.drawable.paperboy_label_background);
                    viewHolder.type.getBackground().setColorFilter(getColor(data.getType()),
                                                                   PorterDuff.Mode.SRC_ATOP);
                    break;
            }

            viewHolder.title.setText(data.getTitle());
        }
        else if (holder instanceof ViewHolderItemIcon) {
            ViewHolderItemIcon viewHolder = (ViewHolderItemIcon) holder;
            PaperboyItem data = (PaperboyItem) m_dataset.get(position).data;

            switch (data.getType()) {
                case ItemTypes.FEATURE:
                    viewHolder.type.setImageResource(R.drawable.paperboy_ic_done);
                    viewHolder.type.setColorFilter(getColor(data.getType()));
                    break;
                case ItemTypes.BUG:
                    viewHolder.type.setImageResource(R.drawable.paperboy_ic_bug_report);
                    viewHolder.type.setColorFilter(getColor(data.getType()));
                    break;
                case ItemTypes.IMPROVEMENT:
                    viewHolder.type.setImageResource(R.drawable.paperboy_ic_trending_up);
                    viewHolder.type.setColorFilter(getColor(data.getType()));
                    break;
            }

            viewHolder.title.setText(data.getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return m_dataset.get(position).type;
    }

    @Override
    public int getItemCount() {
        return m_dataset.size();
    }

    private int getColor(@ItemType int type) {
        int color = m_colors.get(type);

        return color == 0 ? DEFAULT_COLOR : color;
    }

    @ElementType
    private int getElementType(@ViewType int type) {
        switch (type) {
            case ViewTypes.ICON:
                return ELEMENT_TYPE_ITEM_ICON;
            case ViewTypes.LABEL:
                return ELEMENT_TYPE_ITEM_LABEL;
            case ViewTypes.NONE:
            default:
                return ELEMENT_TYPE_ITEM_NONE;
        }
    }
}
