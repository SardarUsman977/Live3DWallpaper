package cam.trim.live.wallpaper3d

import android.content.Context
import android.graphics.Canvas
import android.graphics.ImageDecoder
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.SurfaceHolder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.lang.RuntimeException

internal class UseAnim @RequiresApi(api = Build.VERSION_CODES.P) constructor(
    ctx: Context,
    var holder: SurfaceHolder,
    gifResId: Int
) {
    var startRunnable: Runnable
    var gif: AnimatedImageDrawable? = null
    var fps = 60f
    var handler = Handler(Looper.getMainLooper())
    fun restart() {
        stop()
        start()
    }

    var scale = -1f
    fun start() {
        // since get gif with AnimatedImageDrawable must be in handler.post, so gif maybe null
        if (gif != null) {
            var canvas: Canvas? = null
            try {
                if (scale == -1f) {
                    updateScaleAndPadding()
                }
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    canvas.translate(horiPadding.toFloat(), vertiPadding.toFloat())
                    canvas.scale(scale, scale)
                    gif!!.draw(canvas)
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
        handler.removeCallbacks(startRunnable)
        handler.postDelayed(startRunnable, (1000L / fps).toLong())
    }

    fun stop() {
        handler.removeCallbacks(startRunnable)
    }

    var horiPadding = 0
    var vertiPadding = 0
    private fun updateScaleAndPadding() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            val cw = canvas.width
            val ch = canvas.height
            updateScaleAndPadding2(cw, ch)
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    fun updateScaleAndPadding2(cw: Int, ch: Int) {
        if (gif != null) {
            val gifW = gif!!.intrinsicWidth
            val gifH = gif!!.intrinsicHeight
            scale = if (gifW * 1f / gifH > cw * 1f / ch) {
                ch * 1f / gifH
            } else {
                cw * 1f / gifW
            }
            horiPadding = ((cw - gifW * scale) / 2).toInt()
            vertiPadding = ((ch - gifH * scale) / 2).toInt()
        }
    }

    init {
        val src = ImageDecoder.createSource(ctx.resources, gifResId)
        startRunnable = Runnable { start() }
        Handler().post {
            try {
                gif = ImageDecoder.decodeDrawable(src) as AnimatedImageDrawable
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            gif!!.start()
        }
    }
}