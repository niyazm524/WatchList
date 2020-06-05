package dev.procrastineyaz.watchlist.ui.home

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.ui.common.FilmsAdapter
import dev.procrastineyaz.watchlist.data.dto.Movie
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val vm: HomeViewModel by viewModel()
    private val filmsAdapter: FilmsAdapter = get()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_movies.apply {
            adapter = filmsAdapter
        }
        filmsAdapter.submitList(
            listOf(
                Movie(
                    id = 1,
                    title = "Остров проклятых",
                    titleGlobal = "Shutter island (2009)",
                    rating = 9.3f,
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

}
