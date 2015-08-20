package king.muchbeer.sokohuru.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

import  king.muchbeer.sokohuru.MainActivity;
import king.muchbeer.sokohuru.R;
import  king.muchbeer.sokohuru.SokoHuruFragment;
import  king.muchbeer.sokohuru.Sokoni.Soko;
import  king.muchbeer.sokohuru.detail.MainActivityDetail;
import  king.muchbeer.sokohuru.extras.Constants;
import  king.muchbeer.sokohuru.network.VolleySingleton;

/**
 * Created by muchbeer on 6/19/2015.
 */
public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolderSokoni> {


    private final LayoutInflater layoutInflater;
    private final VolleySingleton volleySingleTon;
    private final ImageLoader imageLoader;
    private int SHARE_CODE = 1;
    int count;
    private Filter planetFilter;

    private ArrayList<Soko> planetList;
    public ArrayList<Soko> origPlanetList = new ArrayList<>();
    private ArrayList<Soko> slistSokoni = new ArrayList<>();
    private static Context context;
    private static ClickListener clickListener;
    private static ClickListenerSearch clickListenerSearch;
    private String URL_FOR_NULL = "http://sokouhuru.com/image/sokohuru.png";
    private CharSequence SOKOHURU = "sokouhuru";


    public AdapterSearch(Context context,  ArrayList<Soko> product_details) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleTon = VolleySingleton.getsInstance();
        imageLoader = volleySingleTon.getImageLoader();

        this.slistSokoni=product_details;
        this.count= product_details.size();
        // L.t(context, "message");
    }

    public void setSokoList(ArrayList<Soko> listSokoni) {
        this.slistSokoni = listSokoni;
        notifyItemRangeChanged(0, listSokoni.size());
        this.origPlanetList = listSokoni;
    }

    public void animateTo(List<Soko> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        //  setClickListener.
    }

    private void applyAndAnimateRemovals(List<Soko> newModels) {
        int batchCount = 0; // continuous # of items that are being removed
        for (int i = slistSokoni.size() - 1; i >= 0; i--) {
            final Soko model = slistSokoni.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
                batchCount ++;
            } else if (batchCount != 0) { // dispatch batch
                notifyItemRangeRemoved(i + 1, batchCount);
                batchCount = 0;
            }
        }

        // notify for remaining
        if (batchCount != 0) { // dispatch remaining
            notifyItemRangeRemoved(0, batchCount);
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
            final int fromPosition = slistSokoni.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);

                SokoHuruFragment mainFragmentActivity = new SokoHuruFragment();
                mainFragmentActivity.returnPosition(fromPosition);

            }
        }
    }

    public Soko removeItem(int position) {
        final Soko model = slistSokoni.remove(position);
        notifyItemRangeRemoved(0, position);
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

        holder.sokoTitle.setText(currentItem.getName());
        holder.sokoGenre.setText(currentItem.getPrice());
        holder.sokoLocation.setText(currentItem.getLocation());
        holder.sokoContact.setText(currentItem.getContact());



        //Soko Huru detail

        String urlThumbnail = currentItem.getImage();
        String urlThumbnailReplacement = URL_FOR_NULL;
        if(urlThumbnail.length() > 10) {
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
        else {
            // img.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.chen_gong));

            imageLoader.get(urlThumbnailReplacement, new ImageLoader.ImageListener() {
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


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolderSokoni extends RecyclerView.ViewHolder implements View.OnClickListener{

        private static final int SHARING_CODE = 1;
        public ImageView sokoThumbnail;
        public TextView sokoTitle;
        public TextView sokoGenre;
        public TextView  sokoLocation;
        public TextView sokoContact;
        //  private ClickListener clickListener;


        public ViewHolderSokoni(View itemView) {
            super(itemView);

            sokoThumbnail = (ImageView) itemView.findViewById(R.id.sokoThumbnail);
            sokoTitle = (TextView) itemView.findViewById(R.id.sokoTitle);
            sokoGenre = (TextView) itemView.findViewById(R.id.sokoRating);
            sokoLocation = (TextView) itemView.findViewById(R.id.sokoLocation);
            sokoContact = (TextView) itemView.findViewById(R.id.sokoContact);



            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            //  Intent startIntent = new Intent(context, MainActivityDetail.class);

            //  ((MainActivity)context).startActivityForResult(i, SHARING_CODE );

            if(clickListener!=null) {
                clickListener.itemClicked(view, getPosition());

            } if(clickListenerSearch !=null) {


                clickListenerSearch.itemClicked2(view,  getPosition());
            }
        }


    }
    public interface ClickListener{

        public void itemClicked(View view, int position);
    }

    public interface ClickListenerSearch {
        public void itemClicked2(View view, int positionSearch);
    }

    public ArrayList<Soko> filter(ArrayList<Soko> models, String query) {
        // query = query.toLowerCase();

        query = query.toLowerCase(Locale.getDefault());
        slistSokoni.clear();
        ArrayList<Soko> filteredModelList = new ArrayList<>();
        //    ArrayList<Soko> filteredModelList = new ArrayList<>();
        if (query.length() == 0) {
            slistSokoni.addAll(models);
        } else {
            for (Soko model : models) {
                if (model.getTitle().toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        notifyDataSetChanged();

        return filteredModelList;
    }

}
