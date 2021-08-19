package com.spsoft.gamelist.ui.main.paging

import androidx.paging.PagingSource
import com.spsoft.gamelist.data.models.gamelist.Result
import com.spsoft.gamelist.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

private const val RAWG_STARTING_PAGE_INDEX = 1

class PagingSourceGameList(private val apiService: ApiService) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        var position = params.key ?: RAWG_STARTING_PAGE_INDEX
        return try {
            val response = apiService.getList(position, 12, "ebe7f17643cd4c5e92176cc953c88700")
            var games = response.results
            LoadResult.Page(
                data = games,
                prevKey = if (position == RAWG_STARTING_PAGE_INDEX) null else position,
                nextKey = if (position == RAWG_STARTING_PAGE_INDEX) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}