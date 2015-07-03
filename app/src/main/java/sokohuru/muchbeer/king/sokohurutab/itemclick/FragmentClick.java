package sokohuru.muchbeer.king.sokohurutab.itemclick;

import android.app.Activity;
import android.os.Bundle;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sokohuru.muchbeer.king.sokohurutab.R;

/**
 * Created by muchbeer on 7/1/2015.
 */
public class FragmentClick  extends Fragment {

    public FragmentClick() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_click, container, false);
      //  mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
       // adapter = new ListAdapterHolder(mActivity);
        return rootView;
    }

}