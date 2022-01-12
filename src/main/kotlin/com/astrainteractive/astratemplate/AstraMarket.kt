package com.astrainteractive.astratemplate

//import com.makeevrserg.empiretemplate.database.EmpireDatabase
import CommandManager
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astratemplate.api.AuctionAPI
import com.astrainteractive.astratemplate.events.EventHandler
import com.astrainteractive.astratemplate.sqldatabase.Database
import com.astrainteractive.astratemplate.utils.*
import com.astrainteractive.astratemplate.utils.config.AuctionConfig
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

/**
 * Initial class for your plugin
 */
class AstraMarket : JavaPlugin() {
    final val TAG = "AstraMarket"

    /**
     * Static objects of this class
     * @see Translation
     */
    companion object {
        lateinit var instance: AstraMarket
            private set
        lateinit var translations: Translation
            private set
        lateinit var empireFiles: Files
            private set
        lateinit var pluginConfig: AuctionConfig
            private set
        public lateinit var database: Database
            private set
    }


    private lateinit var eventHandler: EventHandler

    private lateinit var commandManager: CommandManager


    override fun onEnable() {
        AstraLibs.create(this)
        Logger.init("AstraAuctions")
        instance = this
        translations = Translation()
        empireFiles = Files()
        eventHandler = EventHandler()
        commandManager = CommandManager()
        pluginConfig = AuctionConfig.load()
        database = Database().apply { onEnable() }
        VaultHook()
        Logger.log("Plugin enabled", TAG)
        if (ServerVersion.getServerVersion() == ServerVersion.UNMAINTAINED)
            Logger.warn("Your server version is not maintained and might be not fully functional!", TAG)
        else
            Logger.log("Your server version is: ${ServerVersion.version}. This version is supported!", TAG)
        if (ServerType.getServerType() == ServerType.UNMAINTAINED)
            Logger.warn(
                "Your server type is not PaperMC means it's not maintained and might be not fully functional!",
                TAG
            )
        else
            Logger.log("You are using PaperMC", TAG)
        AuctionAPI.startAuctionChecker()
    }

    override fun onDisable() {
        AuctionAPI.stopAuctionChecker()
        eventHandler.onDisable()
        database.onDisable()
        HandlerList.unregisterAll(this)
        Logger.log("Plugin disabled", TAG)
    }

    fun reloadPlugin() {
        onDisable()
        onEnable()
    }

}

