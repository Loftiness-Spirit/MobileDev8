package com.example.pr8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UserListAdapter adapter = new UserListAdapter(new UserListAdapter.UserDiff());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(this, users -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(users);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, NewActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

        Log.i("app-specific", getFilesDir().toString());
        Log.i("external", getExternalFilesDir(null).toString());
        File file1 = new File(getFilesDir(),"internal.txt");
        File file2 = new File(getExternalFilesDir(null), "external.txt");
        String text = "internal";
        String text1 = "external";
        try (FileOutputStream fos = openFileOutput(file1.getName(),MODE_PRIVATE)) {
            fos.write(text.getBytes());
            if(file1.exists()){
                Log.i("app-specific","internal file exists");
            }
        } catch (Exception ex) {
            Log.e("app-specific", ex.toString());
        }
        try (FileOutputStream fos = new FileOutputStream(file2)) {
            fos.write(text1.getBytes());
        } catch (Exception ex) {
            Log.e("external", ex.toString());
        }
        SharedPreferences setts = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        setts.edit().putString("key1","text in sharedPrefernces").apply();
        Log.w("SharedPreferences",setts.getString("key1","default"));

        MyDatabase db = Room.databaseBuilder(getApplicationContext(),
                MyDatabase.class, "users data").build();
        UserDao userDao = db.userDao();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            User user = new User(data.getIntExtra("uid", 0),
                    data.getStringExtra("first_name"),
                    data.getStringExtra("last_name"));
            mUserViewModel.insert(user);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}