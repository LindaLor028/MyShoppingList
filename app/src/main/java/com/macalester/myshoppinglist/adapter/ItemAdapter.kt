package com.macalester.myshoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.macalester.myshoppinglist.R
import com.macalester.myshoppinglist.ScrollingActivity
import com.macalester.myshoppinglist.data.Item
import com.macalester.myshoppinglist.databinding.ItemRowBinding
import com.macalester.myshoppinglist.touch.ItemTouchHelperCallback
import com.macalester.myshoppinglist.viewmodel.ItemViewModel
import java.util.*

class ItemAdapter(
    private val context: Context,
    private val itemViewModel: ItemViewModel):
    ListAdapter<Item, ItemAdapter.ViewHolder>(ItemDiffCallback()),
    ItemTouchHelperCallback {

    companion object {
        val imageArray = arrayListOf(
            R.drawable.foodicon,
            R.drawable.clothesicon,
            R.drawable.houseicon,
            R.drawable.othericon
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRowBinding = ItemRowBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return ViewHolder(itemRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    override fun onDismissed(position: Int) {
        itemViewModel.deleteItem(getItem(position))
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val tempList = mutableListOf<Item>()
        tempList.addAll(currentList)
        Collections.swap(tempList, fromPosition, toPosition)
        submitList(tempList)
    }

    inner class ViewHolder(private val itemRowBinding: ItemRowBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {
        fun bind(item: Item) {

            setItemDisplay(item)

            val demoAnim = AnimationUtils.loadAnimation(context,
                R.anim.rowbutton_anim)

            itemRowBinding.ibDelete.setOnClickListener { view ->
                itemRowBinding.ibDelete.startAnimation(demoAnim)
                (context as ScrollingActivity).showDeleteDialog(item)
            }

            itemRowBinding.ibEdit.setOnClickListener {
                itemRowBinding.ibEdit.startAnimation(demoAnim)
                (context as ScrollingActivity).showEditDialog(item)
            }

            itemRowBinding.cbBought.setOnClickListener {
                item.isBought = itemRowBinding.cbBought.isChecked
                itemViewModel.updateItem(item)
            }

            itemRowBinding.cvItem.setOnClickListener {
                (context as ScrollingActivity).showDetailsDialog(item)
            }

        }

        private fun setItemDisplay(item: Item) {
            itemRowBinding.tvItem.text = item.itemTitle
            itemRowBinding.tvPrice.text = "$ ${item.price.toString()}"
            itemRowBinding.imgCategory.setImageResource(imageArray[item.category.toInt()])
            itemRowBinding.cbBought.isChecked = item.isBought
        }
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}
