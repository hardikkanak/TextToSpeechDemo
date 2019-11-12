package com.texttospeechdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    Button button;
    EditText editText;
    private TextToSpeech tts;
    Bundle params = new Bundle();
    String text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.enterYouText);

        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() > 0){
                    text = editText.getText().toString();
                    speakOut();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter some text.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                button.setEnabled(true);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakOut() {

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Started" , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        startSpeech(text);
    }


    void startSpeech(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "Dummy String");
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
