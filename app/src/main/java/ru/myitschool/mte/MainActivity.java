package ru.myitschool.mte;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import ru.myitschool.mte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private Button btnStart, btnStop;


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
                stopped = false;
                fragmentThread.start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentThread.interrupt();
                stopped=true;
            }
        });


    }
    boolean stopped = true;
    Thread fragmentThread = new Thread(new Runnable() {
        @Override
        public void run() {
            FirstFragment firstFragment = new FirstFragment();
            ProceedingFragment proceedingFragment = new ProceedingFragment();

            final boolean[] firstFragmentWatch = {true};
            final boolean[] firstItem = {true};

            while (!stopped){
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        if(firstItem[0]){
                            fragmentTransaction.add(R.id.output_fragment, firstFragment);
                            firstItem[0] = false;
                            firstFragmentWatch[0] = false;
                        }
                        else{
                            if(firstFragmentWatch[0])
                                fragmentTransaction.replace(R.id.output_fragment, proceedingFragment);
                            else
                                fragmentTransaction.replace(R.id.output_fragment, firstFragment);
                            firstFragmentWatch[0] = !firstFragmentWatch[0];
                        }
                        fragmentTransaction.commit();

                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    });
}
