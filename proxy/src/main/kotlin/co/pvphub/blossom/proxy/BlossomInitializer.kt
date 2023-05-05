package co.pvphub.blossom.proxy

import co.pvphub.blossom.BlossomManager
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import java.lang.reflect.Method
import java.net.InetSocketAddress

class BlossomInitializer<C : Channel>(
    private val manager: BlossomManager,
    private val originalChannel: ChannelInitializer<C>,
): ChannelInitializer<C>() {


    companion object {
        val initChannelMethod: Method = run {
            val method = ChannelInitializer::class.java.getDeclaredMethod("initChannel", Channel::class.java)
            method.isAccessible = true
            method
        }
    }

    override fun initChannel(ch: C) {
        val remoteAddress = ch.remoteAddress()
        // We should check just incase something is going on...
        if(remoteAddress is InetSocketAddress) {
            if(manager.bush.isIpBlacklisted(remoteAddress.address.hostAddress)) {
                // Disconnect immediately.
                ch.disconnect()
                return
            }
        }
        initChannelMethod.invoke(originalChannel, ch)
    }
}