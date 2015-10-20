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
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;


class PaperboyAdapter extends Adapter<ViewHolder> {
    private static final int    DEFAULT_COLOR = Color.BLACK;
    private static final String TAG           = PaperboyAdapter.class.getSimpleName();

    private final LayoutInflater        m_inflater;
    private final List<Element>         m_dataset;
    @ElementType
    private final int                   m_elementType;
    private final PaperboyConfiguration m_config;

    public PaperboyAdapter(@NonNull Context context, @NonNull PaperboyConfiguration config) {
        m_inflater = LayoutInflater.from(context);
        m_dataset = new ArrayList<>();
        m_config = config;
        m_elementType = getElementType(config.getViewType());
    }

    public void setData(@NonNull List<PaperboySection> dataset) {
        for (PaperboySection section : dataset) {
            if (m_config.isSortItems() || m_config.getViewType() == ViewTypes.HEADER) {
                Collections.sort(section.getItems(), new Comparator<PaperboyItem>() {
                    @Override
                    public int compare(@NonNull PaperboyItem lhs, @NonNull PaperboyItem rhs) {
                        ItemType lhsDef = m_config.getItemTypes().get(lhs.getType());
                        ItemType rhsDef = m_config.getItemTypes().get(rhs.getType());
                        return (lhsDef != null ? lhsDef.getSortOrder() : Integer.MAX_VALUE) -
                                (rhsDef != null ? rhsDef.getSortOrder() : Integer.MAX_VALUE);
                    }
                });
            }

            Element sectionElement = new Element();
            sectionElement.type = ElementTypes.SECTION_HEADER;
            sectionElement.data = section;
            m_dataset.add(sectionElement);

            int prevItemType = DefaultItemTypes.NONE;

            for (PaperboyItem item : section.getItems()) {
                if (item.getType() != prevItemType && m_config.getViewType() == ViewTypes.HEADER) {
                    Element typeElement = new Element();
                    typeElement.type = ElementTypes.TYPE_HEADER;
                    typeElement.data = m_config.getItemTypes().get(item.getType());
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
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    private static class ViewHolderType extends ViewHolder {
        TextView name;

        public ViewHolderType(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    private static class ViewHolderItemNone extends ViewHolder {
        TextView title;

        public ViewHolderItemNone(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private static class ViewHolderItemLabel extends ViewHolder {
        TextView type;
        TextView title;

        public ViewHolderItemLabel(@NonNull View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private static class ViewHolderItemIcon extends ViewHolder {
        ImageView type;
        TextView  title;

        public ViewHolderItemIcon(@NonNull View itemView) {
            super(itemView);
            type = (ImageView) itemView.findViewById(R.id.type);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private static class Element {
        @ElementType
        int type;
        Object data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, @ElementType int viewType) {
        switch (viewType) {
            case ElementTypes.SECTION_HEADER: {
                int sectionLayout = R.layout.list_item_paperboy_section;
                if (m_config.getSectionLayout() != 0)
                    sectionLayout = m_config.getSectionLayout();

                View rootView = m_inflater.inflate(sectionLayout, parent, false);
                return new ViewHolderSection(rootView);
            }
            case ElementTypes.TYPE_HEADER: {
                int typeLayout = R.layout.list_item_paperboy_type;
                if (m_config.getTypeLayout() != 0)
                    typeLayout = m_config.getTypeLayout();

                View rootView = m_inflater.inflate(typeLayout, parent, false);
                return new ViewHolderType(rootView);
            }
            case ElementTypes.ITEM_NONE: {
                View rootView = inflateItemView(R.layout.list_item_paperboy_item_none, parent);
                return new ViewHolderItemNone(rootView);
            }
            case ElementTypes.ITEM_LABEL: {
                View rootView = inflateItemView(R.layout.list_item_paperboy_item_label, parent);
                return new ViewHolderItemLabel(rootView);
            }
            case ElementTypes.ITEM_ICON: {
                View rootView = inflateItemView(R.layout.list_item_paperboy_item_icon, parent);
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

            if (viewHolder.name == null)
                Log.w(TAG, "View id 'R.id.name' missing in custom layout");
            else
                viewHolder.name.setText(data.getName());
        }
        else if (holder instanceof ViewHolderType) {
            ViewHolderType viewHolder = (ViewHolderType) holder;
            ItemType data = (ItemType) m_dataset.get(position).data;

            if (viewHolder.name == null)
                Log.w(TAG, "View id 'R.id.name' missing in custom layout");
            else {
                if (data == null)
                    viewHolder.name.setVisibility(View.GONE);
                else {
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(data.getTitlePlural());
                    viewHolder.name.setTextColor(data.getColor());
                }
            }
        }
        else if (holder instanceof ViewHolderItemNone) {
            ViewHolderItemNone viewHolder = (ViewHolderItemNone) holder;
            PaperboyItem data = (PaperboyItem) m_dataset.get(position).data;

            if (viewHolder.title == null)
                Log.w(TAG, "View id 'R.id.title' missing in custom layout");
            else
                setHtmlText(viewHolder.title, data.getTitle());
        }
        else if (holder instanceof ViewHolderItemLabel) {
            ViewHolderItemLabel viewHolder = (ViewHolderItemLabel) holder;
            PaperboyItem data = (PaperboyItem) m_dataset.get(position).data;
            ItemType definition = m_config.getItemTypes().get(data.getType());

            if (viewHolder.type == null)
                Log.w(TAG, "View id 'R.id.type' missing in custom layout");
            else {
                if (definition == null)
                    viewHolder.type.setVisibility(View.GONE);
                else {
                    viewHolder.type.setVisibility(View.VISIBLE);
                    viewHolder.type.setText(definition.getTitleSingular());
                    viewHolder.type.setBackgroundResource(R.drawable.paperboy_label_background);
                    viewHolder.type.getBackground().setColorFilter(getColor(definition), PorterDuff.Mode.SRC_ATOP);
                }
            }

            if (viewHolder.title == null)
                Log.w(TAG, "View id 'R.id.title' missing in custom layout");
            else
                setHtmlText(viewHolder.title, data.getTitle());
        }
        else if (holder instanceof ViewHolderItemIcon) {
            ViewHolderItemIcon viewHolder = (ViewHolderItemIcon) holder;
            PaperboyItem data = (PaperboyItem) m_dataset.get(position).data;
            ItemType definition = m_config.getItemTypes().get(data.getType());

            if (viewHolder.type == null)
                Log.w(TAG, "View id 'R.id.type' missing in custom layout");
            else {
                if (definition == null)
                    viewHolder.type.setVisibility(View.GONE);
                else {
                    viewHolder.type.setVisibility(View.VISIBLE);
                    viewHolder.type.setImageResource(definition.getIcon());
                    viewHolder.type.setColorFilter(getColor(definition));
                }
            }

            if (viewHolder.title == null)
                Log.w(TAG, "View id 'R.id.title' missing in custom layout");
            else
                setHtmlText(viewHolder.title, data.getTitle());
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

    @ColorInt
    private int getColor(@Nullable ItemType definition) {
        int color = definition == null ? 0 : definition.getColor();

        return color == 0 ? DEFAULT_COLOR : color;
    }

    @ElementType
    private int getElementType(@ViewType int type) {
        switch (type) {
            case ViewTypes.ICON:
                return ElementTypes.ITEM_ICON;
            case ViewTypes.LABEL:
                return ElementTypes.ITEM_LABEL;
            case ViewTypes.NONE:
            default:
                return ElementTypes.ITEM_NONE;
        }
    }

    private View inflateItemView(@LayoutRes int layoutRes, @NonNull ViewGroup parent) {
        if (m_config.getItemLayout() != 0)
            layoutRes = m_config.getItemLayout();

        return m_inflater.inflate(layoutRes, parent, false);
    }

    private void setHtmlText(@NonNull TextView textView, @NonNull String html) {
        Spanned spanned = Html.fromHtml(html);
        ClickableSpan[] spans = null;

        textView.setText(spanned);

        if (spanned != null)
            spans = spanned.getSpans(0, spanned.length(), ClickableSpan.class);
        if (spans != null && spans.length > 0) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());

            if (!(textView instanceof TouchDispatchingTextView)) {
                Log.w(TAG, "Use " + TouchDispatchingTextView.class.getName() +
                        " for View id 'R.id.title' in your custom layout when working with HTML links");
            }
        }
        else {
            textView.setMovementMethod(null);
        }
    }
}
