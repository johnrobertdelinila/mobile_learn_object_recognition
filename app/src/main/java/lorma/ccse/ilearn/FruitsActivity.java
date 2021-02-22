package lorma.ccse.ilearn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import lorma.ccse.ilearn.Adapters.ImageAdapter;
import lorma.ccse.ilearn.Helper.CenterZoomLayoutManager;
import lorma.ccse.ilearn.Object.Category;
import lorma.ccse.ilearn.Object.ImageItem;
import lorma.ccse.ilearn.Object.Quiz;
import lorma.ccse.ilearn.Object.Thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class FruitsActivity extends AppCompatActivity {

    private RecyclerView fruitsRecycler;
    private List<ImageItem> imageItemList;
    private ImageAdapter adapter;

    private AdView adView;

    private CenterZoomLayoutManager centerZoomLayoutManager;

    private Button previous, play, next;
    private int counter = 0;

    private MediaPlayer mediaPlayer;
    private int[] sounds;
    private List<Integer> indexArr;
    private List<ImageItem> newImageItemList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quiz_scores = db.collection("quiz_scores");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fruits);

        sounds = new int[]{R.raw.banana, R.raw.cherry, R.raw.coconut, R.raw.fig, R.raw.grape, R.raw.green_apple, R.raw.kiwi,
                R.raw.papaya, R.raw.peach, R.raw.pineapple, R.raw.pomegranate, R.raw.strawberry, R.raw.watermelon};

        adView = (AdView) findViewById(R.id.animals_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        adView.loadAd(adRequest);
        imageItemList = new ArrayList<>();
        initList();
        adapter = new ImageAdapter(this, newImageItemList);

        fruitsRecycler = (RecyclerView) findViewById(R.id.recycler_fruits);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        fruitsRecycler.setLayoutManager(centerZoomLayoutManager);
        fruitsRecycler.setItemAnimator(new DefaultItemAnimator());
        fruitsRecycler.setAdapter(adapter);

        previous = (Button) findViewById(R.id.previous_fruits);
        play = (Button) findViewById(R.id.play_fruits);
        next = (Button) findViewById(R.id.next_fruits);

        counter = Integer.MAX_VALUE / 2;

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter--;
                fruitsRecycler.smoothScrollToPosition(counter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                fruitsRecycler.smoothScrollToPosition(counter);
            }
        });

        fruitsRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(fruitsRecycler);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                int pos = counter % imageItemList.size();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[indexArr.get(pos)]);
                mediaPlayer.start();
            }
        });

        ((TextView) findViewById(R.id.text_quiz)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jelly_crazies.ttf"));
    }

    private void initList() {
        imageItemList.add(new ImageItem("Banana", R.drawable.banana));
        imageItemList.add(new ImageItem("Cherry", R.drawable.cherry));
        imageItemList.add(new ImageItem("Coconut", R.drawable.coconut));
        imageItemList.add(new ImageItem("Fig", R.drawable.dragon));
        imageItemList.add(new ImageItem("Grapes", R.drawable.grape));
        imageItemList.add(new ImageItem("Green Apple", R.drawable.greenapple));
        imageItemList.add(new ImageItem("Kiwi", R.drawable.kiwi));
        imageItemList.add(new ImageItem("Papaya", R.drawable.papaya));
        imageItemList.add(new ImageItem("Peach", R.drawable.peach));
        imageItemList.add(new ImageItem("Pineapple", R.drawable.pine));
        imageItemList.add(new ImageItem("Pomegranate", R.drawable.pomegranate));
        imageItemList.add(new ImageItem("Strawberry", R.drawable.strawberry));
        imageItemList.add(new ImageItem("Watermelon", R.drawable.watermelon));

        Category category = new Category("Fruits",
                R.drawable.tiger,
                0,
                R.color.lime,
                R.style.AppTheme3,
                "COLUMN_FRUITS",
                R.drawable.love);

        int i = 0;
        indexArr = new ArrayList<>();
        for (ImageItem imageItem: imageItemList) {
            indexArr.add(i); i++;
        }
        Collections.shuffle(indexArr);
        for (Integer index: indexArr) {
            category.addThing(new Thing(imageItemList.get(index).getImage(), R.raw.tiger, imageItemList.get(index).getName(), R.raw.tiger));
            newImageItemList.add(imageItemList.get(index));
        }
        QuizActivity.currentCategory = category;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (QuizActivity.isQuizFinish) {
            Toasty.success(this, "Well Done!", Toasty.LENGTH_LONG, true).show();
            // Getting the High and Last Score
            quiz_scores.whereEqualTo("imei", LandingActivity.IMEI).orderBy("timestamp", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.getException() != null) {
                            Toasty.error(FruitsActivity.this, task1.getException().getMessage());
                            return;
                        }
                        QuerySnapshot querySnapshot = task1.getResult();
                        if (querySnapshot == null) {
                            Toasty.error(FruitsActivity.this, "Something went wrong");
                            return;
                        }

                        Quiz quiz = new Quiz(QuizActivity.score, LandingActivity.IMEI, null);
                        quiz_scores.add(quiz);

                        int last_score = 0;
                        int highest_score = 0;
                        if (querySnapshot.size() > 0) {
                            for (DocumentSnapshot snapshot : querySnapshot) {
                                Quiz oldQuiz = snapshot.toObject(Quiz.class);
                                if (oldQuiz != null && oldQuiz.getScore() > highest_score) {
                                    highest_score = oldQuiz.getScore();
                                }
                            }
                            if (querySnapshot.getDocuments().get(0) != null) {
                                Quiz oldQuiz = querySnapshot.getDocuments().get(0).toObject(Quiz.class);
                                if (oldQuiz != null) {
                                    last_score = oldQuiz.getScore();
                                }
                            }
                        } else {
                            highest_score = QuizActivity.score;
                        }

                        if (QuizActivity.score > highest_score) {
                            // New High Score
                            Toasty.success(this, "NEW HIGH SCORE!", 5000, true).show();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(FruitsActivity.this);
                        builder.setTitle("Scores");
                        String stringBuilder = "HIGHEST SCORE: " + highest_score + "\n\n" +
                                "LAST SCORE: " + last_score + "\n\n" +
                                "SCORE: " + QuizActivity.score;
                        View view = getLayoutInflater().inflate(R.layout.emphasized_layout, null);
                        TextView textView = view.findViewById(R.id.textView);
                        textView.setText(stringBuilder);
                        textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jelly_crazies.ttf"));
                        builder.setView(view);
                        builder.setNegativeButton("OKAY", (dialog, which) -> dialog.dismiss());
                        builder.show();
                    });
            final KonfettiView konfettiView = findViewById(R.id.konfettiView);
            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .stream(200, 5000L);
            QuizActivity.isQuizFinish = false;
        }
    }

    public void takeQuiz(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
