package dev.procrastineyaz.watchlist.ui.main.item_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import dev.procrastineyaz.watchlist.databinding.FragmentItemDetailsBinding
import kotlinx.android.synthetic.main.fragment_item_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemDetailsFragment : Fragment() {
    private val args: ItemDetailsFragmentArgs by navArgs()
    private val vm: ItemDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemDetailsBinding.inflate(inflater, container, false)

        binding.vm = vm
        binding.item = args.item
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_poster.load(args.item.posterUrl) {
            transformations(RoundedCornersTransformation(12f))
        }
    }
}
