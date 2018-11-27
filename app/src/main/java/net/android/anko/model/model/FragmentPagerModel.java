package net.android.anko.model.model;

import android.support.v4.app.Fragment;

public class FragmentPagerModel {
    public String title;
    public Fragment fragment;

    public FragmentPagerModel(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
