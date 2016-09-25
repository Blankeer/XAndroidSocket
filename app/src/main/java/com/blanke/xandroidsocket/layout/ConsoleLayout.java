package com.blanke.xandroidsocket.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blanke.xandroidsocket.R;

/**
 */
public class ConsoleLayout extends FrameLayout {
    private TextView consoleTitle;
    private Button consoleClear;
    private TextView consoleText;


    public ConsoleLayout(Context context) {
        super(context);
    }

    public ConsoleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConsoleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConsoleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_console, this, true);
        consoleTitle = (TextView) findViewById(R.id.console_title);
        consoleClear = (Button) findViewById(R.id.console_clear);
        consoleText = (TextView) findViewById(R.id.console_text);
        consoleClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearConsole();
            }
        });
    }

    public void clearConsole() {
        consoleText.setText("");
    }

    public void addLog(String msg) {
        consoleText.append(msg + "\n");
    }
}
