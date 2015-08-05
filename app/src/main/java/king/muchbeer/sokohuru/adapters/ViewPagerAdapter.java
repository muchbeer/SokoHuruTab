package king.muchbeer.sokohuru.adapters;

/**
 * Created by muchbeer on 6/22/2015.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import  king.muchbeer.sokohuru.LoginFragment;
import  king.muchbeer.sokohuru.SokoHuruFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
           // sokohuru.muchbeer.king.sokohurutab.syncItem.SokoHuruFragment tab1 = new sokohuru.muchbeer.king.sokohurutab.syncItem.SokoHuruFragment();


            SokoHuruFragment tab1 = new SokoHuruFragment();

            return tab1;
                }

        else if(position ==1) {
            LoginFragment tab2 = new LoginFragment();

            return tab2;
        }


        return null;
    }

// This method return the titles for the Tabs in the Tab Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}
