package dev.procrastineyaz.watchlist.ui.main.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.UserType
import kotlinx.android.synthetic.main.fragment_users.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class UsersFragment : Fragment() {
    private val args: UsersFragmentArgs by navArgs()
    private val vm: UsersViewModel by viewModel()
    private val usersAdapter = UsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_fragment_title.text =
            if (args.userType == UserType.Subscriber) "Подписчики" else "Подписки"
        rv_users.adapter = usersAdapter
        vm.users.observe(viewLifecycleOwner) { users ->
            usersAdapter.submitList(users)
        }
        vm.invalidateItems(args.userType)
    }
}
