package com.macalester.myshoppinglist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.macalester.myshoppinglist.R
import com.macalester.myshoppinglist.ScrollingActivity
import com.macalester.myshoppinglist.adapter.ItemAdapter.Companion.imageArray
import com.macalester.myshoppinglist.data.Item
import com.macalester.myshoppinglist.databinding.DetailsDialogBinding
import com.macalester.myshoppinglist.databinding.ItemDialogBinding

class DetailsDialog : DialogFragment() {

    lateinit var itemDialogHandler: ItemDialog.ItemDialogHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemDialog.ItemDialogHandler) {
            itemDialogHandler = context
        } else {
            throw java.lang.RuntimeException(
                getString(R.string.item_dialog_handler_attach_error))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        val dialogViewBinding = DetailsDialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        dialogBuilder.setTitle(getString(R.string.about_item))
        val item = requireArguments().get(ScrollingActivity.KEY_ITEM_DETAILS) as Item

        setTextViews(dialogViewBinding, item)

        if (item.isBought) {
            dialogViewBinding.ctview.setCheckMarkDrawable(R.drawable.check)
            dialogViewBinding.ctview.setChecked(true)
        } else {
            dialogViewBinding.ctview.setCheckMarkDrawable(R.drawable.not_check)
            dialogViewBinding.ctview.setChecked(false)
        }

        dialogBuilder.setNegativeButton(getString(R.string.ok)) {
                dialog, which ->
        }

        return dialogBuilder.create()
    }

    private fun setTextViews(
        dialogViewBinding: DetailsDialogBinding,
        item: Item
    ) {
        dialogViewBinding.tvTitle.text = item.itemTitle
        dialogViewBinding.tvDetails.text = item.itemDesc
        dialogViewBinding.tvPrice.text = "$ ${item.price.toString()}"
        dialogViewBinding.ivImg.setImageResource(imageArray[item.category.toInt()])
    }
}