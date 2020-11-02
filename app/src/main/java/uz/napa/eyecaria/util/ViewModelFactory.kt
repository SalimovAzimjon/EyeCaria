package uz.napa.eyecaria.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.napa.eyecaria.repository.Repository
import uz.napa.eyecaria.ui.fragments.CameraViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CameraViewModel::class.java) -> CameraViewModel(repository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}