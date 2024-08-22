package com.pools.store.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected abstract val differ: AsyncListDiffer<T>

    var list: List<T>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position in list.indices) {
            (holder as Binder<T>).bind(list[position])
        }
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun getItemCount(): Int {
        return list.size
    }

    protected var onItemClickListener: ((T) -> Unit)? = null
    protected var onItemSwitchClickListener: ((T , Int , Boolean) -> Unit)? = null

    protected var onClickFillTextListener: ((T,Int) -> Unit)? = null
    protected var onClickChooseTextListener: ((T,Int)-> Unit)? = null


    protected var onDetailClickListener: ((T) -> Unit)? = null


    protected var onItemPositionClickListener: ((T,Int) -> Unit)? = null



    fun setItemPositionClickListener(listener: (T,Int) -> Unit) {
        onItemPositionClickListener = listener
    }
    fun setDetailClickListener(listener: (T) -> Unit) {
        onDetailClickListener = listener
    }
    fun setItemClickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }
    fun setClickFillTextListener(listener: (T,Int) -> Unit) {
        onClickFillTextListener = listener
    }
    fun setClickChooseTextListener(listener: (T,Int) -> Unit) {
        onClickChooseTextListener = listener
    }

    fun setClickSwitchListener(listener: (T , Int , Boolean) -> Unit) {
        onItemSwitchClickListener = listener
    }


    fun removeItemAtPosition(index:Int){
        if (index in list.indices) {
            val newList = ArrayList(list)
            newList.removeAt(index)
            list = newList
            notifyItemRemoved(index)
            notifyItemRangeChanged(index,list.size-index)
        }
    }

    interface Binder<in T> {
        fun bind(item: T)
    }
}
