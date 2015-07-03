package com.scottiebiddle.valdezbreakingnews;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by scottie on 7/2/15.
 */
public class BreakingNewsValdezApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build()
        );
    }


}
