package dev.procrastineyaz.watchlist.ui.main.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.UserType
import dev.procrastineyaz.watchlist.databinding.FragmentMoreBinding
import dev.procrastineyaz.watchlist.services.TokenService
import dev.procrastineyaz.watchlist.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_more.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoreFragment : Fragment() {
    private val tokenService: TokenService by inject()
    private val vm: MoreViewModel by viewModel()
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.username = tokenService.username
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.isLogout.observe(viewLifecycleOwner) {
            if(it == false) { return@observe }
            startActivity(Intent(activity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
        vm.userData.observe(viewLifecycleOwner) { user ->
            user?.let { binding.user = it }
        }
        vm.getUserData()
        iv_avatar.load(R.drawable.sharingan) {
            transformations(CircleCropTransformation())
        }

        linearLayout_subscriptions.setOnClickListener {
            openUsersFragment(UserType.Subscription)
        }

        linearLayout_subscribers.setOnClickListener {
            openUsersFragment(UserType.Subscriber)
        }

        btn_action_share_app.setOnClickListener {
            Toast.makeText(
                context,
                "Отправка статистики и ссылки на приглашение будет реализована позже",
                Toast.LENGTH_LONG
            ).show()
        }

        iv_avatar.setOnClickListener {
            Toast.makeText(
                context,
                "Функция смены аватара появится в следующей версии",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openUsersFragment(userType: UserType) {
        val action = MoreFragmentDirections.actionNavigationMoreToUsers(userType)
        findNavController().navigate(action)
    }
}
