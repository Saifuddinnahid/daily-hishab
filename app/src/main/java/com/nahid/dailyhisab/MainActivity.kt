package com.nahid.dailyhisab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nahid.dailyhisab.data.repository.AuthRepository
import com.nahid.dailyhisab.ui.navigation.MainNavGraph
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
                var ready by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    authRepository.ensureDefaultUser()
                    ready = true
                }

                if (ready) {
                    MainNavGraph(userEmail = authRepository.getDefaultUserEmail())
                }
            }
        }
    }
}
