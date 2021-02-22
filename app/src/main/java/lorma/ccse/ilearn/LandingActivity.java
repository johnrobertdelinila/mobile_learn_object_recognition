package lorma.ccse.ilearn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import lorma.ccse.ilearn.Adapters.CategoryAdapter;
import lorma.ccse.ilearn.Object.CategoryItem;
import lorma.ccse.ilearn.Object.Kid;
import lorma.ccse.ilearn.cameraActivity.ClassifierActivity;
import lorma.ccse.ilearn.view.MainShapeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class LandingActivity extends AppCompatActivity {

    private RecyclerView categoryRecycler;
    private List<CategoryItem> categoryItemList;
    private CategoryAdapter categoryAdapter;

    private TextView learnToday;
    private ImageView share, rate;

    private SharedPreferences sharedPreferences;
    int appOpened = 1;

    public static boolean isCongratulations = false;
    public static List<String> feedbacks = new ArrayList<>();

    public static String IMEI;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kid_names = db.collection("kid_names");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing);

        MobileAds.initialize(this, getString(R.string.ad_id));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        appOpened = sharedPreferences.getInt("appOpened", 0);
        appOpened++;
        editor.putInt("appOpened", appOpened);
        editor.apply();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/poppins.ttf");
        learnToday = findViewById(R.id.choose_landing);
        learnToday.setTypeface(typeface);

        categoryItemList = new ArrayList<>();
        initList();
        categoryAdapter = new CategoryAdapter(this, categoryItemList);

        categoryRecycler = findViewById(R.id.recycler_landing);
        RecyclerView.LayoutManager catLayMan = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(catLayMan);
        categoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryRecycler.setAdapter(categoryAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(categoryRecycler);

        share = findViewById(R.id.share_app);
        rate = findViewById(R.id.rate_app);
        share.setOnClickListener(v -> {
            FeedbackDialog dialog = new FeedbackDialog(LandingActivity.this);
            dialog.show();
        });
        share.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity.this);
            builder.setTitle("Feedbacks");
            StringBuilder stringBuilder = new StringBuilder();
            for (String feedback : feedbacks) {
                stringBuilder.append(feedback);
                stringBuilder.append("\n");
            }
            builder.setMessage(stringBuilder.toString());
            builder.setNegativeButton("DONE", (dialog, which) -> dialog.dismiss());
            builder.show();
            return false;
        });
        rate.setOnClickListener(v -> {
            ProfileDialog dialog = new ProfileDialog(LandingActivity.this, learnToday);
            dialog.show();
        });

        ((MaterialButton) findViewById(R.id.find_object)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jelly_crazies.ttf"));
        ((MaterialButton) findViewById(R.id.practice_shape)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jelly_crazies.ttf"));

        fetchImei();

        // We load a drawable and create a location to show a tap target here
        // We need the display to get the width and height at this point in time

    }

    private void getKidName(String imei) {
        kid_names.document(imei).get()
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        return;
                    }
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Kid kid = documentSnapshot.toObject(Kid.class);
                        if (kid != null) {
                            String name = kid.getName();
                            String newText = "WHAT WOULD YOU LIKE TO LEARN TODAY " + name.toUpperCase() + "?";
                            learnToday.setText(newText);
                        }
                    }
                });

        if(appOpened <= 2) {
            tapTarget();
        }

    }

    private void tapTarget() {
        final SpannableString sassyDesc = new SpannableString("It allows you to practice shapes in a fun way!");
        sassyDesc.setSpan(new StyleSpan(Typeface.ITALIC), sassyDesc.length() - "shapes".length(), sassyDesc.length(), 0);

        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        // This tap target will target the back button, we just need to pass its containing toolbar
                        TapTarget.forView(findViewById(R.id.find_object), "Try this game to recognize and identify Objects around you.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorAccent)
                                .targetCircleColor(android.R.color.black)
                                .transparentTarget(true)
                                .cancelable(false)
                                .textColor(android.R.color.black),
                        TapTarget.forView(findViewById(R.id.practice_shape), "Practice Shapes", sassyDesc)
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorAccent)
                                .targetCircleColor(android.R.color.black)
                                .cancelable(false)
                                .transparentTarget(true)
                                .textColor(android.R.color.black),
                        TapTarget.forView(findViewById(R.id.share_app), "Feedback and Rating", "Help us improve our app by submitting your feedback and rating here.").cancelable(false),
                        TapTarget.forView(findViewById(R.id.rate_app), "Name of child", "If you want to set up your Child\'s name for personalization.").cancelable(false),
                        TapTarget.forView(findViewById(R.id.disclaimer_app), "This will show Disclaimer", "But you will not read it anyways :(\nOkay start now! Hope you like it.").cancelable(false)
                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        final AlertDialog dialog = new AlertDialog.Builder(LandingActivity.this)
                                .setTitle("Uh oh")
                                .setMessage("You canceled the sequence")
                                .setPositiveButton("Oops", null).show();
                        TapTargetView.showFor(dialog,
                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                        .cancelable(false)
                                        .tintTarget(false), new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

        // You don't always need a sequence, and for that there's a single time tap target
        final SpannableString spannedDesc = new SpannableString("You can choose one of the Categories to enjoy and learn new things! There is also a Quiz to improve more.");
        spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "Quiz".length(), spannedDesc.length(), 0);

        final Display display = getWindowManager().getDefaultDisplay();
        Drawable droid = ContextCompat.getDrawable(this, R.drawable.ic_baseline_add);
        Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
        // Using deprecated methods makes you look way cool
        droidTarget.offset((display.getWidth() / 2) - 120, (display.getHeight() / 2) - 30);
        TapTargetView.showFor(this, TapTarget.forBounds(droidTarget, "Tap Here!", spannedDesc)
                .cancelable(false)
                .drawShadow(true)
                .outerCircleAlpha(0.80f)
                .titleTextDimen(R.dimen.title_text_size)
                .targetRadius(60)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                // .. which evidently starts the sequence we defined earlier
                sequence.start();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    private void initList() {
        categoryItemList.add(new CategoryItem("Alphabets", R.drawable.alphabet_a));
        categoryItemList.add(new CategoryItem("Animals", R.drawable.tiger));
        categoryItemList.add(new CategoryItem("Objects", R.drawable.desk_chair));
        categoryItemList.add(new CategoryItem("Fruits", R.drawable.grape));
//        categoryItemList.add(new CategoryItem("Vegetables", R.drawable.cauli));
//        categoryItemList.add(new CategoryItem("Vehicles", R.drawable.aeroplane));
        categoryItemList.add(new CategoryItem("Shapes", R.drawable.pyramid));
        categoryItemList.add(new CategoryItem("Colors", R.drawable.colors_bg));
//        categoryItemList.add(new CategoryItem("Spellings", R.drawable.spelling_main));
    }

    public void findObject(View view) {
        startActivity(new Intent(LandingActivity.this, ClassifierActivity.class));
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 3000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please turn on the permission request for IMEI in the settings.", Toast.LENGTH_SHORT).show();
                    return;
                }
                IMEI = telephonyManager.getDeviceId();
                if (IMEI == null) {
                    Toast.makeText(this, "Can't get your phone's IMEI", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    getKidName(IMEI);
                }
            }else {
                Toast.makeText(this, "Please turn on the permission request for IMEI in the settings.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint("HardwareIds")
    private void fetchImei() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_PHONE_STATE
            }, 3000);
        }else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please turn on the permission request for IMEI in the settings.", Toast.LENGTH_SHORT).show();
                return;
            }
            IMEI = telephonyManager.getDeviceId();
            if (IMEI == null) {
                Toast.makeText(this, "Can't get the phone IMEI", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                getKidName(IMEI);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LandingActivity.isCongratulations) {
            LandingActivity.isCongratulations = false;
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
                    .stream(200, 8000L);
            playSound();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LandingActivity.isCongratulations = false;
    }

    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, randomCorrectSound());
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::reset);
    }

    private int randomCorrectSound() {
        List<Integer> correctSounds = new ArrayList<>();
        correctSounds.add(R.raw.correct1_good_job);
        correctSounds.add(R.raw.correct2_well_done);
        correctSounds.add(R.raw.correct4_amazing);
        Random rand = new Random();

        return correctSounds.get(rand.nextInt(correctSounds.size()));
    }

    public void practiceShape(View view) {
        startActivity(new Intent(LandingActivity.this, MainShapeActivity.class));
    }

    public void disclaimer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity.this);
        builder.setTitle("Disclaimer");
        builder.setMessage(getResources().getString(R.string.disclaimer));
        builder.setNegativeButton("OKAY", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /*@Override
    public void onBackPressed() {
        if (appOpened % 10 == 0) {
            FeedbackDialog dialog = new FeedbackDialog(LandingActivity.this);
            dialog.show();
        } else {
            finish();
        }
    }*/
}
