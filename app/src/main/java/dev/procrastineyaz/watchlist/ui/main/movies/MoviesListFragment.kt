package dev.procrastineyaz.watchlist.ui.main.movies

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf

import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Movie
import dev.procrastineyaz.watchlist.ui.main.common.FilmsAdapter
import kotlinx.android.synthetic.main.movies_list_fragment.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesListFragment : Fragment() {

    private val vm: MoviesListViewModel by viewModel()
    private val filmsAdapter: FilmsAdapter = get()
    private var seen: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        seen = arguments?.getBoolean("seen") ?: true
        return inflater.inflate(R.layout.movies_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_movies.adapter = filmsAdapter
        filmsAdapter.submitList(
            listOf(
                Movie(
                    id = 1,
                    title = "Остров проклятых",
                    titleGlobal = "Shutter island (2009)",
                    rating = if(seen) 9.3f else 8.0f,
                    note = "ДиКаприо сходит с ума на острове",
                    posterUri = getUriOfDrawable(R.drawable.film_1).toString()
                )
            )
        )
    }

    private fun getUriOfDrawable(@DrawableRes drawableId: Int): Uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(drawableId))
        .appendPath(resources.getResourceTypeName(drawableId))
        .appendPath(resources.getResourceEntryName(drawableId))
        .build()

    companion object {
        fun newInstance(seen: Boolean) = MoviesListFragment().apply {
            arguments = bundleOf("seen" to seen)
        }
    }

}
