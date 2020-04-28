package dev.procrastineyaz.watchlist.ui.feed

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.ui.common.FilmsAdapter
import dev.procrastineyaz.watchlist.ui.common.data.Movie
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var dashboardViewModel: FeedViewModel
    private val filmsAdapter = FilmsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

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
