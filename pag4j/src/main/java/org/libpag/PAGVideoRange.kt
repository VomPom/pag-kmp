package org.libpag

/**
 *
 * Created by @juliswang on 2025/07/22 21:41
 *
 * @Description Represents a time range from the content of PAGImageLayer.
 */
class PAGVideoRange(startTime: Long, endTime: Long, playDuration: Long, reversed: Boolean) {
    /**
     * The start time of the source video, in microseconds.
     */
    var startTime: Long = 0

    /**
     * The end time of the source video (not included), in microseconds.
     */
    var endTime: Long = 0

    /**
     * The duration for playing after applying speed.
     */
    var playDuration: Long = 0

    /**
     * Indicates whether the video should play backward.
     */
    var reversed: Boolean = false

    init {
        this.startTime = startTime
        this.endTime = endTime
        this.playDuration = playDuration
        this.reversed = reversed
    }
}
