package co.pvphub.blossom.proxy

import co.pvphub.blossom.BlossomManager
import co.pvphub.velocity.dsl.event
import co.pvphub.velocity.plugin.VelocityPlugin
import co.pvphub.velocity.util.color
import co.pvphub.velocity.util.colored
import com.google.inject.Inject
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.ConnectionHandshakeEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.ServerPing
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import java.nio.file.Path
import java.util.*
import java.util.logging.Logger

@Plugin(
    id = "blossom",
    name = "Blossom",
    authors = ["xyzaurora"],
    description = "The most advanced security plugin you can imagine.",
    version = "-SNAPSHOT"
)
class BlossomProxy @Inject constructor(
    proxy: ProxyServer,
    logger: Logger,
    @DataDirectory
    dataDirectory: Path
) : VelocityPlugin(proxy, logger, dataDirectory) {

    @Subscribe
    fun start(e: ProxyInitializeEvent) {
        val blossomManager = BlossomManager()
        val cmField = server::class.java.getDeclaredField("cm")
        cmField.isAccessible = true
        val connectionManager = cmField.get(server)
        val channelInitializerHolder = connectionManager::class.java.getDeclaredMethod("getServerChannelInitializer").invoke(connectionManager)

        val origin = channelInitializerHolder::class.java.getDeclaredMethod("get").invoke(channelInitializerHolder) as ChannelInitializer<Channel>

        val newInit = BlossomInitializer(blossomManager, origin)

        logger.info("Replacing channel initializer with blossom initializer")

        channelInitializerHolder::class.java.getMethod("set", ChannelInitializer::class.java).invoke(channelInitializerHolder, newInit)
    }



}