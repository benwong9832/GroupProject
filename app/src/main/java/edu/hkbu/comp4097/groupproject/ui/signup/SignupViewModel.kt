package edu.hkbu.comp4097.groupproject.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val email = MutableLiveData<String>().apply {
        value = ""
    }
    val password = MutableLiveData<String>().apply {
        value = ""
    }
    val confirmPassword = MutableLiveData<String>().apply {
        value = ""
    }
    val displayName = MutableLiveData<String>().apply {
        value = ""
    }
    val avatarUrl = MutableLiveData<String>().apply {
        value = ""
    }
}