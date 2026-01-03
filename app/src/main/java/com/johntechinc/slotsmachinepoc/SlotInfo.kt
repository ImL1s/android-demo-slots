package com.johntechinc.slotsmachinepoc

/**
 * Created by ImL1s on 23/08/2017.
 * Description: Data class for holding slot machine timing information.
 */
data class SlotInfo(
    var interval: Long = INITIAL_INTERVAL
) {
    fun resetInterval() {
        interval = INITIAL_INTERVAL
    }

    companion object {
        const val INITIAL_INTERVAL = 5L
    }
}