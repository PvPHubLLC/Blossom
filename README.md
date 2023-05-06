# Blossom

Blossom is a security mod for scanners and soon malware scanning.
## FAQ

### X IP did Y on my server, can you blacklist it?

**No**, this only is for mass scanners and other people. The process of blacklisting IPs is automated and no manual additions will be made unless there's a extreme case.

### What do blacklisted IPs see?

**Simple:** It looks like the minecraft server isnt even on. Blossom will cancel the connection to the port immediately.

### Why?

Recently, many scanners have been spam joining servers with usernames, spamming peoples console.
The IPs often change, so we have honeypots all over the world detecting when they ping, and add them to a blacklist. 

### Is there chances of false positives?

**No.** The IPs of the sensors are not shared and cannot be easily fingerprinted. No legitimate user will be blocked
