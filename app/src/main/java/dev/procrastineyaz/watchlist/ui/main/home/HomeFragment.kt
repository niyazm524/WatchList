package dev.procrastineyaz.watchlist.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.ui.main.common.FilmPagesAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val vm: HomeViewModel by viewModel()
    private lateinit var filmPagesAdapter: FilmPagesAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filmPagesAdapter = FilmPagesAdapter(this)
        vp_movies_lists.adapter = filmPagesAdapter
        TabLayoutMediator(tabLayout, vp_movies_lists) { tab, position ->
            tab.text = getString(
                if(position == 0) R.string.tab_watched else R.string.tab_wish_list
            )
        }.attach()
    }

}
