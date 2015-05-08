package com.thedeveloperworldisyours.evaluationtask;

import android.app.Application;
import android.graphics.Typeface;

import com.thedeveloperworldisyours.evaluationtask.utils.Constants;

/**
 * Created by javiergonzalezcabezas on 8/5/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTypefaces();
    }

    public static class Fonts {
        public static Typeface PENCIL;
    }

    /**
     * Create font from asset
     */
    private void initializeTypefaces() {

//        Fonts.PENCIL = Typeface.createFromAsset(getAssets(), Constants.FONT_PATH);
    }
}