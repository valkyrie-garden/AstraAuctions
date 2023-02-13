package com.astrainteractive.astramarket.events

import com.astrainteractive.astramarket.domain.dto.AuctionDTO
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RemoveAuctionEvent(auction: AuctionDTO?) : Event() {

    private var auction: AuctionDTO? = null

    init {
        this.auction = auction
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList = HandlerList()
    }

    fun getAuction() : AuctionDTO? {
        return this.auction
    }
}