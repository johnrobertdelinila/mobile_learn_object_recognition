package lorma.ccse.ilearn;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import lorma.ccse.ilearn.Adapters.ColorsAdapter;
import lorma.ccse.ilearn.Helper.CenterZoomLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColorsActivity extends AppCompatActivity {
    private RecyclerView colorRecycler;
    private ColorsAdapter adapter;
    private CenterZoomLayoutManager centerZoomLayoutManager;

    private AdView adView;

    private Button previous, play, next;
    private int counter = 0;
    private int[] sounds;
    private MediaPlayer mediaPlayer;
    private List<Integer> indexArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_colors);

        adView = (AdView) findViewById(R.id.colors_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        adView.loadAd(adRequest);
        sounds = new int[]{R.raw.red, R.raw.pink, R.raw.purple, R.raw.indigo, R.raw.blue, R.raw.sky_blue, R.raw.cyan, R.raw.teal,
                R.raw.green, R.raw.lime, R.raw.yellow, R.raw.amber, R.raw.orange, R.raw.brown, R.raw.grey, R.raw.black, R.raw.white};

        int i = 0;
        indexArr = new ArrayList<>();
        for (Integer sound: sounds) {
            indexArr.add(i); i++;
        }
        Collections.shuffle(indexArr);

        final int[] colors = getApplicationContext().getResources().getIntArray(R.array.colors);
        adapter = new ColorsAdapter(getApplicationContext(), indexArr);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        colorRecycler = (RecyclerView) findViewById(R.id.recycler_colors);
        previous = (Button) findViewById(R.id.previous_colors);
        play = (Button) findViewById(R.id.play_colors);
        next = (Button) findViewById(R.id.next_colors);

        colorRecycler.setLayoutManager(centerZoomLayoutManager);
        colorRecycler.setItemAnimator(new DefaultItemAnimator());
        colorRecycler.setAdapter(adapter);

        counter = Integer.MAX_VALUE / 2;

        previous.setOnClickListener(view -> {
            counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
            counter--;
            if (counter < 0) {
                counter = colors.length - 1;
                colorRecycler.scrollToPosition(counter);
            }
            colorRecycler.smoothScrollToPosition(counter);
        });

        next.setOnClickListener(view -> {
            counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
            counter++;
            colorRecycler.smoothScrollToPosition(counter);
        });

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(colorRecycler);

        play.setOnClickListener(view -> {
            counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
            int pos = counter % colors.length;
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[indexArr.get(pos)]);
            mediaPlayer.start();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
