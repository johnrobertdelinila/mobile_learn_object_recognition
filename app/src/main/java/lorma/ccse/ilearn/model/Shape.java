package lorma.ccse.ilearn.model;

import com.orm.SugarRecord;

public class Shape extends SugarRecord<Shape> {

    private String bitmapPath;
    private float maxScore;
    private String level;
    private int resourceId;

    public Shape(){}


    public String getBitmapPath() {
        return bitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        this.bitmapPath = bitmapPath;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
