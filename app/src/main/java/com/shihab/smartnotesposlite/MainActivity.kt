package com.shihab.smartnotesposlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shihab.smartnotesposlite.data.local.AppDatabase
import com.shihab.smartnotesposlite.data.repository.ProductRepository
import com.shihab.smartnotesposlite.ui.screen.possell.PosScreen
import com.shihab.smartnotesposlite.ui.theme.SmartNotesPOSLiteTheme
import com.shihab.smartnotesposlite.ui.screen.possell.PosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ProductRepository(database.productDao())
        val factory = PosViewModel.PosViewModelFactory(repository)

        setContent {
            SmartNotesPOSLiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val viewModel: PosViewModel = viewModel(factory = factory)
                    PosScreen(viewModel)
                }
            }
        }
    }
}
