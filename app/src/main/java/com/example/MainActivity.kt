package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CombatRepository
import com.example.data.ShatterpointDatabase
import com.example.ui.MainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ShatterpointViewModel
import com.example.viewmodel.ShatterpointViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Database & Repository
        val database = ShatterpointDatabase.getDatabase(applicationContext)
        val repository = CombatRepository(database.dao())
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val vm: ShatterpointViewModel = viewModel(
                        factory = ShatterpointViewModelFactory(repository)
                    )
                    MainScreen(viewModel = vm)
                }
            }
        }
    }
}
