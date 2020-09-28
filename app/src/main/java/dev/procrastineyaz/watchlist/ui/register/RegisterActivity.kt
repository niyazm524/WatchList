package dev.procrastineyaz.watchlist.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val vm: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.lifecycleOwner = this
        binding.viewModel = vm

        vm.result.observe(this) { result ->
            if(result is Result.Success) {
                finishWithResult(result.value)
            } else if(result is Result.Error) {
                Toast.makeText(
                    this,
                    "Произошла ошибка: ${result.value.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun finishWithResult(credentials: Credentials) {
        setResult(RESULT_REGISTERED, Intent().apply {
            putExtra("username", credentials.first)
            putExtra("password", credentials.second)
        })
        finish()
    }

    companion object {
        const val RESULT_REGISTERED = 12
    }
}
