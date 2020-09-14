package dev.procrastineyaz.watchlist.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.databinding.FragmentHomeBinding
import dev.procrastineyaz.watchlist.ui.main.common.CategoryItemPagesAdapter
import dev.procrastineyaz.watchlist.ui.main.movies.MoviesListFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val vm: HomeViewModel by viewModel()
    private lateinit var categoryItemPagesAdapter: CategoryItemPagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_home, container, false)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = vm
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryItemPagesAdapter =
            CategoryItemPagesAdapter(this, MoviesListFragment.Companion.Providers.HomeVM)
        vp_movies_lists.adapter = categoryItemPagesAdapter
        TabLayoutMediator(tabLayout, vp_movies_lists) { tab, position ->
            tab.text = getString(
                if (position == 0) R.string.tab_watched else R.string.tab_wish_list
            )
            vm.setCurrentTab(if (position == 0) SeenParameter.SEEN else SeenParameter.UNSEEN)
        }.attach()
    }

}
