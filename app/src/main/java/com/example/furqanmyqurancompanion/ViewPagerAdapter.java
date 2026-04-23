package com.example.furqanmyqurancompanion;

public class ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
    public ViewPagerAdapter(@androidx.annotation.NonNull androidx.fragment.app.FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @androidx.annotation.NonNull
    @Override
    public androidx.fragment.app.Fragment createFragment(int position) {
        switch (position) {
            case 0: return new homeFragment();
            case 1: return new quranFragment();
            case 2: return new audioFragment();
            case 3: return new guideFragment();
            case 4: return new profileFragment();
            default: return new homeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}