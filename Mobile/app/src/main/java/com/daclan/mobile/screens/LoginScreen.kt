package com.daclan.mobile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daclan.mobile.network.LoginRequest
import com.daclan.mobile.network.RetrofitClient
import com.daclan.mobile.util.Prefs
import kotlinx.coroutines.launch
import org.json.JSONObject
import androidx.compose.material3.Divider


@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onGoRegister: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A2332),
                        Color(0xFF2D4A6F),
                        Color(0xFF5A7A9E),
                        Color(0xFFD4917E),
                        Color(0xFF7DA3C6)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1FFFFFFF)),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🌙", fontSize = 48.sp, modifier = Modifier.padding(bottom = 12.dp))

                    Text(
                        text = "Welcome Back",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Sign in to your account",
                        fontSize = 15.sp,
                        color = Color(0xCCFFFFFF),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Error message
                    if (errorMsg.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0x33EF4444))
                        ) {
                            Text(
                                text = errorMsg,
                                color = Color(0xFFFECACA),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    // Email
                    Text(
                        text = "✉  Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Enter your email", color = Color(0x80FFFFFF)) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0x80FFFFFF),
                            unfocusedBorderColor = Color(0x4DFFFFFF),
                            focusedContainerColor = Color(0x26FFFFFF),
                            unfocusedContainerColor = Color(0x1AFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    // Password
                    Text(
                        text = "🔒  Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Enter your password", color = Color(0x80FFFFFF)) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 28.dp),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0x80FFFFFF),
                            unfocusedBorderColor = Color(0x4DFFFFFF),
                            focusedContainerColor = Color(0x26FFFFFF),
                            unfocusedContainerColor = Color(0x1AFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    // Login Button
                    Button(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty()) {
                                errorMsg = "Please fill in all fields"
                                return@Button
                            }
                            errorMsg = ""
                            isLoading = true
                            scope.launch {
                                try {
                                    val response = RetrofitClient.api.login(LoginRequest(email, password))
                                    if (response.isSuccessful) {
                                        val body = response.body()!!
                                        Prefs.saveToken(context, body.token)
                                        Prefs.saveUser(context, body.user.email, body.user.firstName, body.user.lastName)
                                        onLoginSuccess()
                                    } else {
                                        val err = response.errorBody()?.string()
                                        errorMsg = try {
                                            JSONObject(err!!).getString("error")
                                        } catch (e: Exception) { "Login failed" }
                                    }
                                } catch (e: Exception) {
                                    errorMsg = "Network error: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xCC3B82F6)),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Sign In", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(
                        color = Color(0x33FFFFFF),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    TextButton(onClick = onGoRegister) {
                        Text(
                            text = "Don't have an account? Sign up",
                            color = Color(0xFFA5D8FF),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}