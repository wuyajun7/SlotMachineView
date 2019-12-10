package com.example.projectdemo.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.projectdemo.R;
import com.example.projectdemo.widget.tiger2.SlotMachineView;

import static com.example.projectdemo.widget.tiger2.SlotMachineView.INDEX_COIN;

/**
 * Created by ethan on 2019/12/10
 * explain:
 */
public class SlotMachineActivity extends Activity {

    private SlotMachineView slotMachineView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_slot_machine);

        slotMachineView = findViewById(R.id.sm_view);
        slotMachineView.setOnScrollListener(new SlotMachineView.OnScrollListener() {
            @Override
            public void onStop(int bingoIndex) {

            }
        });

        findViewById(R.id.btn_mix).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                slotMachineView.setBingoIndex(INDEX_COIN);
                slotMachineView.startSm();
            }
        });
    }
}
