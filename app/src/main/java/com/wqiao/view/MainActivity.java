package com.wqiao.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ArrowKeyView mArrowKeyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArrowKeyView = findViewById(R.id.arrowKeyView);

        mArrowKeyView.setOnclickPtzListener(new ArrowKeyView.OnclickPtzListener() {
            @Override
            public void clickLeft() {
                showShortToast("向左");
            }

            @Override
            public void clickUp() {
                showShortToast("向上");
            }

            @Override
            public void clickRight() {
                showShortToast("向右");
            }

            @Override
            public void clickDown() {
                showShortToast("向下");
            }
        });
    }

    private void showShortToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
