package com.bleckshiba.speechtotext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var spinner: Spinner
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)
        spinner = findViewById(R.id.spinner)

        initRecordingSetup()

        val btnRecord: Button = findViewById(R.id.btnRecord)
        btnRecord.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString())
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力")
            resultLauncher.launch(intent)
        }
    }

    private fun initRecordingSetup() {
        try {
            resultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data;
                    if (data != null) {
                        val items: ArrayList<String>? =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        if (items.isNullOrEmpty().not()) {
                            textView.text = items?.get(0)
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("MAIN", e.localizedMessage)
        }
    }
}