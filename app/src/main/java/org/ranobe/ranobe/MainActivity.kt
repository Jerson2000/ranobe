package org.ranobe.ranobe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ranobe.ranobe.theme.AppTheme
import org.ranobe.ranobe.util.EventStates
import kotlin.getValue


class MainActivity : ComponentActivity() {
    private val viewModel: ViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getNovels()
        getNovel()
    }

    fun getNovel(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.novels.collect{
                    when(it){
                        is EventStates.Success->{
                            println("ASDASDASD")
                        }
                        is EventStates.Error->{

                        }
                        else -> Unit
                    }

                }
            }
        }
    }
}

