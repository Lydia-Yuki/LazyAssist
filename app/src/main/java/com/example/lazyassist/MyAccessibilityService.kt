package com.example.lazyassist


import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MyAccessibilityService : AccessibilityService() {
    var mLayout: FrameLayout? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected() {

        // Create an overlay and display the action bar
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(this)
        val lp = WindowManager.LayoutParams()
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        lp.format = PixelFormat.TRANSLUCENT
        lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.activity_main, mLayout)
        wm.addView(mLayout, lp)

        //configureButton();

    }

    override fun onInterrupt() {

    }
    

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        configureButton();

        lateinit var mTTS: TextToSpeech

        val eventType = event!!.eventType
        var eventText: String? = null
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> eventText = "Focused: "
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> eventText = "Focused: "
        }

        eventText = eventText + event.contentDescription

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.UK
            }
        })

        mTTS.speak(eventText, TextToSpeech.QUEUE_FLUSH, null)

    }

    val REQUEST_CODE_SPEECH_INPUT = 100

    private fun configureButton() {
        val voiceButton: ImageButton = mLayout!!.findViewById<View>(R.id.voiceBtn) as ImageButton
        voiceButton.setOnClickListener{
            speak();
            }


        }

    private fun speak() {
        //intent to show speech to text dialogue
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something")
        //mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val b = Bundle();
        b.putInt("speech input", REQUEST_CODE_SPEECH_INPUT)


        try {
            //if there is no error show speechtotext dialogue
            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            startActivity(mIntent, b)
        }
        catch (e: Exception){
            //if there is any error get error message and show in toast
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
