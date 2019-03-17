package com.bakaikin.sergey.reminder;

import android.app.Activity;
import android.view.View;

public class Ads {

//    public static void showBanner(final Activity activity) {
//
//        final AdView banner = (AdView) activity.findViewById(R.id.banner);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        banner.loadAd(adRequest);
//
//        banner.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                setupContentViewPadding(activity, banner.getHeight());
//            }
//        });
//    }

    public static void setupContentViewPadding(Activity activity, int padding) {
        View view = activity.findViewById(R.id.coordinator);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }
}
