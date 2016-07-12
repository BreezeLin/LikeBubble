package com.breezelin.livelikebtndemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.breezelin.likebubble.utils.ScreenUtil;
import com.breezelin.likebubble.widgets.BubbleLikeView;

public class VGModeActivity extends AppCompatActivity {

    private BubbleLikeView likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vgmode);

        likeView = (BubbleLikeView) findViewById(R.id.like_view);
        if (likeView != null) {
            likeView.setBubbleGravity(Gravity.END, 0);
            likeView.setBubbleFlyPadding(0, ScreenUtil.dip2px(25, this));
        }
    }

    /**
     * 吐出单个泡泡
     */
    public void showSingleAnimation(View view) {
        likeView.showBubble();
    }

    /**
     * 吐出泡泡，瞬间
     */
    public void showInstantAnimation(View view) {
        likeView.showBubbleForDuration(1000);
    }

    /**
     * 吐出泡泡，短时间
     */
    public void showShortAnimation(View view) {
        likeView.showBubbleForDuration(3000);
    }

    /**
     * 吐出泡泡，长时间
     */
    public void showLongAnimation(View view) {
        likeView.showBubbleForDuration(10000);
    }
}
