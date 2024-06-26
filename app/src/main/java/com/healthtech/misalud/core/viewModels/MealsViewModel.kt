package com.healthtech.misalud.core.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtech.misalud.core.models.MealRecord
import com.healthtech.misalud.core.navigation.Navigation
import com.healthtech.misalud.core.network.data.services.PeopleService
import com.healthtech.misalud.core.storage.sharedPreferences.TokenManagement
import com.healthtech.misalud.core.storage.sharedPreferences.UserManagement
import com.healthtech.misalud.ui.components.FilterItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MealsViewModel: ViewModel() {

    private val _peopleService = PeopleService()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //MealRecord
    private val _filterItemList = MutableLiveData<List<FilterItem>>()
    val filterItemList : LiveData<List<FilterItem>> = _filterItemList

    private val _records = MutableLiveData<List<MealRecord>>()
    val records: LiveData<List<MealRecord>> = _records

    private val _allowedDays = MutableLiveData<List<String>>()
    val allowedDays: LiveData<List<String>> = _allowedDays

    //MealRegistry

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _selectorItemList = MutableLiveData<List<String>>()
    val selectorItemList: LiveData<List<String>> = _selectorItemList

    private val _selectorState = MutableLiveData<String>()
    val selectorState: LiveData<String> = _selectorState

    private val _score = MutableLiveData<Float>()
    val score: LiveData<Float> = _score

    private val _submitEnabled = MutableLiveData<Boolean>()
    val submitEnabled: LiveData<Boolean> = _submitEnabled

    fun onNameChange(text: String){
        _name.value = text
        val hasNumbers = text.any { it.isDigit() }
        _submitEnabled.value = _name.value!!.length > 2 && !hasNumbers
    }

    fun onSelectorChange(state: String){
        _selectorState.value = state
    }

    fun onScoreChange(text: Float){
        _score.value = text
    }

    fun setItemList(list: List<String>){
        _selectorItemList.value = list
    }

    fun setFilterItems(list: List<FilterItem>){
        _filterItemList.value = list
    }

    fun changeFilter(index: Int, range: String){
        val newList = _filterItemList.value!!.mapIndexed { idx, item ->
            if (idx == index) item.copy(selected = true) else item.copy(selected = false)
        }
        _filterItemList.value = newList
        getRecords(range)
    }

    fun navigate(route: String) {
        Navigation.controller!!.navigate(route)
    }

    fun getRecords(range: String){
        viewModelScope.launch {
            _isLoading.value = true

            val accessToken = "Bearer " + TokenManagement.accessToken
            val uuid = UserManagement.getUserAttributeString("uuid")!!

            val result = async { _peopleService.doGetMealRecords(accessToken, uuid, range) }
            val infoDeffered = result.await()

            if(infoDeffered.success){
                _records.value = infoDeffered.records
            } else {
                _records.value = listOf()
            }

            _isLoading.value = false
        }
    }

    fun getRecordDays(){
        viewModelScope.launch {
            _isLoading.value = true

            val accessToken = "Bearer " + TokenManagement.accessToken
            val uuid = UserManagement.getUserAttributeString("uuid")!!

            val result = async { _peopleService.doGetMealRecordDays(accessToken, uuid) }
            val infoDeffered = result.await()

            if(infoDeffered.success){
                _allowedDays.value = infoDeffered.dates
            } else {
                _allowedDays.value = listOf()
            }

            _isLoading.value = false
        }
    }

    fun addRecord(){
        viewModelScope.launch {
            _isLoading.value = true

            val accessToken = "Bearer " + TokenManagement.accessToken
            val uuid = UserManagement.getUserAttributeString("uuid")!!

            val result = _peopleService.doAddMealRecord(accessToken, uuid, _name.value!!, _selectorState.value!!, _score.value!!)
            if(result.success == true){
                _name.value = ""
                _selectorState.value = _selectorItemList.value!![0]
                _score.value = 1f

                Navigation.controller!!.navigate("MealRecord")
            } else {
                //_errorText.value = result.error?.message.toString()
            }

            _isLoading.value = false
        }
    }
}