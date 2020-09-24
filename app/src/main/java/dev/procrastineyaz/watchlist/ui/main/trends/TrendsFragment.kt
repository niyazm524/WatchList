package dev.procrastineyaz.watchlist.ui.main.trends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.databinding.FragmentTrendsBinding
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapter
import kotlinx.android.synthetic.main.fragment_trends.*
import kotlinx.android.synthetic.main.layout_chips.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrendsFragment : Fragment() {

    private val vm: TrendsViewModel by viewModel()
    private lateinit var binding: FragmentTrendsBinding
    private val itemsAdapter: ItemsAdapter = get()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(this::binding.isInitialized) return binding.root
        binding = FragmentTrendsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_items.adapter = itemsAdapter
        cg_categories.setOnCheckedChangeListener { _, checkedId ->
            vm.selectCategory(
                when (checkedId) {
                    R.id.chip_films -> Category.FILM
                    R.id.chip_series -> Category.SERIES
                    R.id.chip_anime -> Category.ANIME
                    else -> Category.UNKNOWN
                }
            )
        }
        cg_categories.check(R.id.chip_all)
        vm.items.observe(viewLifecycleOwner) { items ->
            itemsAdapter.submitList(items)
        }
        vm.networkState.observe(viewLifecycleOwner) { state ->
            if(state is NetworkState.Failure) {
                Toast.makeText(
                    context,
                    "Error: ${state.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
