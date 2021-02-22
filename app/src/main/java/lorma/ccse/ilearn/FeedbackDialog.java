package lorma.ccse.ilearn;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import lorma.ccse.ilearn.Object.Feedback;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Akshansh on 11-12-2017.
 */

public class FeedbackDialog extends Dialog {

    public Button yes, no;
    private Context mContext;
    public TextInputLayout textInputLayout;
    public TextInputEditText textInputEditText;
    private MaterialRatingBar ratingBar;

    public FeedbackDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference feedbacks = db.collection("feedbacks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feedback_dialog);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        ratingBar = findViewById(R.id.application_ratingbar);

        textInputLayout= findViewById(R.id.feedback_text_input);
        textInputEditText = findViewById(R.id.feedback_edit_text);

        yes.setOnClickListener(v -> {
            if (yes.getText().toString().equalsIgnoreCase("SURE")) {
                yes.setText("SUBMIT");
                yes.setTypeface(Typeface.DEFAULT_BOLD);
                findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
            }else {
                dismiss();
                if (textInputEditText.getText() != null && textInputEditText.getText().toString().length() > 0) {
                    String review = textInputEditText.getText().toString();
                    Feedback feedback = new Feedback(ratingBar.getRating(), review, LandingActivity.IMEI, null);
                    feedbacks.add(feedback).addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            Toasty.error(mContext, task.getException().getMessage(), Toasty.LENGTH_LONG).show();
                            return;
                        }
                        Toasty.success(mContext, "Thank you for your feedback! We appreciated it.", 3500, true).show();
                    });
                }
            }

        });

        no.setOnClickListener(v -> dismiss());
    }
}