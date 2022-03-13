import com.google.gson.annotations.SerializedName;

public class LiftRide {

    private int time;
    @SerializedName("liftID")
    private int liftId;
    private int waitTime;

    public LiftRide(int time, int liftId, int waitTime) {
        this.time = time;
        this.liftId = liftId;
        this.waitTime = waitTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLiftId() {
        return liftId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}