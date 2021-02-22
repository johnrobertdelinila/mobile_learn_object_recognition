package lorma.ccse.ilearn.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lorma.ccse.ilearn.R;

/**
 * Created by Hemant on 10/9/2017.
 */

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorViewHolder> {
    private Context context;
    private List<Integer> colorsList = new ArrayList<>();
    private List<String> colorNames = new ArrayList<>();
    private List<Integer> newColorsList = new ArrayList<>();
    private List<String> newColorNames = new ArrayList<>();
    private Typeface jellyCrazies;

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private LinearLayout linearLayout;

        public ColorViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.alphabet_text);
            linearLayout = itemView.findViewById(R.id.colors_layout);
        }
    }

    public ColorsAdapter(Context context, List<Integer> indexArr) {
        this.context = context;
        int[] colors = context.getResources().getIntArray(R.array.colors);
        for (int i = 0; i < colors.length; i++) {
            colorsList.add(colors[i]);
        }
        colorNames = Arrays.asList(context.getResources().getStringArray(R.array.colorNames));
        for (Integer index: indexArr) {
            newColorNames.add(colorNames.get(index));
            newColorsList.add(colorsList.get(index));
        }
        jellyCrazies = Typeface.createFromAsset(context.getAssets(), "fonts/jelly_crazies.ttf");
    }

    @Override
    public ColorsAdapter.ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alphabet_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorsAdapter.ColorViewHolder holder, int position) {
        int pos = position % newColorNames.size();

        holder.textView.setText(newColorNames.get(pos));
        holder.textView.setTextSize(18);

        if (newColorNames.get(pos).equals("WHITE")) {
            holder.textView.setTextColor(Color.BLACK);
        } else {
            holder.textView.setTextColor(Color.WHITE);
        }
        holder.linearLayout.setBackgroundColor(newColorsList.get(pos));
        holder.textView.setTypeface(jellyCrazies);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


}
