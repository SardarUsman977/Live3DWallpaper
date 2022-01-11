package cam.trim.live.wallpaper3d

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import java.io.IOException

import android.os.Build
class WallpaperService : WallpaperService() {
    private lateinit var useAnim : UseAnim

    override fun onCreate() {
        super.onCreate()
    }

    override fun onCreateEngine(): Engine? {
        return try {
            WallpaperEngine()
        } catch (e: IOException) {
            stopSelf()
            null
        }
    }

    inner class WallpaperEngine() : Engine(){
        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                useAnim = UseAnim(applicationContext, getSurfaceHolder(), R.raw.usman)
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                useAnim.restart()
            } else {
                useAnim.stop()
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            useAnim.updateScaleAndPadding2(width, height)
            useAnim.restart()
        }


        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xOffsetStep: Float,
            yOffsetStep: Float,
            xPixelOffset: Int,
            yPixelOffset: Int
        ) {
            super.onOffsetsChanged(
                xOffset,
                yOffset,
                xOffsetStep,
                yOffsetStep,
                xPixelOffset,
                yPixelOffset
            )
            useAnim.restart()
        }

        override fun onDestroy() {
            super.onDestroy()
            useAnim.stop()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}