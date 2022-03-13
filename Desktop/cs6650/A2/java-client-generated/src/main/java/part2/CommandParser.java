package part2;

public class CommandParser {
    // --numThreads 100 --numSkier 100 --numLifts 10 --numRuns 15 --IpPort 192.168.1.67:8080
    public int numThreads;
    public int numSkiers;
    public int numLifts;
    public int numRuns;
    public String ipPort;

    public CommandParser(int numThreads, int numSkiers, int numLifts, int numRuns, String ipPort) {
        this.numThreads = numThreads;
        this.numSkiers = numSkiers;
        this.numLifts = numLifts;
        this.numRuns = numRuns;
        this.ipPort = ipPort;
    }

    public static CommandParser parseArgs(String[] args) {
        int numThreadsTemp = 0;
        int numSkiersTemp = 50000; // max = 100000
        int numLiftsTemp = 40; // default = 40
        int numRunsTemp = 10; // default = 10
        String ipPortTemp = null;

        // --numThreads 100 --numSkiers 100 --numLifts 10 --numRuns 15 --ipPort 192.168.1.67:8080
        for (int i = 0; i < args.length; i++) {
            String option = args[i];
            String value = args[++i];
            switch (option) {
                case "--numThreads":
                    numThreadsTemp = Integer.parseInt(value);
                    break;
                case "--numSkiers":
                    numSkiersTemp = Integer.parseInt(value);
                    break;
                case "--numLifts":
                    numLiftsTemp = Integer.parseInt(value);
                    break;
                case "--numRuns":
                    numRunsTemp = Integer.parseInt(value);
                    break;
                case "--ipPort":
                    ipPortTemp = value;
                    break;
            }
        }

        return new CommandParser(numThreadsTemp, numSkiersTemp, numLiftsTemp, numRunsTemp, ipPortTemp);
    }
}
