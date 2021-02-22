package lorma.ccse.ilearn.Object;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Feedback {
    private Float rating;
    private String feeback, imei;
    private @ServerTimestamp Date timestamp;

    public Feedback() {}

    public Feedback(Float rating, String feeback, String imei, Date timestamp) {
        this.rating = rating;
        this.feeback = feeback;
        this.imei = imei;
        this.timestamp = timestamp;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getFeeback() {
        return feeback;
    }

    public void setFeeback(String feeback) {
        this.feeback = feeback;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
