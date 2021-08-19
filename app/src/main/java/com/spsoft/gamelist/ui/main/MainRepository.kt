package com.spsoft.gamelist.ui.main

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.spsoft.gamelist.data.models.gamelist.Game
import com.spsoft.gamelist.data.models.gamelist.Result
import com.spsoft.gamelist.data.remote.ApiService
import com.spsoft.gamelist.ui.main.paging.PagingSourceGameList
import com.spsoft.gamelist.utils.BaseRepository
import kotlinx.coroutines.flow.Flow

class MainRepository(private val api: ApiService) : BaseRepository() {
    suspend fun getListGames(page: Int, page_size: Int, key: String) = saveApiCall {
        api.getList(page, page_size, key)
    }
    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
    fun getList(page: Int, page_size: Int, key: String): Flow<PagingData<Result>> {
        return Pager(config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = true
        ), pagingSourceFactory = { PagingSourceGameList(api) }).flow
    }

    suspend  fun getListGamesLink(s: String)= saveApiCall {
        api.getlistLink(s)
    }
}


