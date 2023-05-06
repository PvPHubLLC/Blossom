package co.pvphub.blossom.backend

import co.pvphub.blossom.BlossomManager
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.bukkit.plugin.java.JavaPlugin
import io.papermc.paper.network.ChannelInitializeListenerHolder.addListener
import net.kyori.adventure.key.Key
import net.minecraft.network.protocol.Packet
import java.net.InetSocketAddress

class BlossomBackend : JavaPlugin() {

    override fun onEnable() {
        // saveDefaultConfig()
        val manager = BlossomManager(
            // disabled until further discussionconfig.getString("token")!!
        )
        addListener(Key.key("blossom", "guard")) {
            it.pipeline().addAfter("packet_handler", "blossom_guard", object : MessageToByteEncoder<Packet<*>>() {
                override fun encode(ctx: ChannelHandlerContext, msg: Packet<*>?, out: ByteBuf?) {
                    val remoteAddress = ctx.channel().remoteAddress()
                    if(remoteAddress is InetSocketAddress) {
                        if(manager.bush.isIpBlacklisted(remoteAddress.address.hostAddress)) {
                            it.disconnect()
                        }
                    }
                }


            })
        }
    }

}