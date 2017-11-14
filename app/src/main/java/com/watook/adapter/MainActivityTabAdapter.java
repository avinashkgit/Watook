package com.watook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.watook.fragment.ChatsListFragment;
import com.watook.fragment.NearByFragment;
import com.watook.fragment.LikesFragment;


public class MainActivityTabAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[]{"CHATS", "NEAR BY", "LIKES"};


    public MainActivityTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChatsListFragment.newInstance();
            case 1:
                return NearByFragment.newInstance();
            case 2:
                return LikesFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
