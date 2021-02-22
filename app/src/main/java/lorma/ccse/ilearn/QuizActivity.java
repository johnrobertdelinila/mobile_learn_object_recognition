package lorma.ccse.ilearn;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import lorma.ccse.ilearn.Object.Category;
import lorma.ccse.ilearn.Object.Thing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    public static Category currentCategory;
    public static boolean isQuizFinish = false;
    ArrayList<Thing> things;
    private ImageView mainPicture;
    private RelativeLayout relativeLayout;
    private RadioButton answer1;
    private RadioButton answer2;
    private RadioButton answer3;
    private RadioButton answer4;
    private RadioButton answer5;
    private Thing thingAnswer;
    private TextView questionTextView, level;
    private TextView scoreTextView;
    private MediaPlayer mediaPlayer;

    public static int score = 0;
    private int questionNumber = 1, levelNumber = 1;

    // if this variable is set, the touch events are not processed (the UI is blocked)
    private boolean stopUserInteractions;
    private Boolean isHaveAnotherChance = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (currentCategory == null) {
            this.finish();
        }
        score = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        things = currentCategory.getListOfThings();
        relativeLayout = findViewById(R.id.quizLayout);
        mainPicture = findViewById(R.id.quizImage);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        answer5 = findViewById(R.id.answer5);
        scoreTextView = findViewById(R.id.scoreCounter);
        level = findViewById(R.id.text_level);
        questionTextView = findViewById(R.id.questionCounter);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);

        updateResources();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // it won't process the touch events if this variable is set to true
        if (stopUserInteractions) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    protected void updateResources() {

        // if the quiz has just started
        if (questionNumber == 1 && levelNumber == 1) {
            scoreTextView.setText("Score: " + 0);
        } else if (questionNumber > 10) {
            questionNumber = 1;
            levelNumber++;
            if (levelNumber > 3) {
                isQuizFinish = true;
                /*Highscores.open(this);
                if (Highscores.setHighscore(currentCategory.columnName, score)) {
                    Toasty.success(this, "New Highscore!", Toast.LENGTH_LONG, true).show();
                }else {
                    Toasty.success(this, "Well Done!", Toast.LENGTH_LONG, true).show();
                }
                Highscores.close();*/

                this.finish();
                return;
            }else {
                level.setText("Level " + levelNumber);
                scoreTextView.setText("Score: " + score);
                if (levelNumber == 2) {
                    answer4.setVisibility(View.VISIBLE);
                }if (levelNumber == 3) {
                    answer5.setVisibility(View.VISIBLE);
                }
            }

        }
        questionTextView.setText("Question: " + questionNumber);
        questionNumber++;
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme(); //gets the current Theme

        //merr vleren e atributit background - fillimisht duhet te deklarohet ne attrs.xml
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int primaryLightColor = typedValue.data;

        Random r = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < (3 + (levelNumber - 1))) {
            set.add(r.nextInt(things.size()));
        }
        // three random Thing indexes. e.g., [2, 6, 9] which may represent:
        // [Apple, Mango, Pear]
        Integer[] answers = set.toArray(new Integer[set.size()]);
        // indexes [0, 1 , 2] to randomly fetch the Thing indexes:
        // something like: [0-Apple, 1-Mango and 2-Pear]
        ArrayList<Integer> indexes;
        if (levelNumber == 1) {
            indexes = new ArrayList<>(Arrays.asList(0, 1, 2));
        }else if (levelNumber == 2) {
            indexes = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        }else {
            indexes = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        }
        //a random index to set the picture question:
        // e.g., 1-Mango
        int index = indexes.get(r.nextInt(indexes.size()));
        thingAnswer = things.get(answers[index]);

        mainPicture.setVisibility(View.INVISIBLE);
        mainPicture.setImageResource(thingAnswer.getImage());
        mainPicture.setVisibility(View.VISIBLE);

        setRandomAnswer(answer1, indexes, answers);
        setRandomAnswer(answer2, indexes, answers);
        setRandomAnswer(answer3, indexes, answers);
        if (levelNumber > 1) {
            answer4.setOnClickListener(this);
            setRandomAnswer(answer4, indexes, answers);
        }
        if (levelNumber > 2) {
            answer5.setOnClickListener(this);
            setRandomAnswer(answer5, indexes, answers);
        }
    }

    private void setRandomAnswer(RadioButton button, ArrayList<Integer> indexes, Integer[] answers) {
        // params:
        // e.g., indexes = [0, 1, 2]
        // e.g., answers [2, 6, 9]
        Random r = new Random();
        // random index from [0, 1, 2]. e.g., 1-Mango
        int index = indexes.get(r.nextInt(indexes.size()));
        // e.g., remove the index 1 so Mango won't appear two times as answer
        indexes.remove(Integer.valueOf(index));
        // indexes = [0, 2]
        button.setText(things.get(answers[index]).getText());
    }


    @Override
    public void onClick(final View v) {
        if (v instanceof RadioButton) {
            if (((RadioButton) v).getText() == thingAnswer.getText()) {
                score++;
                scoreTextView.setText("Score: " + score);
                playSound(true);
                showAlertDialog(R.layout.dialog_positive_layout);
                showNextQuestion(v);
            } else {
                playSound(false);
                showAlertDialog(R.layout.dialog_negative_layout);
                if (isHaveAnotherChance) {
                    isHaveAnotherChance = false;
                }else {
                    showNextQuestion(v);
                }
            }
        }
    }

    private void showAlertDialog(int layout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QuizActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        MaterialButton dialogButton = layoutView.findViewById(R.id.btnDialog);
        if (!isHaveAnotherChance) {
            dialogButton.setText("CONTINUE");
        }
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(view -> alertDialog.dismiss());
    }

    private void showNextQuestion(View v) {
        // block UI so the user is not able to select answer before the new question appears
        stopUserInteractions = true;
        Handler handler = new Handler();
        // wait 2 seconds before going to the next question
        handler.postDelayed(() -> {
            updateResources();
            isHaveAnotherChance = true;
            if (v instanceof RadioButton)
                ((RadioButton) v).setChecked(false);
            stopUserInteractions = false; // enable UI again after the next question is displayed
        }, 1500);
    }

    /**
     * Plays a random sound effect
     *
     * @param isCorrect true if the answer is correct, false otherwise
     */
    private void playSound(boolean isCorrect) {
        mediaPlayer = MediaPlayer.create(this,
                isCorrect ? randomCorrectSound() : randomWrongSound());
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(player -> player.reset());
    }

    /**
     * Populates a list with sound effects for correct answers and chooses
     * one of them randomly
     *
     * @return a randomly chosen sound effect from the list
     */
    private int randomCorrectSound() {
        List<Integer> correctSounds = new ArrayList<>();
        correctSounds.add(R.raw.correct1_good_job);
        correctSounds.add(R.raw.correct2_well_done);
        correctSounds.add(R.raw.correct3_perfect);
        correctSounds.add(R.raw.correct4_amazing);
        correctSounds.add(R.raw.correct5_great);
        Random rand = new Random();

        return correctSounds.get(rand.nextInt(correctSounds.size()));
    }

    /**
     * Populates a list with sound effects for wrong answers and chooses
     * one of them randomly
     *
     * @return a randomly chosen sound effect from the list
     */
    private int randomWrongSound() {
        List<Integer> wrongSounds = new ArrayList<>();
        if (isHaveAnotherChance) {
            wrongSounds.add(R.raw.wrong2_try_again);
        }else {
            wrongSounds.add(R.raw.wrong1_oh_no);
            wrongSounds.add(R.raw.wrong3_wrong);
            wrongSounds.add(R.raw.wrong4_you_need_some_practice);
        }
        Random rand = new Random();
        return wrongSounds.get(rand.nextInt(wrongSounds.size()));
    }

    /**
     * Releases the media player (if is not null) and sets it to null
     */
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
