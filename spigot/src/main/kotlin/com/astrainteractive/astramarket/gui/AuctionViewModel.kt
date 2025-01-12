package com.astrainteractive.astramarket.gui

import com.astrainteractive.astramarket.domain.api.AuctionsAPI
import com.astrainteractive.astramarket.domain.dto.AuctionDTO
import com.astrainteractive.astramarket.api.*
import com.astrainteractive.astramarket.api.use_cases.AuctionBuyUseCase
import com.astrainteractive.astramarket.api.use_cases.ExpireAuctionUseCase
import com.astrainteractive.astramarket.api.use_cases.RemoveAuctionUseCase
import com.astrainteractive.astramarket.utils.sortBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import ru.astrainteractive.astralibs.async.AsyncComponent
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.utils.next
import ru.astrainteractive.astralibs.utils.prev
import ru.astrainteractive.astralibs.utils.uuid

class AuctionViewModel(
    private val player: Player,
    private val expired: Boolean = false,
    private val dataSource: AuctionsAPI
) : AsyncComponent() {
    private val _auctionList = MutableStateFlow(listOf<AuctionDTO>())
    val auctionList: StateFlow<List<AuctionDTO>>
        get() = _auctionList
    val maxItemsAmount: Int
        get() = _auctionList.value.size

    var sortType = AuctionSort.DATE_ASC
    private val auctionBuyUseCase = AuctionBuyUseCase()
    private val expireAuctionUseCase = ExpireAuctionUseCase()
    private val removeAuctionUseCase = RemoveAuctionUseCase()
    fun onSortButtonClicked(isRightClick: Boolean) {
        sortType = if (isRightClick)
            sortType.next()
        else
            sortType.prev()
        sort()
    }

    private fun sort() {
        PluginScope.launch(Dispatchers.IO) {
            _auctionList.update {
                auctionList.value.sortBy(sortType)
            }
        }
    }

    private suspend fun onExpiredAuctionClicked(auction: AuctionDTO): Boolean {
        return removeAuctionUseCase(RemoveAuctionUseCase.Params(auction, player))
    }

    private suspend fun onAuctionClicked(auction: AuctionDTO, clickType: ClickType): Boolean {
        return when (clickType) {
            ClickType.LEFT -> auctionBuyUseCase(AuctionBuyUseCase.Params(auction, player))
            ClickType.RIGHT -> removeAuctionUseCase(RemoveAuctionUseCase.Params(auction, player))
            ClickType.MIDDLE -> expireAuctionUseCase(ExpireAuctionUseCase.Params(auction, player))
            else -> return false
        }
    }

    suspend fun onAuctionItemClicked(i: Int, clickType: ClickType): Boolean {
        val auction = _auctionList.value.getOrNull(i) ?: return false
        val result = if (expired) onExpiredAuctionClicked(auction)
        else onAuctionClicked(auction, clickType)
        if (result)
            loadItems()
        return result
    }

    fun loadItems() =
        PluginScope.launch(Dispatchers.IO) {
            val list =
                if (!expired) dataSource.getAuctions(expired) else dataSource.getUserAuctions(player.uuid, expired)
            val sorted = list?.sortBy(sortType) ?: emptyList()
            _auctionList.update {
                sorted
            }
        }

    init {
        loadItems()
    }
}