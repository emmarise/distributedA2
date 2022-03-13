package part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator {

    List<List<String>> rows;
    long startTime;
    long endTime;

    List<Integer> latencyPost = new ArrayList<>();

    public Calculator(List<List<String>> rows, long startTime, long endTime) {
        this.rows = rows;
        this.startTime = startTime;
        this.endTime = endTime;
        // row : ["startTime", "requestType", "latency", "responseCode"]
        for (List<String> row : rows) {
            int latency =  Integer.parseInt(row.get(2));
            latencyPost.add(latency);
        }
    }

    public int getMean() {
        List<Integer> lst = latencyPost;
        int total = 0;
        for (int n : lst) {
            total += n;
        }
        return total / lst.size();
    }

    public int getMedian() {
        List<Integer> lst = latencyPost;
        int median = 0;
        int numResponse = lst.size();
        if (numResponse % 2 != 0) {
            median = Integer.parseInt(rows.get(numResponse / 2).get(2));
        } else {
            median = (Integer.parseInt(rows.get(numResponse / 2 - 1).get(2)) +
                    Integer.parseInt(rows.get(numResponse / 2).get(2))) / 2;
        }
        return median;
    }

    public long getTotalWallTime() {
        return endTime - startTime;
    }

    public long getThroughput() {
        int totalNumReq = rows.size();
        long totalWallTime = getTotalWallTime() / 1000;
        return totalNumReq / totalWallTime;
    }

    public int getP99() {
        List<Integer> lst = latencyPost;
        Collections.sort(lst);
        int idx = (int)(lst.size() * 0.99);
        return lst.get(idx);
    }

    public int getMaxResponseTime() {
        List<Integer> lst = latencyPost;
        int max = lst.get(0);
        for (int i : lst) {
            max = Math.max(i, max);
        }
        return max;
    }

    public int getMinResponseTime() {
        List<Integer> lst = latencyPost;
        int min = lst.get(0);
        for (int i : lst) {
            min = Math.min(i, min);
        }
        return min;
    }
}