package com.konstantinmuzhik.audiorecorder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity(), Timer.OnTimerTickListener {

    private var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private lateinit var recorder: MediaRecorder
    private var dirPath = ""
    private var fileName = ""
    private var isRecording = false
    private var isPaused = false

    private var duration = ""

    private lateinit var timer: Timer

    private lateinit var db: AppDatabase

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if(!permissionGranted)
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
        "audioRecords"
        ).build()

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        timer = Timer(this)

        btnRecord.setOnClickListener {
            when {
                isPaused -> resumeRecording()
                isRecording -> pauseRecording()
                else -> startRecording()
            }
        }

        btnList.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        btnDone.setOnClickListener {
            stopRecording()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBG.visibility = View.VISIBLE
            filenameInput.setText(fileName)
        }

        btnCancel.setOnClickListener {
            File("$dirPath$fileName.mp3").delete()
            dismiss()
        }

        btnOK.setOnClickListener {
            dismiss()
            save()
        }

        bottomSheetBG.setOnClickListener{
            File("$dirPath$fileName.mp3").delete()
            dismiss()
        }

        btnDelete.setOnClickListener {
            stopRecording()
            File("$dirPath$fileName.mp3").delete()
        }

        btnDelete.isClickable = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun save() {
        val filePath: String
        val newFilename = filenameInput.text.toString()
        if (newFilename != fileName) {
            val newFile = File("$dirPath$newFilename.mp3")
            File("$dirPath$fileName.mp3").renameTo(newFile)
        }

        filePath = "$dirPath$newFilename.mp3"
        val timestamp = Date().time
        val ampsPath = "$dirPath$newFilename"

        val record = AudioRecord(newFilename, filePath, timestamp, duration, ampsPath)

        GlobalScope.launch {
            db.audioRecordDao().insert(record)
        }
    }


    private fun dismiss() {
        bottomSheetBG.visibility = View.GONE
        hideKeyboard(filenameInput)

        Handler(Looper.getMainLooper()).postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }, 100)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun pauseRecording() {
        recorder.pause()
        isPaused = true
        btnRecord.setImageResource(R.drawable.ic_record)
        timer.pause()
    }

    private fun resumeRecording() {
        recorder.resume()
        isPaused = false
        btnRecord.setImageResource(R.drawable.ic_pause)
        timer.start()
    }

    @SuppressLint("SimpleDateFormat")
    private fun startRecording() {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            return
        }

        dirPath = "${externalCacheDir?.absolutePath}/"
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        val date = simpleDateFormat.format(Date())
        fileName = "audio_record_$date"

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$fileName.mp3")

            try {
                prepare()
            } catch (e: IOException){}

            start()
        }
        btnRecord.setImageResource(R.drawable.ic_pause)
        isRecording = true
        isPaused = false

        timer.start()

        btnDelete.isClickable = true
        btnDelete.setImageResource(R.drawable.ic_delete)

        btnList.visibility = View.GONE
        btnDone.visibility = View.VISIBLE
    }

    private fun stopRecording() {
        timer.stop()

        recorder.apply {
            stop()
            release()
        }

        isPaused = false
        isRecording = false

        btnList.visibility = View.VISIBLE
        btnDone.visibility = View.GONE

        btnDelete.isClickable = false
        btnDelete.setImageResource(R.drawable.ic_delete_disabled)

        btnRecord.setImageResource(R.drawable.ic_record)

        tvTimer.text = getString(R.string.defaultTime)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE)
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun onTimerTick(duration: String) {
        tvTimer.text = duration
        this.duration = duration.dropLast(3)
    }
}