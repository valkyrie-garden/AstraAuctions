package com.astrainteractive.astramarket.domain.api

import com.astrainteractive.astramarket.domain.dto.AuctionDTO

interface AuctionsAPI {
    suspend fun insertAuction(auctionDTO: AuctionDTO): Int?
    suspend fun expireAuction(auctionDTO: AuctionDTO): Unit?
    suspend fun getUserAuctions(uuid: String, expired: Boolean): List<AuctionDTO>?
    suspend fun getAuctions(expired: Boolean): List<AuctionDTO>?
    suspend fun getAuctionsOlderThan(millis: Long): List<AuctionDTO>?
    suspend fun fetchAuction(id: Int): AuctionDTO?
    suspend fun deleteAuction(auction: AuctionDTO): Unit?
    suspend fun countPlayerAuctions(uuid: String): Int?
}
