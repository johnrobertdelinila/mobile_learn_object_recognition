package lorma.ccse.ilearn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import lorma.ccse.ilearn.Object.Kid;

import es.dmoral.toasty.Toasty;

public class ProfileDialog extends Dialog {

    public Button yes, no;
    private Context mContext;
    private TextView textView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kid_names = db.collection("kid_names");

    public ProfileDialog(@NonNull Context context, TextView textView) {
        super(context);
        mContext = context;
        this.textView = textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_dialog);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);

        TextInputEditText textInputEditText = findViewById(R.id.feedback_edit_text);

        yes.setOnClickListener(v -> {
            if (textInputEditText.getText() != null && textInputEditText.getText().toString().length() > 0) {
                String name = textInputEditText.getText().toString();
                String newText = "WHAT WOULD YOU LIKE TO LEARN TODAY " + name.toUpperCase() + "?";
                textView.setText(newText);
                Kid kid = new Kid(name, null);
                kid_names.document(LandingActivity.IMEI).set(kid).addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        Toasty.error(mContext, task.getException().getMessage(), Toasty.LENGTH_LONG, true).show();
                        return;
                    }
                    Toasty.info(mContext, mContext.getResources().getString(R.string.set_success), 3500, true).show();
                });
                dismiss();
            }
        });

        no.setOnClickListener(v -> dismiss());
    }
}