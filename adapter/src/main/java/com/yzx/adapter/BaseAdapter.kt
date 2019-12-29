package com.yzx.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author yzx
 * @date 2019/12/11
 * Description adapter基类
 */
abstract class BaseAdapter<VH : RecyclerView.ViewHolder, M : Any>(private var activity: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        const val VIEW_TYPE_HEAD = 10001
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_STATUS_EMPTY = 1
        const val VIEW_TYPE_STATUS_ERROR = 2
        const val VIEW_TYPE_STATUS_LOADING = 3
    }

    private var headViews: ArrayList<View> = ArrayList()
    private var data: ArrayList<M> = ArrayList()
    private var itemClickListener: ItemClickListener? = null
    private var viewType: Int = VIEW_TYPE_NORMAL

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = holder !is BaseAdapter<*, *>.NormalHolder
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //处理GridLayoutManager
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    return if (viewType == VIEW_TYPE_NORMAL) {
                        if (position < headViews.size) layoutManager.spanCount else 1
                    } else {
                        layoutManager.spanCount
                    }
                }
            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        //如果有Head
        if (isHead(position)) {
            return VIEW_TYPE_HEAD + position
        }
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = activity.layoutInflater

        return getHolderByType(layoutInflater, parent, viewType)
    }

    private fun getHolderByType(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {


        return when (viewType) {
            VIEW_TYPE_STATUS_EMPTY, VIEW_TYPE_STATUS_ERROR, VIEW_TYPE_STATUS_LOADING -> StatusHolder(
                layoutInflater.inflate(
                    getStatusViewLayoutResourceByStatus(),
                    parent,
                    false
                )
            )
            VIEW_TYPE_NORMAL -> NormalHolder(
                layoutInflater.inflate(
                    setItemLayout(),
                    parent,
                    false
                )
            )
            else -> HeadHolder(headViews[viewType - VIEW_TYPE_HEAD])
        }
    }

    /**
     * item布局资源
     */
    abstract fun setItemLayout(): Int

    @LayoutRes

    /**
     * 根据状态获取
     */
    private fun getStatusViewLayoutResourceByStatus(): Int {
        return when (viewType) {
            VIEW_TYPE_STATUS_ERROR -> R.layout.layout_status_error
            VIEW_TYPE_STATUS_EMPTY -> R.layout.layout_status_empty
            VIEW_TYPE_STATUS_LOADING -> R.layout.layout_status_loading
            else -> 0
        }
    }


    override fun getItemCount(): Int {
        return when (viewType) {
            VIEW_TYPE_STATUS_ERROR, VIEW_TYPE_STATUS_LOADING, VIEW_TYPE_STATUS_EMPTY -> headViews.size + 1
            else -> headViews.size + getCount()
        }

    }

    open fun getCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is BaseAdapter<*, *>.HeadHolder) {
            return
        }
        if (holder is BaseAdapter<*, *>.StatusHolder) {
            if (viewType != VIEW_TYPE_STATUS_LOADING) {
                holder.itemView.setOnClickListener {
                    updateStatus(VIEW_TYPE_STATUS_LOADING)
                }
            }
            return
        }
        val adjPosition = position - headViews.size
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(activity, adjPosition)
        }
        onBindHolder(holder, adjPosition, this.data[adjPosition])
    }

    private fun isHead(position: Int): Boolean {
        return position < headViews.size
    }

    abstract fun onBindHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: M
    )

    /**
     * 添加headView
     */
    fun addHead(headView: View) {
        headViews.add(headView)
        notifyItemInserted(headViews.size)
    }

    /**
     * 更新状态
     */
    fun updateStatus(viewType: Int) {
        if (this.viewType != viewType) {
            if (viewTypeIsStatus(viewType) && viewTypeIsStatus(this.viewType)) {
                this.viewType = viewType
                notifyItemChanged(headViews.size)
            } else {
                this.viewType = viewType
                notifyDataSetChanged()
            }
        }
    }

    private fun viewTypeIsStatus(viewType: Int): Boolean {
        return (viewType == VIEW_TYPE_STATUS_LOADING) or (viewType == VIEW_TYPE_STATUS_ERROR) or (viewType == VIEW_TYPE_STATUS_EMPTY)
    }

    /**
     * 添加数据
     */
    fun addData(newData: ArrayList<M>) {
        if (viewType != VIEW_TYPE_NORMAL) {
            updateStatus(VIEW_TYPE_NORMAL)
        }
        if (newData.isNullOrEmpty()) {
            return
        }
        data.addAll(newData)
        notifyItemRangeInserted(itemCount, newData.size)
    }

    fun getData(): ArrayList<M> {
        return data
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    /**
     * 状态Holder
     */
    inner class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 状态Holder
     */
    inner class StatusHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * HeadHolder
     */
    inner class HeadHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}