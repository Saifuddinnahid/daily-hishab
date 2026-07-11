package com.nahid.dailyhisab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nahid.dailyhisab.data.repository.AuthRepository
import com.nahid.dailyhisab.security.BiometricAuth
import com.nahid.dailyhisab.ui.auth.AuthScreen
import com.nahid.dailyhisab.ui.navigation.MainNavGraph
import com.nahid.dailyhisab.ui.security.BiometricLockScreen
import com.nahid.dailyhisab.ui.theme.DailyHisabTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DailyHisabTheme {
                var currentScreen by remember { mutableStateOf("auth") }
                var userEmail by remember { mutableStateOf("") }
                var showBiometric by remember { mutableStateOf(false) }

                when {
                    showBiometric -> {
                        BiometricLockScreen(
                            onAuthenticated = {
                                showBiometric = false
                            },
                            onUsePassword = {
                                showBiometric = false
                            }
                        )
                    }
                    currentScreen == "auth" -> {
                        AuthScreen(
                            authRepository = authRepository,
                            onLoginSuccess = { email ->
                                userEmail = email
                                if (BiometricAuth.isAvailable(this)) {
                                    showBiometric = true
                                    currentScreen = "main"
                                } else {
                                    currentScreen = "main"
                                }
                            }
                        )
                    }
                    currentScreen == "main" -> {
                        MainNavGraph(userEmail = userEmail)
                    }
                }
            }
        }
    }
}
