package dev.procrastineyaz.watchlist.ui.main.user_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.databinding.FragmentUserViewBinding
import dev.procrastineyaz.watchlist.ui.helpers.loadUserAvatar
import kotlinx.android.synthetic.main.fragment_user_view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class UserViewFragment : Fragment() {
    private val vm: UserViewViewModel by sharedViewModel()
    private val args: UserViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentUserViewBinding
    private lateinit var fragment: UserItemsFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(::binding.isInitialized) return binding.root
        binding = FragmentUserViewBinding.inflate(inflater, container, false)
        binding.user = args.user
        binding.vm = vm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_user_avatar.loadUserAvatar(args.user.avatarUrl)
        if(!this::fragment.isInitialized) fragment = UserItemsFragment().also {
            it.arguments = bundleOf("userId" to args.user.id)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.fcv_user_items, fragment)
            .commit()
        vm.itemForDetails.observe(viewLifecycleOwner) { item ->
            item?.let { (item, extras) -> openItemDetails(item, extras) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        childFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
        super.onSaveInstanceState(outState)
    }

    private fun openItemDetails(item: Item, extras: Navigator.Extras) {
        val action = UserViewFragmentDirections.actionNavigationUserViewToItemDetails(item = item)
        findNavController().navigate(action)
    }
}
