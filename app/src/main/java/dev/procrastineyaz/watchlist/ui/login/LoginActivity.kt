package dev.procrastineyaz.watchlist.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.ui.main.MainActivity
import dev.procrastineyaz.watchlist.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val vm: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener { vm.onLoginClick() }
        tv_register.setOnClickListener {
            startActivityForResult(
                Intent(this, RegisterActivity::class.java),
                REQUEST_OPEN_REGISTER
            )
        }

        et_login.doOnTextChanged { text, _, _, _ ->
            vm.onLoginChanged(text.toString())
        }
        et_password.doOnTextChanged { text, _, _, _ ->
            vm.onPasswordChanged(text.toString())
        }

        vm.loginErrorMessage.observe(this, Observer<String?> { error ->
            setInputLayoutError(til_login, error)
        })
        vm.passwordErrorMessage.observe(this, Observer<String?> { error ->
            setInputLayoutError(til_password, error)
        })
        vm.progressState.observe(this, Observer { state ->
            when (state) {
                is LoginProgressState.None -> {
                    setInputLayoutError(til_login, null)
                    setInputLayoutError(til_password, null)
                    showProgress(false)
                }
                is LoginProgressState.Loading -> showProgress(true)
                is LoginProgressState.Success -> {
                    showProgress(false)
                    goToNextActivity()
                }
                is LoginProgressState.Error -> {
                    showProgress(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        })


    }

    private fun setInputLayoutError(til: TextInputLayout, errorMessage: String?) = til.apply {
        if (errorMessage != null) {
            error = errorMessage
            isErrorEnabled = true
        } else {
            error = null
            isErrorEnabled = false
        }
    }

    private fun showProgress(isShowing: Boolean) {
        btn_login.isEnabled = !isShowing
        btn_login.text = getString(
            if (isShowing) R.string.login_button_loading else R.string.login_button_text
        )
        progressBar.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OPEN_REGISTER && resultCode == RegisterActivity.RESULT_REGISTERED && data != null) {
            val username = requireNotNull(data.getStringExtra("username"))
            val password = requireNotNull(data.getStringExtra("password"))
            et_login.setText(username)
            et_password.setText(password)
        }
    }

    private fun goToNextActivity() = startActivity(Intent(this, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }).also { finish() }

    companion object {
        const val REQUEST_OPEN_REGISTER = 34
    }
}
