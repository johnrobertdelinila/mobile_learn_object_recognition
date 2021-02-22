package lorma.ccse.ilearn.Object;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Kid {
    private String name;
    private @ServerTimestamp Date timestamp;

    public Kid() {}

    public Kid(String name, Date timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
