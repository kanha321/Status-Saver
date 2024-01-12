package com.kanha.statussaver.Adapters;

import static com.kanha.statussaver.MainActivity.tabCount;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kanha.statussaver.Fragments.FragmentSaved;
import com.kanha.statussaver.Fragments.FragmentStatuses;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentStateAdapter {

    static List<Fragment> tabs = new ArrayList<>();

    public TabsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        tabs.add(new FragmentStatuses());
        tabs.add(new FragmentSaved());

        int a = 0;
        for (int i = 0; i < tabs.size(); i++)
            if (position == i)
                a = i;
        return tabs.get(a);
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
