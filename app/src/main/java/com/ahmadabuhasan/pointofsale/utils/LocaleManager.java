package com.ahmadabuhasan.pointofsale.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleManager {

    public static final String ENGLISH = "en";
    public static final String INDONESIAN = "id";
    private static final String LANGUAGE_KEY = "language_key";

    @Retention(RetentionPolicy.SOURCE)
    public @interface LocaleDef {
        public static final String[] SUPPORTED_LOCALES = {
                LocaleManager.ENGLISH,
                LocaleManager.INDONESIAN
        };
    }

    public static Context setNewLocale(Context context, String language) {
        setLanguagePref(context, language);
        return updateResources(context, language);
    }

    public static String getLanguagePref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANGUAGE_KEY, ENGLISH);
    }

    private static void setLanguagePref(Context context, String localeKey) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANGUAGE_KEY, localeKey).apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        if (Build.VERSION.SDK_INT >= 23) {
            configuration.setLocale(locale);
            return context.createConfigurationContext(configuration);
        }
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public static Locale getLocale(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? configuration.getLocales().get(0) : configuration.locale;
    }
}