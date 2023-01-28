package com.macalester.myshoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.macalester.myshoppinglist.adapter.ItemAdapter
import com.macalester.myshoppinglist.data.AppDatabase
import com.macalester.myshoppinglist.data.Item
import com.macalester.myshoppinglist.databinding.ActivityScrollingBinding
import com.macalester.myshoppinglist.dialog.DeleteDialog
import com.macalester.myshoppinglist.dialog.DetailsDialog
import com.macalester.myshoppinglist.dialog.ItemDialog
import com.macalester.myshoppinglist.touch.ItemRecyclerTouchCallback
import com.macalester.myshoppinglist.viewmodel.ItemViewModel

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemDialogHandler {

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var adapter: ItemAdapter


    companion object {
        const val KEY_ITEM_DIALOG = "ITEM_DIALOG"
        const val KEY_ITEM_EDIT = "KEY_TODO_EDIT"
        const val KEY_ITEM_DELETE = "KEY_ITEM_DELETE"
        const val KEY_ITEM_DELETE_ALL = "KEY_ITEM_DELETE_ALL"
        const val KEY_ITEM_DETAILS = "KEY_ITEM_DETAILS"

        const val TAG_ITEM_EDIT = "TAG_ITEM_EDIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemViewModel = ViewModelProvider(this)[ItemViewModel::class.java]

        initRecyclerView()

        val itemDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        binding.rvItem.addItemDecoration(itemDivider)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        binding.fabAddItem.setOnClickListener { view ->
            val itemDialog = ItemDialog()
            itemDialog.show(supportFragmentManager, KEY_ITEM_DIALOG)
        }

        binding.fabDeleteAll.setOnClickListener{
            showDeleteAllDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            // after each query change, check results
            override fun onQueryTextChange(newText: String?): Boolean {
                AppDatabase.getInstance(this@ScrollingActivity).itemDao().findItems(newText!!)
                    .observe(
                        this@ScrollingActivity, Observer { items ->
                            adapter.submitList(items)
                        })

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun initRecyclerView() {
        adapter = ItemAdapter(this, itemViewModel)
        binding.rvItem.adapter = adapter

        val callback: ItemTouchHelper.Callback = ItemRecyclerTouchCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.rvItem)

        itemViewModel.allItems.observe(this) { items ->
            adapter.submitList(items)
        }
    }

    override fun itemCreated(item: Item) { // coming from the interface !
        itemViewModel.insertItem(item)

        Snackbar.make(
            binding.root,
            "Item added",
            Snackbar.LENGTH_LONG
        ).setAction("Undo"
        ) {
        }.show()
    }

    override fun itemUpdated(item: Item) {
        itemViewModel.updateItem(item)
    }

    override fun itemDeleted(item: Item) {
        itemViewModel.deleteItem(item)
    }

    override fun itemsDeleted() {
        itemViewModel.deleteAllItems()
    }

    fun showEditDialog(item: Item){
        val itemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_EDIT, item)
        itemDialog.arguments = bundle

        itemDialog.show(supportFragmentManager, TAG_ITEM_EDIT)
    }

    fun showDeleteDialog(item: Item) {
        val delDialog = DeleteDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_DELETE, item)
        delDialog.arguments = bundle

        delDialog.show(supportFragmentManager, KEY_ITEM_DELETE)
    }

    fun showDetailsDialog(item: Item) {
        val detailsDialog = DetailsDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_DETAILS, item)
        detailsDialog.arguments = bundle

        detailsDialog.show(supportFragmentManager, KEY_ITEM_DETAILS)
    }

    private fun showDeleteAllDialog() {
        val delDialog = DeleteDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_DELETE_ALL, null)
        delDialog.arguments = bundle

        delDialog.show(supportFragmentManager, KEY_ITEM_DELETE_ALL)
    }
}