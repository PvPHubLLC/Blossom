package co.pvphub.blossom.backend

import co.pvphub.blossom.BlossomManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.channel.SimpleChannelInboundHandler
import io.papermc.paper.network.ChannelInitializeListenerHolder.addListener
import net.kyori.adventure.key.Key
import org.bukkit.plugin.java.JavaPlugin
import java.net.InetSocketAddress
import java.net.SocketAddress

class BlossomBackend : JavaPlugin() {

    override fun onEnable() {
        // saveDefaultConfig()
        val manager = BlossomManager(
            // disabled until further discussionconfig.getString("token")!!
        )
        addListener(Key.key("blossom", "guard")) {
            it.pipeline().addFirst("blossom_guard", object : SimpleChannelInboundHandler<Any>() {
                var isBlacklisted = false

                override fun channelActive(ctx: ChannelHandlerContext) {
                    super.channelActive(ctx)
                    val remoteAddress = ctx.channel().remoteAddress()
                    if(remoteAddress is InetSocketAddress) {
                        println(manager.bush.isIpBlacklisted(remoteAddress.address.hostAddress))
                        if(manager.bush.isIpBlacklisted(remoteAddress.address.hostAddress)) {
                            isBlacklisted = true
                            ctx.disconnect()
                        } else {
                            ctx.pipeline().remove("blossom_guard")
                        }
                    }
                }

                override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any?) {
                    if(!isBlacklisted) super.channelRead(ctx, msg)
                }

            })
        }
    }

}