package com.johntechinc.slotsmachinepoc


/**
 * Created by ImL1s on 23/08/2017.
 * Description:
 */
class SlotInfo(var interval: Long) {


    fun resetInterval() {
        interval = INITIAL_INTERVAL
    }

    companion object {
        val INITIAL_INTERVAL = 5L
    }
}