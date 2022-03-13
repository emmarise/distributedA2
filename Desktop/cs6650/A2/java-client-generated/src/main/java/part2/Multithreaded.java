package part2;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Multithreaded {

//      private static final String WEB_PACKAGE = "/ServletSkier_war_exploded";  // local
    private static final String WEB_PACKAGE = "/ServletSkier_war";  // aws

    public static void main(String[] args) throws ApiException, InterruptedException {
        // args[]:
        // --numThreads 1024 --numSkier 100 --numLifts 10 --numRuns 15 --ipPort 35.89.18.83:8080
        // --numThreads 1024 --numSkier 100 --numLifts 10 --numRuns 15 --ipPort localhost:8080
        CommandParser commandParser = CommandParser.parseArgs(args);

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://" + commandParser.ipPort + WEB_PACKAGE);
        SkiersApi skiersApi = new SkiersApi(apiClient);

        // Get parameters from command line
        int numThreads = commandParser.numThreads;
        int numSkiers = commandParser.numSkiers;
        int numLifts = commandParser.numLifts;
        int numRuns = commandParser.numRuns;
        int numPostP1 =  (int)(numRuns * 0.2) * (numSkiers / (numThreads / 4));
        int numPostP2 =  (int)(numRuns * 0.6) * (numSkiers / numThreads);
        int numPostP3 =  (int)(numRuns * 0.1) * (numSkiers / (numThreads / 10));

        int numThreadsP1 = numThreads / 4;
        int numThreadsP2 = numThreads;
        int numThreadsP3 = numThreads / 10;
        CountDownLatch totalCount = new CountDownLatch(numThreadsP1 + numThreadsP2 + numThreadsP3);

        AtomicInteger successCalls = new AtomicInteger(0);
        AtomicInteger failCalls = new AtomicInteger(0);

        // ******* part1.Phase 1 *******
        int P2Trigger = (int)Math.ceil(numThreadsP1 / 20.0);
        int startTimeP1 = 1;
        int endTimeP1 = 90;
        CountDownLatch triggerCountP2 = new CountDownLatch(P2Trigger);
        Phase phase1 = new Phase(numThreads, numSkiers, numLifts, startTimeP1,
                endTimeP1, numPostP1, successCalls, failCalls, totalCount, triggerCountP2, skiersApi);

        // ******* part1.Phase 2 *******
        int P3Trigger = (int)Math.ceil(numThreadsP2 / 20.0);
        int startTimeP2 = 91;
        int endTimeP2 = 360;
        CountDownLatch triggerCountP3 = new CountDownLatch(P3Trigger);
        Phase phase2 = new Phase(numThreads, numSkiers, numLifts, startTimeP2,
                endTimeP2, numPostP2, successCalls, failCalls, totalCount, triggerCountP3, skiersApi);

        // ******* part1.Phase 3 *******
        int startTimeP3 = 361;
        int endTimeP3 = 420;
        Phase phase3 = new Phase(numThreads, numSkiers, numLifts, startTimeP3,
                endTimeP3, numPostP3, successCalls, failCalls, totalCount, triggerCountP3, skiersApi);

        // part1. Main logic for running three phases
        long startTime = System.currentTimeMillis();
        System.out.println("-----------PHASE1-----------");
        phase1.start();
        triggerCountP2.await();
        System.out.println("-----------PHASE2-----------");
        phase2.start();
        triggerCountP3.await();
        System.out.println("-----------PHASE3-----------");
        phase3.start();
        totalCount.await();
        long endTime = System.currentTimeMillis();
        long totalRunTime = endTime - startTime;
        System.out.println("Number of successful requests sent: " + successCalls);
        System.out.println("Number of unsuccessful request: " + failCalls);
        System.out.println("The total run time for all phases to complete: " + totalRunTime);
        System.out.println("Throughput: " + ((successCalls.get() + failCalls.get()) / (totalRunTime/ 1000)));

        // generate output to csv file
        List<List<String>> rows = new ArrayList<>();
        rows.addAll(phase1.rows);
        rows.addAll(phase2.rows);
        rows.addAll(phase3.rows);
        CSVPopulator csvPopulator = new CSVPopulator(rows, startTime);
        csvPopulator.outputToCSV();

        // part 2
        System.out.println("=======part 2======");
        Calculator calculator = new Calculator(rows, startTime, endTime);
        System.out.println("Mean response time for POSTs: " + calculator.getMean());
        System.out.println("Median response time for POSTs: " + calculator.getMedian());
        System.out.println("Throughput: " + calculator.getThroughput());
        System.out.println("P99 response time for POSTs: " + calculator.getP99());
        System.out.println("Min response time for POSTs: " + calculator.getMinResponseTime());
        System.out.println("Max response time for POSTs: " + calculator.getMaxResponseTime());
    }
}