package sokohuru.muchbeer.king.sokohurutab.adapters;

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
public class AdapterSoko extends RecyclerView.Adapter<AdapterSoko.ViewHolderSokoni> {


    private final LayoutInflater layoutInflater;
    private final VolleySingleton volleySingleTon;
    private final ImageLoader imageLoader;
    private int SHARE_CODE = 1;

    private Filter planetFilter;

    private ArrayList<Soko> planetList;
    private ArrayList<Soko> origPlanetList = new ArrayList<>();
    private ArrayList<Soko> slistSokoni = new ArrayList<>();
    private static Context context;
    private static ClickListener clickListener;


    public AdapterSoko(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleTon = VolleySingleton.getsInstance();
        imageLoader = volleySingleTon.getImageLoader();

       // L.t(context, "message");
    }

    public void setSokoList(ArrayList<Soko> listSokoni) {
        this.slistSokoni = listSokoni;
        notifyItemRangeChanged(0, listSokoni.size());
        this.origPlanetList = listSokoni;
    }

    public void animateTo(ArrayList<Soko> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(ArrayList<Soko> newModels) {
        for (int i = slistSokoni.size() - 1; i >= 0; i--) {
            final Soko model = slistSokoni.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }



    private void applyAndAnimateAdditions(ArrayList<Soko> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Soko model = newModels.get(i);
            if (!slistSokoni.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Soko> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Soko model = newModels.get(toPosition);
            final int fromPosition = slistSokoni.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
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
    }

    @Override
    public AdapterSoko.ViewHolderSokoni onCreateViewHolder(ViewGroup parent, int viewType) {

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

    @Override
    public int getItemCount() {
        return slistSokoni.size();
    }


        public static class ViewHolderSokoni extends RecyclerView.ViewHolder implements View.OnClickListener{

       private static final int SHARING_CODE = 1;
       public ImageView sokoThumbnail;
        public TextView sokoTitle;
        public TextView sokoGenre;
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
       }


   }
public interface ClickListener{

    public void itemClicked(View view, int position);
}

}
