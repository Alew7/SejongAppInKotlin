package com.example.sejongapp.Activities.AiActivity.AiViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AIChatViewModelFactory(private val repository: AiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AIChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AIChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}