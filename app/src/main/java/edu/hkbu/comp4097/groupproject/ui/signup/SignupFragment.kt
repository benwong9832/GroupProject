package edu.hkbu.comp4097.groupproject.ui.signup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import edu.hkbu.comp4097.groupproject.MainActivity
import edu.hkbu.comp4097.groupproject.R
import edu.hkbu.comp4097.groupproject.data.Code
import edu.hkbu.comp4097.groupproject.databinding.SignupFragmentBinding

class SignupFragment : Fragment() {

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

    lateinit var binding: SignupFragmentBinding

    var mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this  // missing this line, page will not be updated because don't lifecycle of this fragment
        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        binding.vm = viewModel

        binding.buttonSignup.setOnClickListener { view ->
            if (viewModel.email.value.isNullOrBlank()) {
                binding.editTextEmail.error = "Email is blank"
                return@setOnClickListener
            }

            if (viewModel.displayName.value.isNullOrBlank()) {
                binding.editTextDisplayName.error = "Display Name is blank"
                return@setOnClickListener
            }

            if (!viewModel.confirmPassword.value.equals(viewModel.password.value)) {
                binding.editTextConfirmPassword.error = "Password do not match"
                return@setOnClickListener
            }

            if (viewModel.password.value.isNullOrBlank()) {
                binding.editTextPassword.error = "Password is blank"
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword("${viewModel.email.value}", "${viewModel.password.value}").continueWithTask { auth ->
                val req = UserProfileChangeRequest.Builder()
                req.displayName = "${viewModel.displayName.value}"
                auth.result?.user?.updateProfile(req.build())
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                Toast.makeText(context, "Welcome, ${viewModel.displayName.value}!", Toast.LENGTH_LONG).show()
                activity?.apply {
                    setResult(Activity.RESULT_OK)
                    finishActivity(Code.SIGNUP_RESULT)
                    val MainIntent = Intent(this, MainActivity::class.java)
                    startActivityForResult(MainIntent, Code.SIGNUP_RESULT)
                }
            }
        }

        binding.imageViewAvatar.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(mediaIntent, Code.IMAGE_RESULT)
        }

        return binding.root

        //    return inflater.inflate(R.layout.signup_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //     viewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)
        // TODO: Use the ViewModel
        activity?.let {
            viewModel.email.value = it.intent.getStringExtra("email")
            viewModel.password.value = it.intent.getStringExtra("password")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Code.IMAGE_RESULT && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                binding.vm?.avatarUrl?.value = "$it"
            }
        }
    }

}