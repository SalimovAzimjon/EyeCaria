package uz.napa.eyecaria.ui.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.video_controller.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Training
import uz.napa.eyecaria.ui.activity.listeners.PlayerListener
import uz.napa.eyecaria.ui.activity.listeners.SeekBarListener
import uz.napa.eyecaria.util.isVisible
import java.text.SimpleDateFormat
import java.util.*


class VideoActivity : AppCompatActivity() {
    private lateinit var exoPlayer: SimpleExoPlayer
    private val mHandler = Handler()
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            val bufferedPos = exoPlayer.bufferedPosition
            video_progress.secondaryProgress = bufferedPos.toInt()
            val currentPos = exoPlayer.currentPosition
            video_progress.progress = currentPos.toInt()
            tv_time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPos)
            mHandler.postDelayed(this, 1_000)
        }
    }
    private var isPortrait = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        detectOrientation()
        setUpPlayer()
        setUpSeekBar()
        handleClick()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (val orientation = newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> btn_full_screen.setImageResource(R.drawable.exo_controls_fullscreen_enter)

            Configuration.ORIENTATION_LANDSCAPE -> btn_full_screen.setImageResource(R.drawable.exo_controls_fullscreen_exit)

            else -> Log.d("tag", "other: $orientation")
        }
    }

    private fun detectOrientation() {
        Log.d(
            "ssass",
            "${resources.configuration.orientation} - ${Configuration.ORIENTATION_PORTRAIT}"
        )
        isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        if (isPortrait)
            btn_full_screen.setImageResource(R.drawable.exo_controls_fullscreen_enter)
        else
            btn_full_screen.setImageResource(R.drawable.exo_controls_fullscreen_exit)

    }

    private fun handleClick() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_full_screen.setOnClickListener {
            isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            if (isPortrait) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                mHandler.postDelayed({
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }, 3000)
                btn_full_screen.setImageResource(R.drawable.exo_controls_fullscreen_exit)
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                mHandler.postDelayed({
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }, 3000)
                btn_full_screen.setImageResource(R.drawable.exo_controls_fullscreen_enter)
            }
        }

        btn_play.setOnClickListener {
            if (exoPlayer.isPlaying) {
                btn_play.setImageResource(R.drawable.pause_to_play)
                val draw = btn_play.drawable
                if (draw is AnimatedVectorDrawable) {
                    draw.start()
                    exoPlayer.playWhenReady = false
                } else if (draw is AnimatedVectorDrawableCompat) {
                    draw.start()
                    exoPlayer.playWhenReady = false
                }
            } else {
                btn_play.setImageResource(R.drawable.play_to_pause)
                val draw = btn_play.drawable
                if (draw is AnimatedVectorDrawable) {
                    draw.start()
                    exoPlayer.playWhenReady = true
                } else if (draw is AnimatedVectorDrawableCompat) {
                    draw.start()
                    exoPlayer.playWhenReady = true
                }
            }
        }

        btn_frw.setOnClickListener {
            val rotate = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotate.apply {
                duration = 300
                interpolator = LinearInterpolator()
            }
            btn_frw.startAnimation(rotate)
            exoPlayer.seekTo(exoPlayer.currentPosition + 10_000)
        }
        btn_rewind.setOnClickListener {
            val rotate = RotateAnimation(
                0f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotate.apply {
                duration = 300
                interpolator = LinearInterpolator()
            }
            btn_rewind.startAnimation(rotate)
            exoPlayer.seekTo(exoPlayer.currentPosition - 10_000)
        }
    }

    private fun setUpSeekBar() {
        video_progress.setOnSeekBarChangeListener(object : SeekBarListener() {
            var userSelectedPosition = 0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    userSelectedPosition = progress
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                exoPlayer.seekTo(userSelectedPosition.toLong())
            }
        })
    }

    private fun setUpPlayer() {
        val training = intent.getParcelableExtra<Training>("data")
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "EyeCaria")
        )
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(training!!.videoUrl))
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        exoPlayer.apply {
            prepare(videoSource)
            playWhenReady = true
        }
        player_view.apply {
            player = exoPlayer
            keepScreenOn = true
        }
        exoPlayer.addListener(object :
            PlayerListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_BUFFERING)
                    progress_bar.isVisible(true)
                else if (playbackState == Player.STATE_READY) {
                    progress_bar.isVisible(false)
                    updateSeekBar()
                }

            }
        })
    }

    private fun updateSeekBar() {
        video_progress.max = exoPlayer.duration.toInt()
        tv_duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(exoPlayer.duration)
        runOnUiThread(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
        mHandler.removeCallbacks(runnable)
    }
}