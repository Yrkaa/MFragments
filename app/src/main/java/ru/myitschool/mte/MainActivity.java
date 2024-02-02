package ru.myitschool.mte;

import static java.lang.Thread.sleep;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import ru.myitschool.mte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private Button btnStart, btnStop;

    private boolean runThread = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnStart = binding.content.startBtn;
        btnStop = binding.content.stopBtn;

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fragmentThread.isAlive())
                    fragmentThread.start();
                runThread = true;

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runThread = false;
            }
        });


    }
    Thread fragmentThread = new Thread(new Runnable() {
        @Override
        public void run() {
            final boolean[] firstItem = {true};
            while(true){
                if(runThread){
                    //Основной код
                    try {
                        FirstFragment firstFragment = new FirstFragment();
                        ProceedingFragment proceedingFragment = new ProceedingFragment();
                        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (firstItem[0])
                                    fragmentTransaction.replace(R.id.output_fragment, firstFragment);
                                else
                                    fragmentTransaction.replace(R.id.output_fragment, proceedingFragment);
                                firstItem[0] = !firstItem[0];
                                fragmentTransaction.commit();
                            }
                        });
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {throw new RuntimeException(e);}
                    //Основной код
                }
            }

        }

    });
}
