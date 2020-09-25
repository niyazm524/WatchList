package dev.procrastineyaz.watchlist.ui.main.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.SubscribeUser
import dev.procrastineyaz.watchlist.data.dto.UserType
import kotlinx.android.synthetic.main.fragment_subscriptions.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class UsersFragment : Fragment() {
    private val args: UsersFragmentArgs by navArgs()
    private val vm: UsersViewModel by viewModel()
    private val usersAdapter = UsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_fragment_title.text =
            if (args.userType == UserType.Subscriber) "Подписчики" else "Подписки"
        btn_add_user.visibility = if (args.userType == UserType.Subscriber) View.GONE else View.VISIBLE
        btn_add_user.setOnClickListener {
            openAddUserDialog()
        }
        rv_users.adapter = usersAdapter
        vm.users.observe(viewLifecycleOwner) { users ->
            usersAdapter.submitList(users)
        }
        vm.invalidateItems(args.userType)
        usersAdapter.onUserClickListener = UserClickListener { user ->
            if((user.isSubscriber && user.subscribedBack) || (!user.isSubscriber && user.approved)) {
                openUserView(user)
            } else {
                Toast.makeText(context, "Ты не избранный, увы", Toast.LENGTH_SHORT).show()
            }
        }

        usersAdapter.onSubscribeTo = UserClickListener(vm::onUserActionClicked)
    }

    private fun openAddUserDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val addUserSV = view.findViewById<SearchView>(R.id.sv_add_user)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Подписка на пользователя")
            .setPositiveButton("Добавить") { dialog, _ ->
                val query = addUserSV.query?.toString()?.trim()
                if(query.isNullOrEmpty()) {
                    Toast.makeText(context, "Введите имя пользователя", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                vm.onAddUser(query)
                dialog.dismiss()
            }
            .setNeutralButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
            .setView(view)
            .show()
    }

    private fun openUserView(user: SubscribeUser) {
        val action = UsersFragmentDirections.actionNavigationUsersToUserView(user)
        findNavController().navigate(action)
    }
}
