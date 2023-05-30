package com.example.pr8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class NewActivity extends AppCompatActivity {
    private EditText mEditUserView1;
    private EditText mEditUserView2;
    private EditText mEditUserView3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        mEditUserView1 = findViewById(R.id.edit_uid);
        mEditUserView2 = findViewById(R.id.edit_first_name);
        mEditUserView3 = findViewById(R.id.edit_last_name);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditUserView1.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                int uid = Integer.parseInt(mEditUserView1.getText().toString());
                String first_name = mEditUserView2.getText().toString();
                String last_name = mEditUserView3.getText().toString();
                replyIntent.putExtra("uid", uid);
                replyIntent.putExtra("first_name", first_name);
                replyIntent.putExtra("last_name", last_name);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}