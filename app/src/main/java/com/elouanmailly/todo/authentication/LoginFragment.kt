package com.elouanmailly.todo.authentication

import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.System.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.elouanmailly.todo.SHARED_PREF_TOKEN_KEY
import com.elouanmailly.todo.databinding.FragmentLoginBinding
import com.elouanmailly.todo.network.Api
import com.elouanmailly.todo.network.LoginForm
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.loginFragmentLoginButton.setOnClickListener {
            val email = viewBinding.loginFragmentEmailInput.text.toString()
            val password = viewBinding.loginFragmentPasswordInput.text.toString()
            val form = LoginForm(email, password)
            Log.d("LOGINFRAGMENT", form.toString())
            lifecycleScope.launch {
                Log.d("LOGINFRAGMENT", "LAUNCHING API REQUEST")
                val response = Api.INSTANCE.userService.login(form)
                Log.d("LOGINFRAGMENT", response.toString())
                if (response.isSuccessful) {
                    Log.d("LOGINFRAGMENT", "Request is successful")
                    Log.d("LOGINFRAGMENT", response.body().toString())
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, response.body()?.token)
                    }
                } else {
                    Toast.makeText(context, "An error has occured", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private lateinit var viewBinding: FragmentLoginBinding
}