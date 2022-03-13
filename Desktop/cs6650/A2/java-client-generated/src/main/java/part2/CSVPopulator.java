package part2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVPopulator {

    private static final String OUTPUT_PATH = "/Users/emma/Desktop/cs6650/files/";

    List<List<String>> rows;
    long startTime;

    public CSVPopulator(List<List<String>> rows, long startTime) {
        this.rows = rows;
        this.startTime = startTime;
    }

    public void outputToCSV() {
        try {
            FileWriter csvWriter = new FileWriter( OUTPUT_PATH + "result32.csv");
            csvWriter.append("Start time");
            csvWriter.append(",");
            csvWriter.append("Request Type");
            csvWriter.append(",");
            csvWriter.append("Latency");
            csvWriter.append(",");
            csvWriter.append("Response Code");
            csvWriter.append("\n");

            for (List<String> rowData : rows) {
                rowData.set(0, String.valueOf(Long.parseLong(rowData.get(0)) - startTime));
                csvWriter.append(String.join(",", rowData));
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}