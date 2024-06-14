package com.example.studybuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PopupPermissionActivity extends AppCompatActivity {

    Button popupPermDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_popup_permission);
        popupPermDone = findViewById(R.id.popupPermDone);
    }

    public void checkPopupPermission(View view) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        // TODO should we stop the button from showing until the permission has been allowed?
        popupPermDone.setVisibility(View.VISIBLE);
    }

    public void nextScreenAction(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}