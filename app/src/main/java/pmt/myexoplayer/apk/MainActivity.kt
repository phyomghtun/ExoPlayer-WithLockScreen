package pmt.myexoplayer.apk

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.VolumeShaper.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Video
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_controller.*

class MainActivity : AppCompatActivity() {

    val url = "https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4"
    var isFullScreen = false
    var isLock = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_fullscreen.setOnClickListener {

            requestedOrientation = if(!isFullScreen){
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen_exit))
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }else{
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen))
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isFullScreen = !isFullScreen
        }

        btn_lock.setOnClickListener {
            if(!isLock){
                btn_lock.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock))
            }else{
                btn_lock.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock_open))
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        val simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerView.player = simpleExoPlayer
        playerView.keepScreenOn = true
        simpleExoPlayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_BUFFERING){
                    progress_bar.visibility = View.VISIBLE
                }else if(playbackState == Player.STATE_READY){
                    progress_bar.visibility = View.GONE
                }
            }
        })

        val videoSource = Uri.parse(url)
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()

    }

    private fun lockScreen(lock: Boolean) {
        if(lock){
            sec_controlvid1.visibility = View.INVISIBLE
            sec_controlvid2.visibility = View.INVISIBLE
        }else{
            sec_controlvid1.visibility = View.VISIBLE
            sec_controlvid2.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if(isLock) return
        if(isFullScreen){
            bt_fullscreen.performClick()
        }
        else super.onBackPressed()
    }
}