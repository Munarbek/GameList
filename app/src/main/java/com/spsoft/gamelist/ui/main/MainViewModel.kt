package com.spsoft.gamelist.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.spsoft.gamelist.data.models.gamelist.Game
import com.spsoft.gamelist.data.models.gamelist.Result
import com.spsoft.gamelist.ui.main.paging.PagingSourceGameList
import com.spsoft.gamelist.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val main_repository: MainRepository
) : ViewModel() {

    private val _listGames =  MutableLiveData<Game>()
    val gamesList get() = _listGames
    private val _error=MutableLiveData<String>()
    val error get() = _error


    fun getListGames(page: Int, page_size: Int, key: String) = viewModelScope.launch {
         when( val response=  main_repository.getListGames(page, page_size, key)){
             is NetworkResponse.Success->{
                _listGames.value=response.data!!
             }
             is NetworkResponse.Failure->{
                 _error.value=response.message

             }
         }
    }



    fun getListRawg(page: Int, page_size: Int, key: String) = viewModelScope.launch {
        val list=main_repository.getList(page, page_size, key).collect {

        }
    }

    fun getListLink(s: String)= viewModelScope.launch {
        when( val response=  main_repository.getListGamesLink(s)){
            is NetworkResponse.Success->{
                _listGames.value=response.data!!
            }
            is NetworkResponse.Failure->{
                _error.value=response.message
            }
        }
    }

}