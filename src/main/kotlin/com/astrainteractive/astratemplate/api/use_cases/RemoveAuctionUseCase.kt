package com.astrainteractive.astratemplate.api.use_cases

import com.astrainteractive.astramarket.domain.dto.AuctionDTO
import com.astrainteractive.astratemplate.AstraMarket
import com.astrainteractive.astratemplate.modules.ConfigModule
import com.astrainteractive.astratemplate.modules.DataSourceModule
import com.astrainteractive.astratemplate.modules.TranslationModule
import com.astrainteractive.astratemplate.utils.itemStack
import com.astrainteractive.astratemplate.utils.playSound
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.domain.IUseCase
import java.util.*

/**
 * @param _auction auction to remove
 * @param player owner of auction
 * @return boolean - true if succesfully removed
 */
class RemoveAuctionUseCase : IUseCase<Boolean, RemoveAuctionUseCase.Params> {
    private val dataSource by DataSourceModule
    private val translation by TranslationModule
    private val config by ConfigModule
    class Params(
        val auction: AuctionDTO,
        val player: Player
    ) {
        operator fun component1() = auction
        operator fun component2() = player
    }

    override suspend fun run(params: Params): Boolean {
        val (_auction, player) = params

        val auction = dataSource.fetchAuction(_auction.id) ?: return false
        val owner = Bukkit.getOfflinePlayer(UUID.fromString(auction.minecraftUuid))
        if (owner.uniqueId != player.uniqueId) {
            player.sendMessage(translation.notAuctionOwner)
            return false
        }
        val item = auction.itemStack
        if (player.inventory.firstEmpty() == -1) {
            player.playSound(config.sounds.fail)
            player.sendMessage(translation.inventoryFull)
            return false
        }
        val result = dataSource.deleteAuction(auction)
        return if (result != null) {
            player.sendMessage(translation.auctionDeleted)
            player.inventory.addItem(item)
            true
        } else {
            player.sendMessage(translation.unexpectedError)
            false
        }
    }
}