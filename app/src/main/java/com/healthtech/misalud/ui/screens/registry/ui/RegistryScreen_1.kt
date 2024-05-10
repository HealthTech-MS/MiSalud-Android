package com.healthtech.misalud.ui.screens.registry.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthtech.misalud.R
import com.healthtech.misalud.ui.screens.registry.vm.RegistryViewModel

@Composable
fun RegistryScreen_1(viewModel: RegistryViewModel) {
    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Registry_1(
            modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
            viewModel = viewModel
        )
    }
}

@Composable
fun Registry_1(modifier: Modifier, viewModel: RegistryViewModel){

    val firstName : String by viewModel.firstName.observeAsState(initial = "")
    val lastName : String by viewModel.lastName.observeAsState(initial = "")
    val phoneNumber : String by viewModel.phoneNumber.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")
    val confirmPassword : String by viewModel.confirmPassword.observeAsState(initial = "")

    val registerEnabled: Boolean by viewModel.registerEnabled.observeAsState(initial = false)

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        GenericTextField(
            title="Primer Nombre",
            textValue=firstName,
            keyboardType=KeyboardType.Text,
            spaced=true
        ) { viewModel.onRegisterFormChanged(it, lastName, phoneNumber, password, confirmPassword) }
        GenericTextField(
            title="Apellido Paterno",
            textValue=lastName,
            keyboardType=KeyboardType.Text,
            spaced=true
        ) { viewModel.onRegisterFormChanged(firstName, it, phoneNumber, password, confirmPassword) }
        GenericTextField(
            title="Telefono",
            textValue=phoneNumber,
            keyboardType=KeyboardType.Phone,
            spaced=true
        ) { viewModel.onRegisterFormChanged(firstName, lastName, it, password, confirmPassword) }
        PasswordField(
            title="Ingresa una Contraseña",
            textValue=password,
            spaced=true
        ) { viewModel.onRegisterFormChanged(firstName, lastName, phoneNumber, it, confirmPassword) }
        PasswordField(
            title="Confirma tu Contraseña",
            textValue=confirmPassword,
            spaced=true
        ) { viewModel.onRegisterFormChanged(firstName, lastName, phoneNumber, password, it) }
        RegisterButton(registerEnabled = registerEnabled, viewModel)
        AccountsDivider()
        LoginLegend(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            viewModel = viewModel
        )
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.misalud_transparent),
        contentDescription = "Header",
        modifier = modifier
    )
    Spacer(modifier = Modifier.padding(16.dp))
}

@Composable
fun GenericTextField(title: String, textValue: String, keyboardType: KeyboardType, spaced: Boolean, onChange: (String) -> Unit){
    TextField(
        value = textValue,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = title) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )

    if(spaced){
        Spacer(modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun PasswordField(title: String, textValue: String, spaced: Boolean, onChange: (String) -> Unit){
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = textValue,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = title) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            val image = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Ocultar Contraseña" else "Mostrar Contraseña"

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, description)
            }
        }
    )

    if(spaced){
        Spacer(modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun RegisterButton(registerEnabled: Boolean, registryViewModel: RegistryViewModel) {
    Spacer(modifier = Modifier.padding(10.dp))
    Button(
        enabled = registerEnabled,
        onClick = { registryViewModel.onRegisterSelected() },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(44, 90, 168),
            disabledContainerColor = Color(99, 156, 255, 255),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Crear Cuenta")
    }
}

@Composable
fun AccountsDivider() {
    Spacer(modifier = Modifier.padding(10.dp))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        HorizontalDivider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .weight(1f)
        )
        Text(text = "O",
            modifier = Modifier.padding(horizontal = 18.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB5B5B5)
        )
        HorizontalDivider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .weight(1f)
        )
    }
}

@Composable
fun LoginLegend(modifier: Modifier, viewModel: RegistryViewModel){
    Spacer(modifier = Modifier.padding(10.dp))
    Text(
        text = "Inicia Sesión",
        modifier = modifier.clickable { viewModel.navigate("LoginScreen")},
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(26, 152, 220)
    )
}