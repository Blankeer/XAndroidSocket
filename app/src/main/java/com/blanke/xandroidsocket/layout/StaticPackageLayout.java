package com.blanke.xandroidsocket.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blanke.xandroidsocket.R;
import com.blanke.xsocket.tcp.client.helper.stickpackage.AbsStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.BaseStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.SpecifiedStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.StaticLenStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.VariableLenStickPackageHelper;

import java.nio.ByteOrder;

/**
 */
public class StaticPackageLayout extends FrameLayout {
    private AppCompatSpinner staticpackageSpinnerChose;
    private EditText staticpackageEditStaticlen;
    private LinearLayout staticpackageLayoutVariablelen;
    private AppCompatSpinner staticpackageSpinnerOrder;
    private EditText staticpackageEditVariablelenLensize;
    private EditText staticpackageEditVariablelenLenindex;
    private EditText staticpackageEditVariablelenOffset;
    private LinearLayout staticpackageLayoutSpecified;
    private EditText staticpackageEditSpecifiedHead;
    private EditText staticpackageEditSpecifiedTail;
    private FrameLayout staticpackageLayout;
    private int selectPosition;

    public StaticPackageLayout(Context context) {
        super(context);
    }

    public StaticPackageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticPackageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StaticPackageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_staticpackage, this, true);
        staticpackageSpinnerChose = (AppCompatSpinner) findViewById(R.id.staticpackage_spinner_chose);
        staticpackageEditStaticlen = (EditText) findViewById(R.id.staticpackage_edit_staticlen);
        staticpackageLayoutVariablelen = (LinearLayout) findViewById(R.id.staticpackage_layout_variablelen);
        staticpackageSpinnerOrder = (AppCompatSpinner) findViewById(R.id.staticpackage_spinner_order);
        staticpackageEditVariablelenLensize = (EditText) findViewById(R.id.staticpackage_edit_variablelen_lensize);
        staticpackageEditVariablelenLenindex = (EditText) findViewById(R.id.staticpackage_edit_variablelen_lenindex);
        staticpackageEditVariablelenOffset = (EditText) findViewById(R.id.staticpackage_edit_variablelen_offset);
        staticpackageLayoutSpecified = (LinearLayout) findViewById(R.id.staticpackage_layout_specified);
        staticpackageEditSpecifiedHead = (EditText) findViewById(R.id.staticpackage_edit_specified_head);
        staticpackageEditSpecifiedTail = (EditText) findViewById(R.id.staticpackage_edit_specified_tail);
        staticpackageLayout = (FrameLayout) findViewById(R.id.staticpackage_layout);
        staticpackageSpinnerChose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectNone();
                        break;
                    case 1:
                        selectSpecified();
                        break;
                    case 2:
                        selectStaticlen();
                        break;
                    case 3:
                        selectVariablelen();
                        break;
                }
                selectPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public AbsStickPackageHelper getStickPackageHelper() {
        AbsStickPackageHelper stickPackageHelper = null;
        switch (selectPosition) {
            case 0:
                selectNone();
                stickPackageHelper = new BaseStickPackageHelper();
                break;
            case 1:
                String head = staticpackageEditSpecifiedHead.getText().toString().trim();
                String tail = staticpackageEditSpecifiedTail.getText().toString().trim();
                if (!TextUtils.isEmpty(head) || !TextUtils.isEmpty(tail)) {
                    stickPackageHelper = new SpecifiedStickPackageHelper(head.getBytes(), tail.getBytes());
                }
                break;
            case 2:
                String lenStr = staticpackageEditStaticlen.getText().toString().trim();
                if (!TextUtils.isEmpty(lenStr)) {
                    int len = Integer.parseInt(lenStr);
                    stickPackageHelper = new StaticLenStickPackageHelper(len);
                }
                break;
            case 3:
                int orderPosition = staticpackageSpinnerOrder.getSelectedItemPosition();
                ByteOrder byteOrder = orderPosition == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                String lenSizeStr = staticpackageEditVariablelenLensize.getText().toString().trim();
                if (!TextUtils.isEmpty(lenSizeStr)) {
                    int lenSize = Integer.parseInt(lenSizeStr);
                    String lenIndexStr = staticpackageEditVariablelenLenindex.getText().toString().trim();
                    if (!TextUtils.isEmpty(lenIndexStr)) {
                        int lenIndex = Integer.parseInt(lenIndexStr);
                        String lenOffsetStr = staticpackageEditVariablelenOffset.getText().toString().trim();
                        if (!TextUtils.isEmpty(lenOffsetStr)) {
                            int offset = Integer.parseInt(lenOffsetStr);
                            stickPackageHelper = new VariableLenStickPackageHelper(byteOrder, lenSize, lenIndex, offset);
                        }
                    }
                }
                break;
        }
        return stickPackageHelper;
    }

    private void selectNone() {
        hideAllChild();
    }

    private void selectSpecified() {
        hideAllChild();
        staticpackageLayoutSpecified.setVisibility(VISIBLE);
    }

    private void selectStaticlen() {
        hideAllChild();
        staticpackageEditStaticlen.setVisibility(VISIBLE);
    }

    private void selectVariablelen() {
        hideAllChild();
        staticpackageLayoutVariablelen.setVisibility(VISIBLE);
    }

    private void hideAllChild() {
        for (int i = 0; i < staticpackageLayout.getChildCount(); i++) {
            View v = staticpackageLayout.getChildAt(i);
            v.setVisibility(GONE);
        }
    }
}
