package com.example.myapplication.auth

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var isLoading by remember { mutableStateOf(false) }
            val emailState = remember { mutableStateOf("") }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Reset Password",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (emailState.value.isEmpty()) {
                                Toast.makeText(
                                    applicationContext,
                                    "All fields are mandatory!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                isLoading = true
                                sendPasswordResetEmail(emailState.value) { success ->
                                    isLoading = false // Stop loading
                                    if (success) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Password reset mail sent successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onBackPressed()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Error sending password reset email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color.White),
                    ) {
                        Text(
                            text = if (isLoading) "Loading..." else "SEND MAIL",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }

    private fun sendPasswordResetEmail(email: String, callback: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
}
