package com.example.map_app.authentication

import android.app.Application
import android.text.Editable
import androidx.lifecycle.*
import com.example.map_app.database.User
import com.example.map_app.database.UserRepository
import com.example.map_app.util.InputTextUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val registerInputContainer = Array(4){ MutableLiveData<String?>(null) }
    private val loginInputContainer = Array(2){ MutableLiveData<String?>(null) }

    fun switchForm() {
        isInputValid.value=false
        registerInputContainer.flush()
        loginInputContainer.flush()
    }

    private val isInputValid = MutableLiveData<Boolean>()

    var termsOfUseSwitchPosition = MutableLiveData<Boolean>()
    val formCanBeSubmitted = MediatorLiveData<Boolean>()

    init {

        formCanBeSubmitted.addSource(isInputValid){
            val termComplete = termsOfUseSwitchPosition.value

            if(it==false)
                formCanBeSubmitted.value=false
            else
                formCanBeSubmitted.value=termComplete
        }

        formCanBeSubmitted.addSource(termsOfUseSwitchPosition){
            val inputComplete = isInputValid.value

            if(it==false)
                formCanBeSubmitted.value=false
            else
                formCanBeSubmitted.value=inputComplete
        }
    }

    private var repository : UserRepository = UserRepository(application)

    private val _loginData= MediatorLiveData<Boolean>()
    val loginData :LiveData<Boolean>
        get() = _loginData

    private val _regData= MediatorLiveData<Boolean>()
    val regData :LiveData<Boolean>
        get() = _regData

    private val users = repository.users

    private fun addUser(user : User){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }

    private fun authenticateUser(login : String, password : String) : LiveData<User> {
        return repository.authenticateUser(login, password)
    }

    fun onFormSubmit(
        key:Boolean=true,
    ){
        when(key){
            true->{
                val loggedUser = authenticateUser(
                    loginInputContainer[0].value!!,
                    loginInputContainer[1].value!!)
                _loginData.addSource(loggedUser){
                    _loginData.value = (it!=null)
                }
            }
            false-> {
                addUser(User(
                    0,
                    registerInputContainer[1].value!!,
                    registerInputContainer[0].value!!,
                    registerInputContainer[2].value!!
                ))
                _regData.addSource(users) {
                    if (it.isNotEmpty())
                        _regData.value =  (it.last().login == registerInputContainer[0].value)
                }
            }
        }
    }

    fun validateInput(input: TextInputLayout?){
        input?.isErrorEnabled=false
        val nestedEditText = input?.editText
        val inputTextUtils = InputTextUtils(input!!, nestedEditText?.text.toString())
        val tag : Pair<Int,String?> = nestedEditText?.tag as Pair<Int, String?>
        val (positionFromTag,typeFromTag) = tag
        val isRegister = (typeFromTag=="Register")

        fun maintainInput(inputIsValid : Boolean) :Boolean {
            return if (isRegister) {
                registerInputContainer.maintain(positionFromTag, nestedEditText.text, inputIsValid)
            } else{
                loginInputContainer.maintain(positionFromTag, nestedEditText.text, inputIsValid)
            }
        }

        if(isRegister){
            isInputValid.value = when(positionFromTag){
                1 -> maintainInput(inputTextUtils.validateEmail())
                0 -> maintainInput(inputTextUtils.validateLogin())
                2 -> {
                    registerInputContainer[positionFromTag.inc()].value=null
                    maintainInput(inputTextUtils.validatePassword())
                }
                3 -> {
                    val password = registerInputContainer[positionFromTag.dec()].value
                    maintainInput(inputTextUtils.validateConfirmPassword(password))
                }
                else -> false
            }
        }
        else {
            isInputValid.value= when(positionFromTag){
                0 -> maintainInput(inputTextUtils.validateLogin())
                1 -> maintainInput(inputTextUtils.validatePassword())
                else -> false
            }
        }
    }

    private fun Array<MutableLiveData<String?>>.maintain (
        position : Int,
        textValue : Editable?,
        inputIsValid : Boolean
    ) :Boolean
    {
        val observableValue = this[position]

        observableValue.value=null

        if(inputIsValid) {
            observableValue.value = textValue.toString().trim()
            return this.isValid()
        }
        return false
    }

    private fun Array<MutableLiveData<String?>>.isValid():Boolean{
        return !this.any { it.value==null }
    }

    private fun Array<MutableLiveData<String?>>.flush(){
        this.forEach { element-> element.value=null }
    }
}