package dev.procrastineyaz.watchlist.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val vm: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.lifecycleOwner = this
        binding.viewModel = vm

        vm.credentialsIfRegistered.observe(this, Observer { credentials ->
            if(credentials == null) {
                return@Observer
            }
            setResult(RESULT_REGISTERED, Intent().apply {
                putExtra("username", credentials.first)
                putExtra("password", credentials.second)
            })
            finish()
        })
    }

    companion object {
        const val RESULT_REGISTERED = 12
    }
}
