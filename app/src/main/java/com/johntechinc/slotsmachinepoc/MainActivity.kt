package com.johntechinc.slotsmachinepoc

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.johntechinc.slotsmachinepoc.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.Arrays

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isStart = false

    private val interval = 100L
    private val onceIncrease = 6L
    private val maxIncrease = 850L

    private var picker1Index = 0
    private var picker2Index = 0
    private var picker3Index = 0

    private val slot1 = SlotInfo()
    private val slot2 = SlotInfo()
    private val slot3 = SlotInfo()
    private val slots = listOf(slot1, slot2, slot3)

    private var slotStopIndex = 0
    private var stopCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPickers()
    }

    private fun initPickers() {
        binding.btnStart.setOnClickListener { startSlots() }
        binding.btnStop.setOnClickListener { stopSlots() }

        setupPicker(binding.wp1, binding.wp2, binding.wp3)
        setPickerData(binding.wp1, binding.wp2, binding.wp3)
    }

    private fun setupPicker(vararg pickers: com.aigestudio.wheelpicker.WheelPicker) {
        pickers.forEach { picker ->
            picker.isCurved = true
            picker.isCyclic = true
            picker.setAtmospheric(true)
        }
    }

    private fun setPickerData(vararg pickers: com.aigestudio.wheelpicker.WheelPicker) {
        val data = (0..9).toList()
        pickers.forEach { picker ->
            picker.data = data
        }
    }

    private fun startSlots() {
        if (isStart) return
        isStart = true
        stopCount = 0
        slotStopIndex = 0

        playSound("coin_drop_1.mp3")

        lifecycleScope.launch {
            val effectPlayer = createMediaPlayer("slots_effect.mp3")?.apply {
                isLooping = true
                start()
            }

            // Start each wheel animation in its own coroutine
            launch { animateSlot(1) }
            launch { animateSlot(2) }
            launch { animateSlot(3) }

            // Wait until all slots stopped
            while (isStart) {
                delay(100)
            }

            effectPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        }
    }

    private suspend fun animateSlot(slotNumber: Int) {
        val picker = when (slotNumber) {
            1 -> binding.wp1
            2 -> binding.wp2
            else -> binding.wp3
        }
        val slotInfo = when (slotNumber) {
            1 -> slot1
            2 -> slot2
            else -> slot3
        }
        var index = when (slotNumber) {
            1 -> picker1Index
            2 -> picker2Index
            else -> picker3Index
        }

        while (isStart && slotInfo.interval <= maxIncrease) {
            delay(slotInfo.interval)
            index = (index + 1) % picker.data.size
            picker.selectedItemPosition = index
            
            // Sync index back
            when (slotNumber) {
                1 -> picker1Index = index
                2 -> picker2Index = index
                else -> picker3Index = index
            }
        }
        slotInfo.resetInterval()
    }

    private fun stopSlots() {
        if (!isStart) return
        stopSingleSlot(slots[0])
    }

    private fun stopSingleSlot(slot: SlotInfo) {
        lifecycleScope.launch {
            while (slot.interval <= maxIncrease) {
                delay(interval)
                when {
                    slot.interval <= maxIncrease * 0.5 -> slot.interval += onceIncrease * 4
                    slot.interval <= maxIncrease * 0.75 -> slot.interval += onceIncrease * 16
                    else -> {
                        slot.interval += onceIncrease * 64
                        if (slot.interval > maxIncrease) {
                            // Trigger next slot stop
                            if (++slotStopIndex < slots.size) {
                                stopSingleSlot(slots[slotStopIndex])
                            }
                        }
                    }
                }
            }
            stopCount++
            if (stopCount >= slots.size) {
                isStart = false
            }
        }
    }

    private fun playSound(fileName: String) {
        try {
            val afd = assets.openFd(fileName)
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
                setOnCompletionListener { 
                    it.release() 
                    afd.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createMediaPlayer(fileName: String): MediaPlayer? {
        return try {
            val afd = assets.openFd(fileName)
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
