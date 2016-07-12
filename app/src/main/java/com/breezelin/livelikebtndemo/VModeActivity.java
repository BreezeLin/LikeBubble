package com.breezelin.livelikebtndemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.breezelin.likebubble.widgets.BubbleLikeView2;

public class VModeActivity extends AppCompatActivity {

    private BubbleLikeView2 likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmode);

        likeView = (BubbleLikeView2) findViewById(R.id.like_view);
    }

    public void showBubble(View view) {
        likeView.addBubble();
    }
}
