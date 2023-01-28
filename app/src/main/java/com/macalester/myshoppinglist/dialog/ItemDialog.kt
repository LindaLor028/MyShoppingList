package com.macalester.myshoppinglist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.macalester.myshoppinglist.R
import com.macalester.myshoppinglist.ScrollingActivity
import com.macalester.myshoppinglist.data.Item
import com.macalester.myshoppinglist.databinding.ItemDialogBinding
import com.macalester.myshoppinglist.model.InputModel


class  ItemDialog : DialogFragment() {

    interface ItemDialogHandler {
        public fun itemCreated(item: Item)

        public fun itemUpdated(item: Item)

        public fun itemDeleted(item: Item)

        public fun itemsDeleted()
    }

    lateinit var itemDialogHandler: ItemDialogHandler
    private var isEditMode = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemDialogHandler) {
            itemDialogHandler = context
        } else {
            throw java.lang.RuntimeException(
                getString(R.string.item_dialog_handler_attach_error))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        val dialogViewBinding = ItemDialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        // change title accordingly
        if (arguments != null && requireArguments().containsKey(ScrollingActivity.KEY_ITEM_EDIT) && requireArguments().get(ScrollingActivity.KEY_ITEM_EDIT) != null) {
            dialogBuilder.setTitle(getString(R.string.edit_item))
            isEditMode = true
            val item = requireArguments().get(ScrollingActivity.KEY_ITEM_EDIT) as Item
            setUpEdit(dialogViewBinding, item)

        } else {
            dialogBuilder.setTitle(getString(R.string.add_item))
            isEditMode = false
        }

        // handle positive button actions
        dialogBuilder.setPositiveButton(getString(R.string.confirm)) {
                dialog, which ->

            val spinner = dialogViewBinding.spinner

            if (checkNulls(dialogViewBinding)) {
                if (isEditMode) {
                    editItem(dialogViewBinding)
                } else {
                    // create a new item
                    itemDialogHandler.itemCreated(
                        Item(
                            null,
                            dialogViewBinding.etTitle.text.toString(),
                            dialogViewBinding.etDetails.text.toString(),
                            dialogViewBinding.etPrice.text.toString().toFloat(),
                            Integer(spinner.getSelectedItemPosition()),
                            dialogViewBinding.cbBought.isChecked
                        )
                    )
                }
            }
        }

        // handle negative button actions
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) {
                dialog, which ->
        }

        return dialogBuilder.create()
    }

    private fun editItem(dialogViewBinding: ItemDialogBinding) {
        val itemToEdit =
            (requireArguments().getSerializable(
                ScrollingActivity.KEY_ITEM_EDIT
            ) as Item).copy(
                itemTitle = dialogViewBinding.etTitle.text.toString(),
                isBought = dialogViewBinding.cbBought.isChecked,
                price = dialogViewBinding.etPrice.text.toString().toFloat(),
                itemDesc = dialogViewBinding.etDetails.text.toString(),
                category = dialogViewBinding.spinner.selectedItemPosition as Integer
            )
        itemDialogHandler.itemUpdated(itemToEdit)
    }

    private fun setUpEdit(dialogViewBinding : ItemDialogBinding, item : Item) {
        dialogViewBinding.etTitle.setText(item.itemTitle)
        dialogViewBinding.etPrice.setText(item.price.toString())
        dialogViewBinding.etDetails.setText(item.itemDesc)
        dialogViewBinding.cbBought.isChecked = item.isBought
        dialogViewBinding.spinner.setSelection(item.category.toInt())
    }

    private fun checkNulls(dialogViewBinding : ItemDialogBinding) : Boolean {
        return if (InputModel.validateInput(dialogViewBinding.etTitle.text.toString())) {
            false
        } else {
            if (InputModel.validateInput(dialogViewBinding.etPrice.text.toString())) {
                dialogViewBinding.etPrice.setText(getString(R.string.null_price_filler))
            }
            if (InputModel.validateInput(dialogViewBinding.etDetails.text.toString())) {
                dialogViewBinding.etDetails.setText(getString(R.string.null_details_filler))
            }
            true
        }
    }

}

