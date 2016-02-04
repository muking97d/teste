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
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.*

internal class PaperboyAdapter(context: Context, private val config: PaperboyConfiguration)
: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val DEFAULT_COLOR = Color.BLACK
        val TAG = PaperboyAdapter::class.java.simpleName
    }

    private val inflater = LayoutInflater.from(context)
    private val dataset = arrayListOf<Element>()
    private val elementType = getElementType(config.viewType)

    fun setData(sections: List<PaperboySection>) {
        for (section in sections) {
            if (config.sortItems || config.viewType == ViewTypes.HEADER) {
                Collections.sort(section.items) { lhs, rhs ->
                    val lhsDef = config.itemTypes.get(lhs.type)
                    val rhsDef = config.itemTypes.get(rhs.type)
                    (lhsDef?.sortOrder ?: Int.MAX_VALUE) - (rhsDef?.sortOrder ?: Int.MAX_VALUE)
                }
            }

            dataset.add(Element(ElementTypes.SECTION_HEADER, section))

            var prevItemType = DefaultItemTypes.NONE

            for (item in section.items) {
                if (item.type != prevItemType && config.viewType == ViewTypes.HEADER) {
                    dataset.add(Element(
                            ElementTypes.TYPE_HEADER,
                            config.itemTypes.get(item.type)
                    ))
                    prevItemType = item.type
                }

                dataset.add(Element(elementType, item))
            }
        }

        notifyDataSetChanged()
    }

    private class ViewHolderSection(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById(R.id.name) as TextView?
    }

    private class ViewHolderType(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById(R.id.name) as TextView?
    }

    private class ViewHolderItemNone(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById(R.id.title) as TextView?
    }

    private class ViewHolderItemLabel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type = itemView.findViewById(R.id.type) as TextView?
        val title = itemView.findViewById(R.id.title) as TextView?
    }

    private class ViewHolderItemIcon(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type = itemView.findViewById(R.id.type) as ImageView?
        val title = itemView.findViewById(R.id.title) as TextView?
    }

    private data class Element(
            val type: Int,
            val data: Any?)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? = when (viewType) {
        ElementTypes.SECTION_HEADER -> {
            val layout = if (config.sectionLayout != 0) config.sectionLayout else R.layout.list_item_paperboy_section
            ViewHolderSection(inflater.inflate(layout, parent, false))
        }
        ElementTypes.TYPE_HEADER -> {
            val layout = if (config.typeLayout != 0) config.typeLayout else R.layout.list_item_paperboy_type
            ViewHolderType(inflater.inflate(layout, parent, false))
        }
        ElementTypes.ITEM_NONE -> {
            val layout = if (config.itemLayout != 0) config.itemLayout else R.layout.list_item_paperboy_item_none
            ViewHolderItemNone(inflater.inflate(layout, parent, false))
        }
        ElementTypes.ITEM_LABEL -> {
            val layout = if (config.itemLayout != 0) config.itemLayout else R.layout.list_item_paperboy_item_label
            ViewHolderItemLabel(inflater.inflate(layout, parent, false))
        }
        ElementTypes.ITEM_ICON -> {
            val layout = if (config.itemLayout != 0) config.itemLayout else R.layout.list_item_paperboy_item_icon
            ViewHolderItemIcon(inflater.inflate(layout, parent, false))
        }
        else -> null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int): Unit {
        when (holder) {
            is ViewHolderSection -> {
                val data = dataset[position].data as PaperboySection

                if (holder.name == null)
                    Log.w(TAG, "View id 'R.id.name' missing in custom layout")
                else
                    holder.name.text = data.name
            }
            is ViewHolderType -> {
                val data = dataset[position].data as ItemType?

                if (holder.name == null)
                    Log.w(TAG, "View id 'R.id.name' missing in custom layout")
                else {
                    if (data == null)
                        holder.name.visibility = View.GONE
                    else {
                        holder.name.visibility = View.VISIBLE
                        holder.name.text = data.titlePlural
                        holder.name.setTextColor(data.color)
                    }
                }
            }
            is ViewHolderItemNone -> {
                val data = dataset[position].data as PaperboyItem

                if (holder.title == null)
                    Log.w(TAG, "View id 'R.id.title' missing in custom layout")
                else
                    holder.title.htmlText = data.title
            }
            is ViewHolderItemLabel -> {
                val data = dataset[position].data as PaperboyItem
                val definition = config.itemTypes.get(data.type)

                if (holder.type == null)
                    Log.w(TAG, "View id 'R.id.type' missing in custom layout")
                else {
                    if (definition == null)
                        holder.type.visibility = View.GONE
                    else {
                        holder.type.visibility = View.VISIBLE
                        holder.type.text = definition.titleSingular
                        holder.type.setBackgroundResource(R.drawable.paperboy_label_background)
                        holder.type.background.setColorFilter(getColor(definition), PorterDuff.Mode.SRC_ATOP)
                    }
                }

                if (holder.title == null)
                    Log.w(TAG, "View id 'R.id.title' missing in custom layout")
                else
                    holder.title.htmlText = data.title
            }
            is ViewHolderItemIcon -> {
                val data = dataset[position].data as PaperboyItem
                val definition = config.itemTypes.get(data.type)

                if (holder.type == null)
                    Log.w(TAG, "View id 'R.id.type' missing in custom layout")
                else {
                    if (definition == null)
                        holder.type.visibility = View.GONE
                    else {
                        holder.type.visibility = View.VISIBLE
                        holder.type.setImageResource(definition.icon)
                        holder.type.setColorFilter(getColor(definition))
                    }
                }

                if (holder.title == null)
                    Log.w(TAG, "View id 'R.id.title' missing in custom layout")
                else
                    holder.title.htmlText = data.title
            }
            else -> {
                Log.e(TAG, "Unknown view holder ${holder?.javaClass?.name}")
            }
        }
    }

    override fun getItemViewType(position: Int) = dataset[position].type

    override fun getItemCount() = dataset.size

    private fun getColor(definition: ItemType?): Int {
        val color = definition?.color ?: 0
        return if (color == 0) DEFAULT_COLOR else color
    }

    private fun getElementType(type: Int) = when (type) {
        ViewTypes.ICON -> ElementTypes.ITEM_ICON
        ViewTypes.LABEL -> ElementTypes.ITEM_LABEL
        ViewTypes.NONE -> ElementTypes.ITEM_NONE
        else -> ElementTypes.ITEM_NONE
    }

    private var TextView.htmlText: CharSequence
        get() = text
        set(value) {
            val spanned = Html.fromHtml(value.toString())
            val spans = spanned?.getSpans(0, spanned.length, ClickableSpan::class.java)

            text = spanned

            if (spans != null && spans.size > 0) {
                movementMethod = LinkMovementMethod.getInstance()

                if (this !is TouchDispatchingTextView)
                    Log.w(TAG, "Use ${TouchDispatchingTextView::class.java.name} for View id 'R.id.title' " +
                            "in your custom layout when working with HTML links")
            } else
                movementMethod = null
        }
}
