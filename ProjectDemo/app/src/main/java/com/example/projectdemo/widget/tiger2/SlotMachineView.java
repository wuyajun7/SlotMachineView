package com.example.projectdemo.widget.tiger2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.projectdemo.R;
import com.example.projectdemo.widget.tiger2.adapters.AbstractWheelAdapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 2019/12/10
 * explain:
 */
public class SlotMachineView extends LinearLayout {

    public static final int INDEX_GIFT_PACKAGE = 0;// 礼包
    public static final int INDEX_COIN = 1;// 金币
    public static final int INDEX_RED_PACKAGE = 2;// 红包

    private final int IMAGE_WIDTH = dip2px(getContext(), 61);
    private final int IMAGE_HEIGHT = dip2px(getContext(), 61);

    private final int[] imageItems = new int[]{
            R.drawable.img_sm_1,// 礼包
            R.drawable.img_sm_2,// 金币
            R.drawable.img_sm_3,// 红包
    };

    private int bingoIndex = INDEX_RED_PACKAGE;

    private long wheelDelayMillis = 200;
    private int wheelTime = 2000;
    private int wheelBaseIndex = imageItems.length * 200;
    private boolean isBingo;
    private boolean running;

    private WheelView wheel1, wheel2, wheel3;

    public SlotMachineView(Context context) {
        super(context);

        initData();
        initView();
    }

    public SlotMachineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initData();
        initView();
    }

    public SlotMachineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlotMachineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initData();
        initView();
    }

    private void initData() {
    }

    private void initView() {
        LinearLayout layout = (LinearLayout) View.inflate(getContext(), R.layout.layout_slot_machine, null);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        wheel1 = layout.findViewById(R.id.slot_1);
        wheel2 = layout.findViewById(R.id.slot_2);
        wheel3 = layout.findViewById(R.id.slot_3);

        wheel1.addScrollingListener(scrolledListener1);
        wheel2.addScrollingListener(scrolledListener2);
        wheel3.addScrollingListener(scrolledListener3);

        int defaultIndex = (int) (Math.random() * 10);
        initWheel(wheel1, defaultIndex);
        initWheel(wheel2, defaultIndex);
        initWheel(wheel3, defaultIndex);

        addView(layout);
    }

    private OnWheelScrollListener scrolledListener1 = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            running = true;
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
        }
    };

    private OnWheelScrollListener scrolledListener2 = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
        }
    };

    private OnWheelScrollListener scrolledListener3 = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            running = false;

            if (onScrollListener != null) {
                onScrollListener.onStop(bingoIndex);
            }
        }
    };

    public boolean isRunning() {
        return running;
    }

    private void initWheel(WheelView wheel, int defaultIndex) {
        wheel.setViewAdapter(new SlotMachineAdapter(getContext()));
        wheel.setCurrentItem(defaultIndex);
        wheel.setCyclic(true);
        wheel.setEnabled(false);
    }

    public boolean isBingo() {
        int value1 = wheel1.getCurrentItem();
        int value2 = wheel2.getCurrentItem();
        int value3 = wheel3.getCurrentItem();
        return value1 == value2 && value1 == value3;
    }

    public void startSm() {
        if (!running && bingoIndex >= INDEX_GIFT_PACKAGE && bingoIndex <= INDEX_RED_PACKAGE) {
            moveWheel(wheel1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveWheel(wheel2);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveWheel(wheel3);
                        }
                    }, wheelDelayMillis);
                }
            }, wheelDelayMillis);
        }
    }

    private void moveWheel(WheelView wheelView) {
        if (wheelView != null) {
            int currentIndex = wheelView.getCurrentItem();
            int itemsToScroll = wheelBaseIndex + imageItems.length - currentIndex + bingoIndex;
            wheelView.scroll(itemsToScroll, wheelTime);
        }
    }

    // 设置中奖坐标
    public void setBingoIndex(int index) {
        this.bingoIndex = index;
    }

    private class SlotMachineAdapter extends AbstractWheelAdapter {

        private Context context;
        private LinearLayout.LayoutParams params;
        private List<SoftReference<Bitmap>> images;

        public SlotMachineAdapter(Context context) {
            this.context = context;
            this.params = new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

            images = new ArrayList<>(imageItems.length);
            for (int id : imageItems) {
                images.add(new SoftReference<>(loadImage(id)));
            }
        }

        private Bitmap loadImage(int id) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
            bitmap.recycle();
            return scaled;
        }

        @Override
        public int getItemsCount() {
            return imageItems.length;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            LinearLayout itemLayout;

            if (cachedView != null) {
                itemLayout = (LinearLayout) cachedView;
            } else {
                itemLayout = (LinearLayout) View.inflate(context, R.layout.layout_slot_machine_item, null);
            }

            ImageView imageView = itemLayout.findViewById(R.id.sm_image);
            imageView.setLayoutParams(params);

            SoftReference<Bitmap> bitmapRef = images.get(index);
            Bitmap bitmap = bitmapRef.get();
            if (bitmap == null) {
                bitmap = loadImage(imageItems[index]);
                images.set(index, new SoftReference<>(bitmap));
            }
            imageView.setImageBitmap(bitmap);

            return itemLayout;
        }

    }

    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return (int) dipValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setBingo(boolean bingo) {
        this.isBingo = bingo;
    }

    public boolean getBingo() {
        return isBingo;
    }

    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnScrollListener {
        void onStop(int bingoIndex);
    }
}


