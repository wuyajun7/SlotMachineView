# SlotMachineView
老虎机，传统老虎机实现

# 布局文件
    <com.xxx.SlotMachineView
        android:id="@+id/sm_view"
        android:layout_width="281dp"
        android:layout_height="145dp"
        android:layout_gravity="center_horizontal" />


# 调用
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
