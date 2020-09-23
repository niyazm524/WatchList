package dev.procrastineyaz.watchlist.ui.main.common

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.databinding.FragmentAddItemBinding
import kotlinx.android.synthetic.main.fragment_add_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddItemDialogFragment : BottomSheetDialogFragment() {
    private val vm: AddItemDialogViewModel by viewModel()
    private val args: AddItemDialogFragmentArgs by navArgs()
    private val itemsAdapter = ItemsAdapter()

    private val addingObserver = Observer<Result<Any?, Throwable>> { result ->
        when (result) {
            is Result.Success -> setFragmentResult(
                "adding",
                bundleOf("succeed" to true)
            ).also { dismiss() }
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sv_items.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                sv_items.clearFocus()
                return vm.onSearch(query, args.category)
            }

            override fun onQueryTextChange(newText: String?) = false
        })
        itemsAdapter.onItemClickListener = { item, _ ->
            vm.onItemClick(item, args.seen).observe(viewLifecycleOwner, addingObserver)
        }
        rv_items.adapter = itemsAdapter
        vm.liveItems.observe(viewLifecycleOwner) { items ->
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
}
