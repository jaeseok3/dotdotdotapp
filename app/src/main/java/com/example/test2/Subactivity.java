package com.example.test2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.ArrayList;
import java.util.Locale;

public class Subactivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView textView;
    private TextToSpeech tts;              // TTS 변수 선언
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        textView = (TextView) findViewById(R.id.textView1); /*TextView선언*/
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    // 언어를 선택한다.
                    System.out.println("성공");
                    tts.setLanguage(Locale.KOREAN);
                    tts.setSpeechRate((float) 1.1);
                    tts.setPitch((float)1.0);
                }
                tts.speak("읽을 책을 말해주세요!",TextToSpeech.QUEUE_FLUSH, null);
            }
        });

//        Intent intent_1 = getIntent();

//        int text_1 = Integer.parseInt(intent_1.getExtras().getString("text1"));
//        int text_2 = Integer.parseInt(intent_1.getExtras().getString("text2"));
//        int text_3 = Integer.parseInt(intent_1.getExtras().getString("text3"));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecognizer.startListening(intent);

            }
        }, 3000);



    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            textView.setText("Ready");
        }

        @Override
        public void onBeginningOfSpeech() {
            textView.setText("Beginning");
        }

        @Override
        public void onRmsChanged(float v) {
            textView.setText("Changed");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            textView.setText("Buffer");
        }

        @Override
        public void onEndOfSpeech() {
            textView.setText("End");
        }

        @Override
        public void onError(int i) {
            textView.setText("너무 늦게 말하면 오류뜹니다");

        }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            textView.setText(rs[0]);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };
}