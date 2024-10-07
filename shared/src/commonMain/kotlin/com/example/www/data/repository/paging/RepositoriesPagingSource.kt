package com.example.www.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.www.data.util.Result
import com.example.www.domain.model.Repository
import io.ktor.http.Headers

class RepositoriesPagingSource(
    private val fetch: suspend (page: Int, pageSize: Int) -> Result<Pair<List<Repository>, Headers>>,
) :
    PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return (params.key ?: 1).let { _page ->
            try {
                fetch(_page, params.loadSize)
                    .run {
                        when (this) {
                            is Result.Success -> {
                                val response: Pair<List<Repository>, Headers> = this.value
                                val linkHeader = response.second["Link"]
                                var prevPage: Int? = null
                                var nextPage: Int? = null
                                if (linkHeader != null) {
                                    prevPage = extractPageFromLinkHeader(linkHeader, "prev")
                                    nextPage = extractPageFromLinkHeader(linkHeader, "next")
                                }
                                LoadResult.Page(
                                    data = response.first,
                                    prevKey = prevPage,
                                    nextKey = nextPage
                                )
                            }

                            /* error state */
                            is Error -> LoadResult.Error(this)

                            /* should not reach this point */
                            else -> LoadResult.Error(IllegalStateException("$this type of [Result] is not allowed here"))
                        }
                    }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    private fun extractPageFromLinkHeader(linkHeader: String, relParameter: String): Int? {
        val links = linkHeader.split(",")
        val nextLink = links.find { it.contains("; rel=\"$relParameter\"") }
        val pattern = "page=(\\d+)".toRegex()
        val matchResult = pattern.find(nextLink.toString())
        val pageNumber = matchResult?.groupValues?.get(1)?.toInt()
        return pageNumber
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition
    }
}