package lorma.ccse.ilearn.Adapters;

import android.content.Context;
import android.graphics.Typeface;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lorma.ccse.ilearn.Object.ImageItem;
import lorma.ccse.ilearn.R;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Akshansh on 30-09-2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<ImageItem> imageItemList;
    private Typeface jellyCrazies;
    private MediaPlayer mediaPlayer;

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private CardView card;

        public ImageViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            image = view.findViewById(R.id.item_image);
            card = view.findViewById(R.id.holder);
        }
    }

    public ImageAdapter(Context context, List<ImageItem> imageItemList) {
        this.context = context;
        this.imageItemList = imageItemList;
        jellyCrazies = Typeface.createFromAsset(context.getAssets(), "fonts/jelly_crazies.ttf");
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learning_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        int pos = position % imageItemList.size();

        ImageItem imageItem = imageItemList.get(pos);
        holder.name.setTypeface(jellyCrazies);
        holder.name.setText(imageItem.getName());
        Picasso.with(context)
                .load(imageItem.getImage())
                .into(holder.image);

        holder.card.setOnClickListener(v -> {
            if (imageItem.getName().equalsIgnoreCase("bear")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.bearnoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("monkey")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.monkeynoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("snake")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.snakenoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("lion")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.lionnoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("tiger")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.tigernoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("elephant")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.elephantnoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("squirrel")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.squirrelnoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("panda")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.pandanoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("rabbit")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.rabbitnoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("alligator")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.crocodilenoise);
                mediaPlayer.start();
            }else if (imageItem.getName().equalsIgnoreCase("zebra")) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.zebranoise);
                mediaPlayer.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}