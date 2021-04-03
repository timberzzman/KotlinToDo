package com.elouanmailly.todo.authentication

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.elouanmailly.todo.R
import com.elouanmailly.todo.SHARED_PREF_TOKEN_KEY
import com.elouanmailly.todo.databinding.FragmentLoginBinding
import com.elouanmailly.todo.databinding.FragmentSignupBinding
import com.elouanmailly.todo.network.Api
import com.elouanmailly.todo.network.LoginForm
import com.elouanmailly.todo.network.SignupForm
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSignupBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.signupFragmentSignupButton.setOnClickListener {
            val firstname = viewBinding.signupFragmentFirstnameInput.text.toString()
            val lastname = viewBinding.signupFragmentLastnameInput.text.toString()
            val email = viewBinding.signupFragmentEmailInput.text.toString()
            val password = viewBinding.signupFragmentPasswordInput.text.toString()
            val passwordConfirmation = viewBinding.signupFragmentPasswordInput.text.toString()
            val form = SignupForm(firstname, lastname, email, password, passwordConfirmation)
            Log.d("SIGNUPFRAGMENT", form.toString())
            lifecycleScope.launch {
                Log.d("SIGNUPFRAGMENT", "LAUNCHING API REQUEST")
                val response = Api.INSTANCE.userService.signup(form)
                Log.d("SIGNUPFRAGMENT", response.toString())
                if (response.isSuccessful) {
                    Log.d("SIGNUPFRAGMENT", "Request is successful")
                    Log.d("SIGNUPFRAGMENT", response.body().toString())
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, response.body()?.token)
                    }
                } else {
                    Toast.makeText(context, "An error has occured", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private lateinit var viewBinding: FragmentSignupBinding
}