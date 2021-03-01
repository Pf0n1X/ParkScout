package com.example.parkscout.ViewModel

import android.content.ClipData
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchLoctionViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String> get() = mutableSelectedItem

    fun selectItem(item: String) {
        mutableSelectedItem.value = item
    }
}