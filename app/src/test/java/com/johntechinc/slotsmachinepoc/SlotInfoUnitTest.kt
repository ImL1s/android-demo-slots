package com.johntechinc.slotsmachinepoc

import org.junit.Assert.assertEquals
import org.junit.Test

class SlotInfoUnitTest {
    @Test
    fun test_resetInterval() {
        val slotInfo = SlotInfo(100L)
        slotInfo.resetInterval()
        assertEquals(SlotInfo.INITIAL_INTERVAL, slotInfo.interval)
    }

    @Test
    fun test_defaultInterval() {
        val slotInfo = SlotInfo()
        assertEquals(SlotInfo.INITIAL_INTERVAL, slotInfo.interval)
    }
}
