package com.healthtech.misalud.core.network.data.services

import android.util.Log
import com.google.gson.Gson
import com.healthtech.misalud.core.network.retrofit.RetrofitHelper
import com.healthtech.misalud.core.network.data.clients.AuthClient
import com.healthtech.misalud.core.network.data.requests.AuthRequests
import com.healthtech.misalud.core.network.data.responses.AuthResponses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AuthService {
    private val retrofit = RetrofitHelper.getRetrofitAuth()

    suspend fun doLogin(phoneNumber: String, password: String) : AuthResponses.PostLogin {
        val loginRequest = AuthRequests.PostLogin(phoneNumber, password)

        return withContext(Dispatchers.IO){
            val response = retrofit.create(AuthClient::class.java).doLogin(loginRequest)

            if(!response.isSuccessful) {
                val jsonObject = JSONObject(response.errorBody()!!.charStream().readText()).toString()
                return@withContext Gson().fromJson<AuthResponses.PostLogin?>(jsonObject, AuthResponses.PostLogin::class.java)
            }

            return@withContext response.body()!!
        }
    }

    suspend fun doLogout(refreshToken: String) : AuthResponses.PostLogout {
        val logoutRequest = AuthRequests.PostLogout(refreshToken)

        return withContext(Dispatchers.IO){
            val response = retrofit.create(AuthClient::class.java).doLogout(logoutRequest)

            if(!response.isSuccessful) {
                val jsonObject = JSONObject(response.errorBody()!!.charStream().readText()).toString()
                return@withContext Gson().fromJson<AuthResponses.PostLogout?>(jsonObject, AuthResponses.PostLogout::class.java)
            }

            return@withContext response.body()!!
        }
    }

    suspend fun doCreateAccount(firstName: String, lastName: String, phoneNumber: String, password: String) : AuthResponses.PostLogin {
        val registryRequest = AuthRequests.PostRegistry(firstName, lastName, phoneNumber, password)

        Log.i("Test", registryRequest.toString())

        return withContext(Dispatchers.IO){
            val response = retrofit.create(AuthClient::class.java).doRegisterUser(registryRequest)

            Log.i("Test", response.toString())

            if(!response.isSuccessful) {
                val jsonObject = JSONObject(response.errorBody()!!.charStream().readText()).toString()
                Log.i("TEst", jsonObject)
                return@withContext Gson().fromJson<AuthResponses.PostLogin?>(jsonObject, AuthResponses.PostLogin::class.java)
            }

            return@withContext response.body()!!
        }
    }

    suspend fun doChangePassword(uuid: String, newPassword: String) : AuthResponses.PostChangePassword {
        val changePasswordRequest = AuthRequests.PostChangePassword(uuid, newPassword)

        return withContext(Dispatchers.IO){
            val response = retrofit.create(AuthClient::class.java).doChangePassword(changePasswordRequest)

            if(!response.isSuccessful) {
                val jsonObject = JSONObject(response.errorBody()!!.charStream().readText()).toString()
                return@withContext Gson().fromJson<AuthResponses.PostChangePassword?>(jsonObject, AuthResponses.PostChangePassword::class.java)
            }

            return@withContext response.body()!!
        }
    }

}