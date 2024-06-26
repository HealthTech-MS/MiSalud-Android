package com.healthtech.misalud.core.viewModels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.healthtech.misalud.core.navigation.Navigation
import com.healthtech.misalud.core.storage.sharedPreferences.TokenManagement
import com.healthtech.misalud.core.network.data.services.AuthService
import com.healthtech.misalud.core.storage.sharedPreferences.UserManagement
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _authService = AuthService()

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber : LiveData<String> = _phoneNumber

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled : LiveData<Boolean> = _loginEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _errorText = MutableLiveData<String>()
    val errorText : LiveData<String> = _errorText

    fun onLoginChanged(phoneNumber: String, password: String){
        _phoneNumber.value = phoneNumber
        _password.value =  password

        _loginEnabled.value = isValidPhoneNumber(phoneNumber) && isValidPassword(password)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean = Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length == 10

    private fun isValidPassword(password: String): Boolean = password.length > 6

    fun onLoginSelected() {
        viewModelScope.launch {
            _isLoading.value = true

            val result = _authService.doLogin(phoneNumber.value!!, password.value!!)
            if(result.success == true) {

                TokenManagement.accessToken = result.accessToken.toString()
                TokenManagement.refreshToken = result.refreshToken.toString()

                UserManagement.saveUserAttributeString("phoneNumber", _phoneNumber.value!!)

                UserManagement.saveUserAttributeString("uuid", result.uuid.toString())

                _phoneNumber.value = ""
                _password.value = ""
                _errorText.value = ""

                Navigation.controller?.navigate("HomeScreen"){
                    popUpTo(Navigation.controller!!.graph.findStartDestination().id){
                        inclusive = true
                    }
                }
            } else {
                _errorText.value = result.error?.message.toString()
            }
            _isLoading.value = false
        }
    }

    fun navigate(route: String) {
        Navigation.controller!!.navigate(route)
    }
}