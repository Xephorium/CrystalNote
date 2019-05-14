package com.xephorium.crystalnote.ui.base.drawer

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.base.drawer.DrawerItem.Companion.DrawerItemType.*
import kotlinx.android.synthetic.main.drawer_item.view.*

/*
  DrawerAdapter                                                          05.13.2019
  Christopher Cruzen

    This class provides display logic and handling behavior for each element of the
  Navigation Drawer.

*/

open class DrawerAdapter(
        private val context: Context,
        private val items: List<DrawerItem>
) : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {


    /*--- Lifecycle Methods ---*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = when (viewType) {
            ITEM.ordinal -> inflater.inflate(R.layout.drawer_item, parent, false)
            else -> inflater.inflate(R.layout.drawer_divider, parent, false)
        }

        return ViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.type == ITEM.ordinal) {
            items[position].let { item ->
                holder.icon.setImageResource(item.iconResource)
                holder.text.text = item.text
                holder.setNoteClickListeners(item)
            }
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount() = items.size

    inner class ViewHolder internal constructor(val view: View, val type: Int) :
            RecyclerView.ViewHolder(view) {

        internal lateinit var icon: ImageView
        internal lateinit var text: TextView

        init {
            if (type == ITEM.ordinal) {
                icon = view.icon_drawer_item
                text = view.text_drawer_item
            }
        }

        fun setNoteClickListeners(item: DrawerItem) {
            view.setOnClickListener { item.listener.onClick() }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.ordinal
    }
}