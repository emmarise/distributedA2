package part1;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Singlethreaded {

//          private static final String WEB_PACKAGE = "/ServletSkier_war_exploded";  // local
    private static final String WEB_PACKAGE = "/ServletSkier_war";  // aws

    public static void main(String[] args) throws ApiException, InterruptedException {
        // args[]:
        // --numThreads 1024 --numSkier 100 --numLifts 10 --numRuns 15 --ipPort 18.237.217.26:8080
        CommandParser commandParser = CommandParser.parseArgs(args);

        ApiClient apiClient = new ApiClient();
        // Path example: "http://localhost:8080/ServletSkier_war_exploded"
        apiClient.setBasePath("http://" + commandParser.ipPort + WEB_PACKAGE);
        SkiersApi skiersApi = new SkiersApi(apiClient);

        // Get parameters from command line
        int numThreads = commandParser.numThreads;
        int numSkiers = commandParser.numSkiers;
        int numLifts = commandParser.numLifts;
        int numRuns = commandParser.numRuns;
        int numPostP2 = 10000;

        int numThreadsP2 = 1;
        CountDownLatch totalCount = new CountDownLatch(1);

        AtomicInteger successCalls = new AtomicInteger(0);
        AtomicInteger failCalls = new AtomicInteger(0);

        // ******* part1.Phase 2 *******
        int P3Trigger = (int) Math.ceil(numThreadsP2 / 20.0);
        int startTimeP2 = 91;
        int endTimeP2 = 360;
        CountDownLatch triggerCountP3 = new CountDownLatch(P3Trigger);
        Phase phase2 = new Phase(numThreads, numSkiers, numLifts, startTimeP2,
                endTimeP2, numPostP2, successCalls, failCalls, totalCount, triggerCountP3, skiersApi);

        // part1. Main logic for running three phases
        long startTime = System.currentTimeMillis();
        System.out.println("-----------PHASE2-----------");
        phase2.start();
        triggerCountP3.await();
        long endTime = System.currentTimeMillis();
        long totalRunTime = endTime - startTime;
        System.out.println("Number of successful requests sent: " + successCalls);
        System.out.println("Number of unsuccessful request: " + failCalls);
        System.out.println("The total run time for phase 2 to complete: " + totalRunTime);
        System.out.println("Throughput: " + ((successCalls.get() + failCalls.get()) / (totalRunTime / 1000)));
    }
}
