package lorma.ccse.ilearn;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import lorma.ccse.ilearn.Adapters.AlphabetAdapter;
import lorma.ccse.ilearn.Helper.CenterZoomLayoutManager;
import lorma.ccse.ilearn.Object.AlphabetItem;

import java.util.ArrayList;
import java.util.List;

public class AlphabetsActivity extends AppCompatActivity {

    private List<AlphabetItem> alphabetsList;
    private RecyclerView alphabetRecycler;
    private AlphabetAdapter adapter;
    private CenterZoomLayoutManager centerZoomLayoutManager;
    private AdView adView;

    private Button previous, play, next;
    private int counter = 0;

    private int[] sounds;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alphabets);

        sounds = new int[]{R.raw.p, R.raw.q, R.raw.r, R.raw.s, R.raw.t, R.raw.u, R.raw.v, R.raw.w,
                R.raw.x, R.raw.y, R.raw.z, R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g, R.raw.h, R.raw.i, R.raw.j, R.raw.k,
                R.raw.l, R.raw.m, R.raw.n, R.raw.o};

        adView = (AdView) findViewById(R.id.alphabet_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        adView.loadAd(adRequest);

        alphabetsList = new ArrayList<>();
        initList();
        adapter = new AlphabetAdapter(this, alphabetsList);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        alphabetRecycler = (RecyclerView) findViewById(R.id.recycler_alphabets);
        previous = (Button) findViewById(R.id.previous_alphabets);
        play = (Button) findViewById(R.id.play_alphabets);
        next = (Button) findViewById(R.id.next_alphabets);

        alphabetRecycler.setLayoutManager(centerZoomLayoutManager);
        alphabetRecycler.setItemAnimator(new DefaultItemAnimator());
        alphabetRecycler.setAdapter(adapter);

        counter = Integer.MAX_VALUE / 2;

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter--;
                alphabetRecycler.smoothScrollToPosition(counter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                alphabetRecycler.smoothScrollToPosition(counter);
            }
        });

        alphabetRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(alphabetRecycler);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                int pos = counter % alphabetsList.size();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[pos]);
                mediaPlayer.start();
            }
        });
    }

    private void initList() {

        alphabetsList.add(new AlphabetItem("P p"));
        alphabetsList.add(new AlphabetItem("Q q"));
        alphabetsList.add(new AlphabetItem("R r"));
        alphabetsList.add(new AlphabetItem("S s"));
        alphabetsList.add(new AlphabetItem("T t"));
        alphabetsList.add(new AlphabetItem("U u"));
        alphabetsList.add(new AlphabetItem("V v"));
        alphabetsList.add(new AlphabetItem("W w"));
        alphabetsList.add(new AlphabetItem("X x"));
        alphabetsList.add(new AlphabetItem("Y y"));
        alphabetsList.add(new AlphabetItem("Z z"));
        alphabetsList.add(new AlphabetItem("A a"));
        alphabetsList.add(new AlphabetItem("B b"));
        alphabetsList.add(new AlphabetItem("C c"));
        alphabetsList.add(new AlphabetItem("D d"));
        alphabetsList.add(new AlphabetItem("E e"));
        alphabetsList.add(new AlphabetItem("F f"));
        alphabetsList.add(new AlphabetItem("G g"));
        alphabetsList.add(new AlphabetItem("H h"));
        alphabetsList.add(new AlphabetItem("I i"));
        alphabetsList.add(new AlphabetItem("J j"));
        alphabetsList.add(new AlphabetItem("K k"));
        alphabetsList.add(new AlphabetItem("L l"));
        alphabetsList.add(new AlphabetItem("M m"));
        alphabetsList.add(new AlphabetItem("N n"));
        alphabetsList.add(new AlphabetItem("O o"));
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
