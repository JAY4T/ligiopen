package com.jabulani.ligiopen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.jabulani.ligiopen.ui.nav.NavigationGraph
import com.jabulani.ligiopen.ui.theme.LigiopenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = viewModel(factory = AppViewModelFactory.Factory)
            val uiState by viewModel.uiState.collectAsState()
            LigiopenTheme(
                darkTheme = uiState.userAccount.darkMode
            ) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    NavigationGraph(
                        navController = rememberNavController(),
                        onSwitchTheme = {
                            viewModel.switchTheme()
                        }
                    )
                }
            }
        }
    }
}
