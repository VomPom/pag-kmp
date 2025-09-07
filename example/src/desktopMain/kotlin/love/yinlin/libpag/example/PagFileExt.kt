package love.yinlin.libpag.example

import org.libpag.PAGFile
import org.libpag.PAGLayer

/**
 *
 * Created by @juliswang on 2025/08/08 21:15
 *
 * @Description
 */
fun PAGFile.videoTrackIndexList(): List<Int> {
    val imgSize = numImages()
    val trackTag = mutableListOf<Int>()
    for (i in 0..imgSize) {
        val imageLayers = getLayersByEditableIndex(i, PAGLayer.LayerTypeImage)
        if (!imageLayers.isNullOrEmpty()) {
            // 返回是一个vector，取第0个即可。一个PAG里，一个index只对应一个图层
            // 非0的情况只在多个PAGFile组合成一个PAGComposition的时候才会出现，目前不存在
            val imageLayer = imageLayers[0]
            imageLayer?.let {
                val markers = it.markers()
                markers?.forEach { mark ->
                    trackTag.add(it.editableIndex())
                }
            }
        }
    }
    return trackTag
}

fun videoTrackTags(pagFile: PAGFile): String {
    val videoTrack = pagFile.videoTrackIndexList()
    val markList = videoTrack.map { it.toString() }
    return if (markList.isNotEmpty()) {
        markList.toString()
    } else {
        "[]"
    }
}

fun usToS(us: Long): Float {
    return us / 1000_000f
}