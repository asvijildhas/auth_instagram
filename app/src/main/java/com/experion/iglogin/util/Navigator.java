package com.experion.iglogin.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.experion.iglogin.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Navigator {

    private static volatile Navigator instance;

    public static Navigator getInstance() {
        Navigator service = instance;
        if (service == null) {
            synchronized (Navigator.class) {
                service = instance;
                if (service == null) {
                    service = new Navigator();
                    instance = service;
                }
            }
        }
        return service;
    }


    public void navigate(Activity parent, Class target, Bundle bundle, boolean finishParent, boolean clearHistory) {
        Intent intent = new Intent(parent, target);
        if (bundle != null)
            intent.putExtras(bundle);
        if (clearHistory)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        parent.startActivity(intent);

        parent.overridePendingTransition(R.anim.enter, R.anim.exit);
        if (finishParent)
            parent.finish();
    }


    public void navigateForResult(Activity parent, Class target, Bundle bundle, boolean finishParent, boolean clearHistory, int intent_key) {
        Intent intent = new Intent(parent, target);

        if (bundle != null)
            intent.putExtras(bundle);
        if (clearHistory)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        parent.startActivityForResult(intent, intent_key);
        parent.overridePendingTransition(R.anim.enter, R.anim.exit);
        if (finishParent)
            parent.finish();
    }



    public void loadFragment(AppCompatActivity parent, int cotainerId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(cotainerId, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);


        fragmentTransaction.commit();
    }
}
