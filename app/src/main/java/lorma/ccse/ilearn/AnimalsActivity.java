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

public class AnimalsActivity extends AppCompatActivity {

    private RecyclerView animalRecycler;
    private List<ImageItem> imageItemList;
    private ImageAdapter adapter;

    private CenterZoomLayoutManager centerZoomLayoutManager;

    private AdView adView;

    private Button previous, play, next;
    private int counter = 0;

    private MediaPlayer mediaPlayer;
    private int[] sounds;
    private List<Integer> indexArr;
    private List<ImageItem> newImageItemList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quiz_scores = db.collection("quiz_scores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animals);

        adView = findViewById(R.id.animals_ad);
        imageItemList = new ArrayList<>();
        initList();
        adapter = new ImageAdapter(this, newImageItemList);
        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        adView.loadAd(adRequest);

        animalRecycler = findViewById(R.id.recycler_animals);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        animalRecycler.setLayoutManager(centerZoomLayoutManager);
        animalRecycler.setItemAnimator(new DefaultItemAnimator());
        animalRecycler.setAdapter(adapter);

        previous = findViewById(R.id.previous_animals);
        play = findViewById(R.id.play_animals);
        next = findViewById(R.id.next_animals);

        counter = Integer.MAX_VALUE / 2;

        previous.setOnClickListener(view -> {
            counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
            counter--;
            animalRecycler.smoothScrollToPosition(counter);
        });

        next.setOnClickListener(view -> {
            counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
            counter++;
            animalRecycler.smoothScrollToPosition(counter);
        });

        animalRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(animalRecycler);

        play.setOnClickListener(view -> {
            counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
            int pos = counter % imageItemList.size();
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[indexArr.get(pos)]);
            mediaPlayer.start();
        });

        ((TextView) findViewById(R.id.text_quiz)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jelly_crazies.ttf"));
    }

    private void initList() {
        sounds = new int[]{R.raw.alligator, R.raw.bear, R.raw.elephant, R.raw.lion, R.raw.monkey, R.raw.panda,
                R.raw.rabbit, R.raw.snake, R.raw.squirrel, R.raw.tiger, R.raw.zebra};

        imageItemList.add(new ImageItem("Alligator", R.drawable.alligator));
        imageItemList.add(new ImageItem("Bear", R.drawable.bear));
        imageItemList.add(new ImageItem("Elephant", R.drawable.elephant));
        imageItemList.add(new ImageItem("Lion", R.drawable.lion));
        imageItemList.add(new ImageItem("Monkey", R.drawable.monkey));
        imageItemList.add(new ImageItem("Panda", R.drawable.panda));
        imageItemList.add(new ImageItem("Rabbit", R.drawable.rabbit));
        imageItemList.add(new ImageItem("Snake", R.drawable.snake));
        imageItemList.add(new ImageItem("Squirrel", R.drawable.squirrel));
        imageItemList.add(new ImageItem("Tiger", R.drawable.tiger));
        imageItemList.add(new ImageItem("Zebra", R.drawable.zebra));

        Category category = new Category("Animals",
                R.drawable.tiger,
               0,
                R.color.lime,
                R.style.AppTheme3,
                "COLUMN_ANIMALS",
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

    public void takeQuiz(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
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
        if (QuizActivity.isQuizFinish) {
            Toasty.success(this, "Well Done!", Toasty.LENGTH_LONG, true).show();
            // Getting the High and Last Score
            quiz_scores.whereEqualTo("imei", LandingActivity.IMEI).orderBy("timestamp", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.getException() != null) {
                            Toasty.error(AnimalsActivity.this, task1.getException().getMessage());
                            return;
                        }
                        QuerySnapshot querySnapshot = task1.getResult();
                        if (querySnapshot == null) {
                            Toasty.error(AnimalsActivity.this, "Something went wrong");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalsActivity.this);
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

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
