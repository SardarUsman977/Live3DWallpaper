package cam.trim.live.wallpaper3d

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun setWalpaper(view: View) {
        try {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(this, WallpaperService::class.java))
            startActivityForResult(intent, 1200)
        } catch (e: Exception) {
            Log.d("TAG", "setWalpaper: ${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1200){
            if (resultCode== RESULT_OK){
                Toast.makeText(this, "set Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}