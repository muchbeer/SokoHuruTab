package sokohuru.muchbeer.king.sokohurutab.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sokohuru.muchbeer.king.sokohurutab.MainActivity;
import sokohuru.muchbeer.king.sokohurutab.R;
import sokohuru.muchbeer.king.sokohurutab.SokoHuruFragment;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.detail.MainActivityDetail;
import sokohuru.muchbeer.king.sokohurutab.extras.Constants;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;

/**
 * Created by muchbeer on 6/19/2015.
 */
public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolderSokoni> {


    private final LayoutInflater layoutInflater;
    private final VolleySingleton volleySingleTon;
    private final ImageLoader imageLoader;
    private int SHARE_CODE = 1;

   // final ArrayList<Soko> filteredModelList;
    private Filter planetFilter;

    public ArrayList<Soko> itemList = new ArrayList<>();
    private ArrayList<Soko> arraylist;
    public ArrayList<Soko> origPlanetList = new ArrayList<>();
    private ArrayList<Soko> slistSokoni = new ArrayList<>();
    private static Context mcontext;
    private static ClickListener clickListener;
    private static ClickListenerSearch clickListenerSearch;
    public ArrayList<Soko> filteredModelList;
    int positionForSearch;

    public AdapterSearch(Context context, ArrayList<Soko> slistSokoni) {
        mcontext = context;

        volleySingleTon = VolleySingleton.getsInstance();
        imageLoader = volleySingleTon.getImageLoader();
        this.slistSokoni = slistSokoni;
        this.arraylist = new ArrayList<Soko>();
        this.arraylist.addAll(slistSokoni);
         layoutInflater = LayoutInflater.from(context);



    //    this.itemList = new ArrayList<Soko>();
       // this.filteredModelList = new ArrayList<Soko>();
        //  slistSokoni = new ArrayList<>(slistSokoni);

        // L.t(context, "message");
    }

    public void setSokoList(ArrayList<Soko> listSokoni) {
        this.slistSokoni = listSokoni;
        this.filteredModelList = listSokoni;
        this.arraylist = listSokoni;
        notifyItemRangeChanged(0, listSokoni.size());

    }

    public void animateTo(List<Soko> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
       applyAndAnimateMovedItems(models);
        //  setClickListener.
    }

    private void applyAndAnimateRemovals(List<Soko> newModels) {
        for (int i = slistSokoni.size() - 1; i >= 0; i--) {
            final Soko model = slistSokoni.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions2(List<Soko> newModels) {
        for (int i = slistSokoni.size(), count = newModels.size(); i < count; i++) {
            final Soko model = newModels.get(i);
            if (!slistSokoni.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Soko> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Soko model = newModels.get(i);
            if (!slistSokoni.contains(model)) {
                addItem(i, model);
            }
        }

    }


    private void applyAndAnimateMovedItems(List<Soko> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Soko model = newModels.get(toPosition);
           // public void getPositioning()
            final int fromPosition = slistSokoni.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
               moveItem(fromPosition, toPosition);
              //  positionForSearch = fromPosition;
                MainFragmentActivity mainFragmentActivity = new MainFragmentActivity();
                mainFragmentActivity.returnPosition(fromPosition);
              //  adapterPosition = toPosition;
            }

        }
    }

    public Soko removeItem(int position) {
        final Soko model = slistSokoni.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Soko model) {
        slistSokoni.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Soko model = slistSokoni.remove(fromPosition);
        slistSokoni.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
       // positionForSearch = fromPosition;
      //  notifyDataSetChanged();
    }

    @Override
    public AdapterSearch.ViewHolderSokoni onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =  layoutInflater.inflate(R.layout.fragment_soko_lookfeel, parent, false);
        ViewHolderSokoni viewHolderSokoni = new ViewHolderSokoni(view);


        return viewHolderSokoni;
    }

    @Override
    public void onBindViewHolder(final ViewHolderSokoni holder, int position) {
        Soko currentItem = slistSokoni.get(position);

        holder.sokoTitle.setText(currentItem.getTitle());
        holder.sokoGenre.setText(currentItem.getGenre());

        String urlThumbnail = currentItem.getImage();
        if(urlThumbnail !=null) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean b) {
                    holder.sokoThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    // L.t(context, volleyError.toString());
                    // holder.sokoThumbnail.setImageBitmap();
                }
            });
        }



    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public void setClickListenerSearch( ClickListenerSearch clickListenerSearch) {
        this.clickListenerSearch = clickListenerSearch;

    }

    @Override
    public int getItemCount() {

        return slistSokoni.size();
    }


    public static class ViewHolderSokoni extends RecyclerView.ViewHolder implements View.OnClickListener{

        private static final int SHARING_CODE = 1;
        public ImageView sokoThumbnail;
        public TextView sokoTitle;
        public TextView sokoGenre;
        public int retreivePosition;

        //  private ClickListener clickListener;


        public ViewHolderSokoni(View itemView) {
            super(itemView);

            sokoThumbnail = (ImageView) itemView.findViewById(R.id.sokoThumbnail);
            sokoTitle = (TextView) itemView.findViewById(R.id.sokoTitle);
            sokoGenre = (TextView) itemView.findViewById(R.id.sokoRating);


            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            //  Intent startIntent = new Intent(context, MainActivityDetail.class);

            //  ((MainActivity)context).startActivityForResult(i, SHARING_CODE );

            if(clickListener!=null) {
                clickListener.itemClicked(view, getPosition());

            }
            if(clickListenerSearch !=null) {


                clickListenerSearch.itemClicked2(view,  getPosition(), retreivePosition);
            }
        }


    }
    public interface ClickListener{

        public void itemClicked(View view, int position);
    }

    public interface ClickListenerSearch {
        public void itemClicked2(View view, int positionSearch, int movedItem);
    }
    public ArrayList<Soko> filterBest(ArrayList<Soko> models, String query) {
      //  query = query.toLowerCase();

     //  filteredModelList = new ArrayList<>();
          //  position = slistSokoni.(position);

       // ClickListener().

        query = query.toLowerCase();

        final ArrayList<Soko> filteredModelList = new ArrayList<>();
        if (query.length() == 0) {
            slistSokoni.addAll(models);
        } else {
            for (Soko model : models) {
                final String text = model.getTitle().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }

        }
        notifyDataSetChanged();
        return filteredModelList;
    }


    public ArrayList<Soko> filter(ArrayList<Soko> models, String query) {
        // query = query.toLowerCase();

        query = query.toLowerCase(Locale.getDefault());
        slistSokoni.clear();

        //    ArrayList<Soko> filteredModelList = new ArrayList<>();
        if (query.length() == 0) {
            slistSokoni.addAll(itemList);
        } else {
            for (Soko model : models) {
                if (model.getTitle().toLowerCase(Locale.getDefault()).contains(query)) {
                    itemList.add(model);
                }
            }
        }
        notifyDataSetChanged();
            return models;
    }

    public ArrayList<Soko> filterJesus(ArrayList<Soko> model, String query) {
        query =     query.toLowerCase(Locale.getDefault());
       // slistSokoni.clear();

            for (Soko wp : model)
            {
                if ((wp.getTitle().toLowerCase(Locale.getDefault()).contains(query)))
                {
                    arraylist.add(wp);

                }
            }
        notifyDataSetChanged();
        return arraylist;
    }


}
