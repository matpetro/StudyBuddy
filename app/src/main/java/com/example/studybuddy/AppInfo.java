package com.example.studybuddy;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class AppInfo {

    // Contains the information needed for each app on the phone
    public static HashMap<String, AppInfo> appInfoHashMap = new HashMap<>();
    public static boolean alwaysBlock = false;
    private String name;
    private String pkg;
    private Drawable logo;
    private boolean blocked;

    public AppInfo(String name, String pkg, Drawable logo, boolean blocked)
    {
        this.name = name;
        this.pkg = pkg;
        this.logo = logo;
        this.blocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
