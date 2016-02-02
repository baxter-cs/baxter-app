package org.baxter_academy.flex;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by wil on 1/18/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter{

    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.numOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentTodo tabTodo = new FragmentTodo();
                return tabTodo;
            case 1:
                FragmentDoing tabDoing = new FragmentDoing();
                return tabDoing;
            case 2:
                FragmentDone tabDone = new FragmentDone();
                return tabDone;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
