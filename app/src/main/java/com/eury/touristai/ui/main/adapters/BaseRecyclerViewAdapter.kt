package com.eury.touristai.ui.main.adapters

import android.content.Context
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import com.eury.touristai.utils.Loggable
import java.util.*


/**
 * Created by euryperez on 3/2/18.
 */

abstract class BaseRecyclerViewAdapter<T, V : View> : RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolderWrapper<V>>, Loggable {

    //======================================================
    //                      FIELDS
    //======================================================

    protected abstract var context: Context

    var items: MutableList<T>? = null

    /**
     * Returns the items list
     */
    val itemList: List<T>?
        get() = this.items

    //======================================================
    //                      CONSTRUCTORS
    //======================================================
    /**
     * Public constructor, if used items will be filled with last
     * by default.
     */
    constructor() {
        items = ArrayList()
    }

    /**
     * Public constructor, if used items will be filled with last
     * by default.
     *
     * @param context Context
     */
    constructor(context: Context) : super() {
        items = ArrayList()
        this.context = context
    }

    /**
     * Public constructor when items list is provided
     *
     * @param items to initialize the list
     */
    constructor(items: MutableList<T>) {
        this.items = items
    }

    /**
     * Public constructor when items list is provided
     *
     * @param items to initialize the list
     */
    constructor(context: Context, items: MutableList<T>) {
        this.context = context
        this.items = items
    }

    //======================================================
    //                      OVERRIDEN
    //======================================================
    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWrapper<V> {
        return ViewHolderWrapper(onCreateItemView(parent, viewType))
    }

    //======================================================
    //                      METHODS
    //======================================================

    /**
     * Adds data to the actual Dataset
     *
     * @param items to be merged
     */
    fun addData(items: LinkedList<T>, comparator: Comparator<T>?) {
        for (entity in items) {
            val itemIndex = this.items!!.indexOf(entity)
            if (itemIndex > -1) {
                this.items!![itemIndex] = entity
                if (items.indexOf(entity) == items.count() - 1 && comparator != null) {
                    Collections.sort(this.items, comparator)
                }
                notifyItemChanged(itemIndex)
            } else {
                this.items!!.add(entity)
                if (items.indexOf(entity) == items.count() - 1 && comparator != null) {
                    Collections.sort(this.items, comparator)
                }
                notifyItemInserted(itemCount)
            }
        }
    }

    fun addData(items: List<T>, comparator: Comparator<T>?) {
        for (entity in items) {
            val itemIndex = this.items!!.indexOf(entity)
            if (itemIndex > -1) {
                this.items!![itemIndex] = entity
                if (items.indexOf(entity) == items.size - 1 && comparator != null) {
                    Collections.sort(this.items, comparator)
                }
                notifyItemChanged(itemIndex)
            } else {
                this.items!!.add(entity)
                if (items.indexOf(entity) == items.size - 1 && comparator != null) {
                    Collections.sort(this.items, comparator)
                }
                notifyItemInserted(itemCount)
            }
        }
    }

    /**
     * Removes a specific item in the list
     *
     * @param item to be removed
     */
    fun removeItem(item: T) {
        items!!.remove(item)
        notifyDataSetChanged()
    }

    /**
     * Removes a post from the list
     *
     * @param postIDs posts to remove
     */
    fun removeItem(postIDs: List<T>) {
        for (item in postIDs) {
            items!!.remove(getItem(item))
        }
        notifyDataSetChanged()
    }

    /**
     * Updates a specific item in the list
     *
     * @param item  to be updated
     * @param index of the item in the list
     */
    fun putItem(item: T, index: Int) {
        items!![index] = item
        notifyItemChanged(index)
    }

    /**
     * Returns the index of a specified item
     *
     * @param item of the specific item
     */
    fun getIndex(item: T): Int {
        if (this.items != null) {
            return this.items!!.indexOf(getItem(item))
        }
        throw IllegalStateException("Item List is null")
    }

    /**
     * Clears the items list
     */
    fun clear() {
        if (this.items != null) {
            this.items!!.clear()
            notifyDataSetChanged()
        }
    }

    //======================================================
    //                      ABSTRACT
    //======================================================

    protected abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): V

    /**
     * Returns a specific item
     *
     * @param itemId of the specific item
     */
    abstract fun getItem(item: T): T


    // Wrapper class to provide the view access to the viewholder
    class ViewHolderWrapper<out V : View>(val view: V) : RecyclerView.ViewHolder(view)
}

