
package com.example.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var divideResult : TextView
    private lateinit var edtTotalPrice : EditText
    private var peopleNumber: Int = 0
    private var totalPrice : Double = 0.0

    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.edtConta) //numero de pessoas
        divideResult = findViewById(R.id.result)
        edtTotalPrice = findViewById(R.id.editPrice)
        edtConta.addTextChangedListener(this)
        edtTotalPrice.addTextChangedListener(this)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não utilizado neste exemplo
            }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val people = (edtConta.text.toString()).toIntOrNull() ?: 0
        val price = (edtTotalPrice.text.toString()).toDoubleOrNull() ?: 0.0
        divideResult.text = calculateDivision(people,price)
    }

    private fun calculateDivision(people: Int, price: Double): String = "R$${(price / people).toString()}"

    override fun afterTextChanged(s: Editable?) {
    // Não utilizado neste exemplo
    }



    fun clickFalar(v: View){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak("Oi Sumido", TextToSpeech.QUEUE_FLUSH, null, null)
        }




    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

