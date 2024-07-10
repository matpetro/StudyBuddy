package com.example.studybuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UsagePermissionActivity extends AppCompatActivity {

    Button usagePermDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usage_permission);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.usagePopup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        usagePermDone = findViewById(R.id.usagePermDone);
    }

    public void checkUsageAccess(View view) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        Handler handler = new Handler();
        handler.postDelayed(() -> usagePermDone.setVisibility(View.VISIBLE), 1000);

    }

    public void nextScreenAction(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(this, PopupPermissionActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}