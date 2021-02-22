package lorma.ccse.ilearn.Object;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Quiz {
    private int score;
    private String imei;
    private @ServerTimestamp Date timestamp;

    public Quiz(){}

    public Quiz(int score, String imei, Date timestamp) {
        this.score = score;
        this.imei = imei;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
