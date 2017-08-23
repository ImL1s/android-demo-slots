package com.johntechinc.slotsmachinepoc

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.aigestudio.wheelpicker.WheelPicker
import java.util.*

class MainActivity : AppCompatActivity() {

    var picker1: WheelPicker? = null
    var picker2: WheelPicker? = null
    var picker3: WheelPicker? = null

    var isStart = false

    val interval = 100L
    val onceIncrease = 6L
    val maxIncrease = 850L
    var picker1Index = 0
    var picker2Index = 0
    var picker3Index = 0
    var slot1 = SlotInfo(5L)
    var slot2 = SlotInfo(5L)
    var slot3 = SlotInfo(5L)
    var slotArray = arrayListOf(slot1, slot2, slot3)
    var slotArrayIndicator = 0
    var stopCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPicker()
    }

    private fun initPicker() {
        picker1 = findViewById<WheelPicker>(R.id.wp_1)
        picker2 = findViewById<WheelPicker>(R.id.wp_2)
        picker3 = findViewById<WheelPicker>(R.id.wp_3)
        findViewById<Button>(R.id.btn_start)
                .setOnClickListener { startSlots() }
        findViewById<Button>(R.id.btn_stop)
                .setOnClickListener { stopSlots() }

        setCurtain(picker1, picker2, picker3)
        setData(picker1, picker2, picker3)

    }


    fun setCurtain(vararg p: WheelPicker?) {
        p.forEach { x ->
            x?.isCurved = true
            x?.isCyclic = true
            x?.setAtmospheric(true)
        }
    }

    fun setData(vararg p: WheelPicker?) {
        p.forEach { x ->
            x!!.data = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

        }
    }

    fun startSlots() {
        val mediaPlayer: MediaPlayer = MediaPlayer()
        val desc = assets.openFd("coin_drop_1.mp3")
        mediaPlayer.reset()
        mediaPlayer.setDataSource(desc.fileDescriptor, desc.startOffset, desc.length)
        mediaPlayer.prepare()
        mediaPlayer.start()


        picker1Index %= 5

        Thread {
            val mediaPlayerEffect = MediaPlayer()
            val desc = assets.openFd("slots_effect.mp3")
            mediaPlayerEffect.reset()
            mediaPlayerEffect.setDataSource(desc.fileDescriptor, desc.startOffset, desc.length)
            mediaPlayerEffect.prepare()

            isStart = true


            Thread {
                while (isStart) {
                    Thread.sleep(slot1.interval)
//                    if (isStart) mediaPlayerEffect.start()
                    picker1Index %= picker1!!.data.size
                    if (slot1.interval < maxIncrease) {
                        runOnUiThread {
                            picker1?.selectedItemPosition = ++picker1Index
                        }
                    }
                    if (slot1.interval > maxIncrease) {
                        break
                    }
                }
                slot1.resetInterval()
            }.start()



            Thread {
                while (isStart) {
                    Thread.sleep(slot2.interval)
//                    if (isStart) mediaPlayerEffect.start()
                    picker2Index %= picker2!!.data.size
                    if (slot2.interval < maxIncrease) {
                        runOnUiThread {
                            picker2?.selectedItemPosition = ++picker2Index
                        }
                    }
                    if (slot2.interval > maxIncrease) {
                        break
                    }
                }
                slot2.resetInterval()
            }.start()

            Thread {
                while (isStart) {
                    Thread.sleep(slot3.interval)
//                    if (isStart) mediaPlayerEffect.start()
                    picker3Index %= picker3!!.data.size
                    if (slot3.interval < maxIncrease) {
                        runOnUiThread {
                            picker3?.selectedItemPosition = ++picker3Index
                        }
                    }
                    if (slot3.interval > maxIncrease) {
                        break
                    }
                }
                slot3.resetInterval()
            }.start()

            mediaPlayerEffect.stop()
            mediaPlayerEffect.release()
        }.start()

    }

    private fun stopSlots() {
        stopCount = 0
        slotArrayIndicator = 0
        stopSingleSlot(slotArray[0])
//        stopSingleSlot(slotArray[1])
//        stopSingleSlot(slotArray[2])
    }

    private fun stopSingleSlot(currentSlot: SlotInfo, delay: Long = 0) {
        Thread {
            var isLaunchNextSlotStop = false
            if (delay > 0) Thread.sleep(delay)
            while (currentSlot.interval <= maxIncrease) {
                Thread.sleep(interval)
                if (currentSlot.interval <= maxIncrease * 0.5) {
                    currentSlot.interval += onceIncrease * 4
                } else if (currentSlot.interval <= maxIncrease * 0.75) {
                    currentSlot.interval += onceIncrease * 16

                } else {
                    currentSlot.interval += onceIncrease * 64
                    if (!isLaunchNextSlotStop) {
                        isLaunchNextSlotStop = true
                        if (++slotArrayIndicator < slotArray.size) {
                            stopSingleSlot(slotArray[slotArrayIndicator])
                        }
                    }
                }
            }
            stopCount++

            if (stopCount >= 3) {
                isStart = false
            }
        }.start()
    }
}





