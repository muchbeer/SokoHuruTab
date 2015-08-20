package king.muchbeer.sokohuru.searchope;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import king.muchbeer.sokohuru.R;
import king.muchbeer.sokohuru.Sokoni.Soko;
import king.muchbeer.sokohuru.network.VolleySingleton;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private final LayoutInflater mInflater;
    private ArrayList<Soko> mModels;
    private final VolleySingleton volleySingleTon;
    private static ClickListener clickListener;

    private final ImageLoader imageLoader;
    private static final int SHARING_CODE = 1;
    private String URL_FOR_NULL = "http://sokouhuru.com/image/sokohuru.png";

    public ExampleAdapter(Context context, ArrayList<Soko> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        volleySingleTon = VolleySingleton.getsInstance();
        imageLoader = volleySingleTon.getImageLoader();

    }

    public void setSokoList(ArrayList<Soko> listSokoi) {
        this.mModels = listSokoi;
        notifyItemRangeChanged(0, listSokoi.size());
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_soko_lookfeel, parent, false);

        ExampleViewHolder viewHolderSokoi = new ExampleViewHolder(itemView);


        return viewHolderSokoi;
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, int position) {


        Soko currentItem = mModels.get(position);

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


    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<Soko> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Soko> newModels) {
        int batchCount = 0; // continuous # of items that are being removed
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Soko model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
                batchCount ++;
            }else if (batchCount != 0) { // dispatch batch
                notifyItemRangeRemoved(i + 1, batchCount);
                batchCount = 0;
            }
        }
        // notify for remaining
        if (batchCount != 0) { // dispatch remaining
            notifyItemRangeRemoved(0, batchCount);
        }
    }

    private void applyAndAnimateAdditions(List<Soko> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Soko model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Soko> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Soko model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Soko removeItem(int position) {
        final Soko model = mModels.remove(position);
        notifyItemRangeRemoved(0, position);
        return model;
    }

    public void addItem(int position, Soko model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Soko model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }


    public static  class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //  private final TextView tvText;
        public ImageView sokoThumbnail;
        public TextView sokoTitle;
        public TextView sokoGenre;
        public TextView  sokoLocation;
        public TextView sokoContact;
        private ImageLoader imageLoader;
        private String URL_FOR_NULL = "http://sokouhuru.com/image/sokohuru.png";


        public ExampleViewHolder(View itemView) {
            super(itemView);

            // tvText = (TextView) itemView.findViewById(R.id.tvText);

            sokoThumbnail = (ImageView) itemView.findViewById(R.id.sokoThumbnail);
            sokoTitle = (TextView) itemView.findViewById(R.id.sokoTitle);
            sokoGenre = (TextView) itemView.findViewById(R.id.sokoRating);
            sokoLocation = (TextView) itemView.findViewById(R.id.sokoLocation);
            sokoContact = (TextView) itemView.findViewById(R.id.sokoContact);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

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
