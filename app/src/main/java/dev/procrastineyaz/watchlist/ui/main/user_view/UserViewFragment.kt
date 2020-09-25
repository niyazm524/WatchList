package dev.procrastineyaz.watchlist.ui.main.user_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.databinding.FragmentUserViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserViewFragment : Fragment() {
    private val vm: UserViewViewModel by viewModel()
    private val args: UserViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentUserViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserViewBinding.inflate(inflater, container, false)
        binding.user = args.user
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(R.id.fcv_user_items, UserItemsFragment::class.java,
                bundleOf("userId" to args.user.id)
            )
            .commit()
    }
}
