package co.pvphub.blossom

import co.pvphub.blossom.bush.BushClient
import java.util.logging.Logger
const val BRANCH = "prd"
const val URL = "https://blossom.pvphub.co/$BRANCH/"
val logger = Logger.getLogger("Blossom")
class BlossomManager(
    // token: String
) {
    val bush = BushClient()
    // val malscan = BushClient(token) malscan is disabled until further discussion
}