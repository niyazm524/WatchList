package dev.procrastineyaz.watchlist.ui.main.common

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.databinding.FragmentAddItemBinding
import kotlinx.android.synthetic.main.fragment_add_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddItemDialogFragment : BottomSheetDialogFragment() {
    private val vm: AddItemDialogViewModel by viewModel()
    private val itemsAdapter = ItemsAdapter()
    private var seen: Boolean = false
    private var selectedCategory: Category = Category.UNKNOWN
    var onNewItemAdded: ((newItem: Item) -> Unit)? = null

    private val addingObserver = Observer<Result<Item, Throwable>> { result ->
        when (result) {
            is Result.Success -> onNewItemAdded?.invoke(result.value).also { dismiss() }
            is Result.Error -> Toast.makeText(
                activity,
                "Ошибка добавления: ${result.value.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddItemBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = vm
        seen = arguments?.getBoolean("seen") ?: false
        selectedCategory = Category.fromId(arguments?.getInt("category") ?: -1)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sv_items.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                sv_items.clearFocus()
                return vm.onSearch(query, selectedCategory)
            }

            override fun onQueryTextChange(newText: String?) = false
        })
        itemsAdapter.onItemClickListener = { item ->
            vm.onItemClick(item, seen).observe(viewLifecycleOwner, addingObserver)
        }
        rv_items.adapter = itemsAdapter
        vm.liveItems.observe(viewLifecycleOwner) { items ->
            Toast.makeText(activity, "items: ${items.loadedCount}", Toast.LENGTH_SHORT).show()
            itemsAdapter.submitList(items)
        }
        vm.isLoading.observe(viewLifecycleOwner) { loading ->
            pb_loading.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).also { dialog ->
            val offset: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                48f,
                resources.displayMetrics
            ).toInt()
            dialog.behavior.apply {
                peekHeight = resources.displayMetrics.heightPixels / 3 * 2
                isFitToContents = false
                state = BottomSheetBehavior.STATE_SETTLING
                expandedOffset = offset
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }

    companion object {
        fun newInstance(category: Category, seen: Boolean) =
            AddItemDialogFragment().also {
                it.arguments?.apply {
                    putInt("category", category.value)
                    putBoolean("seen", seen)
                }
            }
    }
}
