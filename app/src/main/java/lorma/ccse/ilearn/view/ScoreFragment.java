package lorma.ccse.ilearn.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import lorma.ccse.ilearn.R;

public class ScoreFragment extends Fragment{

    FloatingActionButton buttonBack;
    TextView textViewCurrentScore;
    TextView textViewBestScore;
    TextView textViewMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score, container, false);
        findViewsById(rootView);
        buttonBack = rootView.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> DrawingActivity.flipCard());
        // Setting current score
        textViewCurrentScore.setText(String.valueOf(DrawingActivity.currentScore));
        // Setting best score
        textViewBestScore.setText(String.valueOf(DrawingActivity.shape.getMaxScore()));
        // Setting the message based on the score
        textViewMessage.setText(getMessage(DrawingActivity.currentScore));
        return rootView;
    }

    private void findViewsById(View view){
        textViewBestScore = view.findViewById(R.id.textViewBestScore);
        textViewCurrentScore = view.findViewById(R.id.textViewYourScore);
        textViewMessage = view.findViewById(R.id.textViewMessage);
    }

    private String getMessage(float score){
        if (score == 100){
            return "You are awesome! Enough said";
        }
        else if (score == DrawingActivity.shape.getMaxScore()){
            return "High Score";
        }
        else if (score >= 90){
            return "Great Job! Are you related to picasso?";
        }
        else if (score < 90 && score >= 80){
            return "Great effort! I knew you can do it";
        }
        else if (score < 80 && score >= 60){
            return "Almost there! Keep trying.";
        }
        return "You can do better. I know.";
    }
}
