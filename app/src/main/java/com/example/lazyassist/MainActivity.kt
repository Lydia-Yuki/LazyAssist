package com.example.lazyassist

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SPEECH_INPUT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Button click to show SpeechToText dialogue
        voiceBtn.setOnClickListener {
            speak();
        }
        //intent?.handleIntent()
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        intent?.handleIntent()
//    }

    private fun speak() {
        //intent to show speech to text dialogue
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something")

        try {
            //if there is no error show speechtotext dialogue
            startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
        }
        catch (e: Exception){
            //if there is any error get error message and show in toast
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    //get text from result
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    //set the text to text view
                    textTv.text = result?.get(0)
//                    if (textTv.text.contains("call", ignoreCase = true)) {
//                        val string = textTv.text
//                        val name = String.substringAfterLast("call")
//
//                        val callIntent = Intent().apply {
//                            action = Intent.ACTION_CALL
//                            putExtra(Intent.EXTRA_PHONE_NUMBER, name)
//                            //data = Uri.parse("tel:phoneNumber")
//
//
//                        }
//
//                        // Try to invoke the intent.
//                        try {
//                            startActivity(callIntent)
//                        } catch (e: ActivityNotFoundException) {
//                            // Define what your app should do if no activity can handle the intent.
//                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }

                    if (textTv.text.contains("Whatsapp", ignoreCase = true)) {
                        // Create the text message with a string
                        val appIntent = Intent().apply {
                            val app: Intent = Uri.parse("com.whatsapp").let { appl -> Intent(Intent.ACTION_VIEW, appl) }
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, textTv.text)
                            putExtra(Intent.EXTRA_COMPONENT_NAME, app)
                            type = "text/plain"
                        }


                        // Try to invoke the intent.
                        try {
                            startActivity(appIntent)
                        } catch (e: ActivityNotFoundException) {
                            // Define what your app should do if no activity can handle the intent.
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    if (textTv.text.contains("text", ignoreCase = true)) {
                        // Create the text message with a string.
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, textTv.text)
                            type = "text/plain"
                        }

                        // Try to invoke the intent.
                        try {
                            startActivity(sendIntent)
                        } catch (e: ActivityNotFoundException) {
                            // Define what your app should do if no activity can handle the intent.
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}