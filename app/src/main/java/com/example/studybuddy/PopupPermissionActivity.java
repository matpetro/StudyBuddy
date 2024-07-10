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

// Activity that asks and directs users to allow for the app to display pop ups (to block specified apps)
public class PopupPermissionActivity extends AppCompatActivity {

    Button popupPermDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_popup_permission);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.permPopup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        popupPermDone = findViewById(R.id.popupPermDone);
    }

    // redirects user to the settings to switch on the permission
    public void checkPopupPermission(View view) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        Handler handler = new Handler();
        handler.postDelayed(() -> popupPermDone.setVisibility(View.VISIBLE), 1000);
    }

    // Once done, brings you back to the main activity
    public void nextScreenAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}