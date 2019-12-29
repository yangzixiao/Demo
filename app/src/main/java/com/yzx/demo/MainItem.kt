package com.yzx.demo

import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder
import kotlinx.android.synthetic.main.item_demo_main.view.*

/**
 * @author yzx
 * @date 2019/12/15
 * Description
 */
class MainItem : DslAdapterItem() {
    init {
        itemLayoutId = R.layout.item_demo_main
    }

    var title: String? = null

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem)

        itemHolder.itemView.tvTitle.text = title
    }
}