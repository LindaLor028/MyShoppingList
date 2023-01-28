package com.macalester.myshoppinglist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.macalester.myshoppinglist.R
import com.macalester.myshoppinglist.ScrollingActivity
import com.macalester.myshoppinglist.data.Item
import com.macalester.myshoppinglist.databinding.DeleteDialogBinding
import com.macalester.myshoppinglist.databinding.ItemDialogBinding

class DeleteDialog : DialogFragment()  {

    lateinit var itemDialogHandler: ItemDialog.ItemDialogHandler
    private var isDelAllMode = false

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

        val dialogViewBinding = DeleteDialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        // change title accordingly
        if (arguments != null && requireArguments().containsKey(ScrollingActivity.KEY_ITEM_DELETE)) {
            dialogBuilder.setTitle(getString(R.string.delete_item))
        } else if (arguments != null && requireArguments().containsKey(ScrollingActivity.KEY_ITEM_DELETE_ALL)) {
            dialogBuilder.setTitle(getString(R.string.delete_all))
            isDelAllMode = true
        }

        dialogBuilder.setPositiveButton(getString(R.string.confirm)) {
                dialog, which ->

             if (isDelAllMode){
                itemDialogHandler.itemsDeleted()
            }
            else {
                 val item = requireArguments().get(ScrollingActivity.KEY_ITEM_DELETE) as Item
                 itemDialogHandler.itemDeleted(item)
             }
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel)) {
                dialog, which ->
        }

        return dialogBuilder.create()
    }


}