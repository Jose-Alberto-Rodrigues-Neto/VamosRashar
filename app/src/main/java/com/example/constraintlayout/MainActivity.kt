/*Todo:As features principais são:
   [Feito] (2 pontos) - Por enquanto, o aplicativo só faz divisões do valor pelo número de pessoas
   [Feito] (2 pontos) - Mas ele já tem um ícone
   [Feito] - Já permite o compartilhamento do valor final
   [Feito] (2 pontos) - Fala o valor calculado usando TTS
   [Feito] (2 pontos) - O usuário não precisa clicar para calcular, ele já faz automaticamente após o preenchimento dos campos de valor e número de pessoas
    Envie o trabalho usando um PDF com o printscreen da tela e um link para o repositório do GitHub (ou similar) do projeto*/

package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var divideResult : TextView
    private lateinit var edtTotalPrice : EditText
    private lateinit var speaker : FloatingActionButton
    private lateinit var shareButton : FloatingActionButton

    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.edtConta) //numero de pessoas
        divideResult = findViewById(R.id.result)
        edtTotalPrice = findViewById(R.id.editPrice)
        speaker = findViewById(R.id.floatingTTSButton)
        shareButton = findViewById(R.id.floatingSharingButton)
        tts = TextToSpeech(this, this)

        edtConta.addTextChangedListener(this)
        edtConta.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source == "0" && dest.isEmpty()){
                ""
            } else {
                source
            }
        })
        edtTotalPrice.addTextChangedListener(this)
        speaker.setOnClickListener {
            clickFalar()
        }

        shareButton.setOnClickListener{
            clickShare()
        }


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não utilizado neste exemplo
            }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
    // Não utilizado neste exemplo7
        val people = (edtConta.text.toString()).toIntOrNull() ?: 0
        val price = (edtTotalPrice.text.toString()).toDoubleOrNull() ?: 0.0
        divideResult.text = calculateDivision(people,price)
    }

    private fun calculateDivision(people: Int, price: Double): String {
        return if (people>0){
            "R$ ${(price / people)}"
        } else {
            "Valor Inválido"
        }
    }

    fun clickShare(){
        val TextShare = "Valor Total: R$ ${edtTotalPrice.text.toString().toDoubleOrNull() ?: 0.0} \n" +
                "Total de Pessoas: ${edtConta.text}\n" +
                "Valor Para cada Pessoa: ${divideResult.text}"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, TextShare)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Compartilhar com: "))
    }

    fun clickFalar(){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak("Cada um terá que pagar ${divideResult.text}", TextToSpeech.QUEUE_FLUSH, Bundle(), null)
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

