package part2;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Phase {
    private static int RETRY_TIMES = 5;

    private int numThreads;
    private int numSkier;
    private int numLifts;

    private int startTime;
    private int endTime;

    private int numPost;

    protected AtomicInteger successCalls;
    protected AtomicInteger failCalls;

    private CountDownLatch totalCount;
    private CountDownLatch nextPhaseCount;

    SkiersApi skiersApi;

    public List<List<String>> rows = new ArrayList<>();

    public Phase(int numThreads, int numSkier, int numLifts, int startTime, int endTime,
                 int numPost, AtomicInteger successCalls, AtomicInteger failCalls,
                 CountDownLatch totalCount, CountDownLatch nextPhaseCount, SkiersApi skiersApi) {
        this.numThreads = numThreads;
        this.numSkier = numSkier;
        this.numLifts = numLifts;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numPost = numPost;
        this.successCalls = successCalls;
        this.failCalls = failCalls;
        this.totalCount = totalCount;
        this.nextPhaseCount = nextPhaseCount;
        this.skiersApi = skiersApi;
    }

    public void start() {
        for (int i = 0; i < numThreads; i++) {
            Runnable thread = createRunnable(i);
            new Thread(thread).start();
        }
    }

    public Runnable createRunnable(final int threadIdx) {
        Runnable thread = () -> {
            for (int i = 0; i < numPost; i++) {
                List<String> postRow = new ArrayList<>();
                LiftRide liftRide = createLiftRide();
                String responseCode = "";

                // timestamp for before sending the request
                long startTime = System.currentTimeMillis();

                for (int j = 0; j < RETRY_TIMES; j++) {
                    try {
                        ApiResponse<Void> res = skiersApi.writeNewLiftRideWithHttpInfo(liftRide,
                                1997, "1997", "0114", getRandomSkierId(threadIdx));
                        responseCode = String.valueOf(res.getStatusCode());
                        successCalls.incrementAndGet();
                        break;
                    } catch (ApiException e) {
                        failCalls.incrementAndGet();
                        System.err.println("Exception when calling SkierApi#writeNewLiftRide, tried " + j + " times");
                        e.printStackTrace();
                    }
                }

                // timestamp for after sending the request
                long endTime = System.currentTimeMillis();
                // latency in milliseconds
                long latency = endTime - startTime;
                // store output into rows list
                postRow.addAll(Arrays.asList(startTime+"", "POST", latency+"", responseCode));
                rows.add(postRow);
            }
            totalCount.countDown();
            nextPhaseCount.countDown();
        };
        return thread;
    }

    private LiftRide createLiftRide() {
        LiftRide liftRide = new LiftRide();
        liftRide.setTime(getRandomTime());
        liftRide.setLiftID(getRandomLiftId());
        liftRide.setWaitTime((int)Math.random() * 10);
        return liftRide;
    }

    /**
     * A helper function to generate random skier id
     * @param threadIdx - the start idx of thread
     * @return a random skier id
     */
    private Integer getRandomSkierId(int threadIdx) {
        int skierPerThread = numSkier / numThreads;
        int start = threadIdx;
        int end = threadIdx + skierPerThread - 1;
        int randId = (int)Math.random() * (end - start + 1) + start;
        return randId;
    }

    private Integer getRandomTime() {
        int randTime = (int)Math.random() * (endTime - startTime + 1) + startTime;
        return randTime;
    }

    private Integer getRandomLiftId() {
        int randLiftId = (int)Math.random() * (numLifts) + 1;
        return randLiftId;
    }
}
