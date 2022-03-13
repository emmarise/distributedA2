import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParameterProcessor {

    private static String FILE_NAME = "config.properties";

    public static ConsumerParameters processParameters() {
        InputStream inputStream;
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = MultithreadedConsumer.class.getClassLoader();
            inputStream = classLoader.getResourceAsStream(FILE_NAME);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Property File : " + FILE_NAME + " not found");
            }
            if (!validInput(properties)) {
                throw new IllegalArgumentException(
                        "Input Parameters are not correct. Please check the file: " +
                                FILE_NAME);
            }
            return loadParameters(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ConsumerParameters();
    }

    private static ConsumerParameters loadParameters(Properties properties) {
        ConsumerParameters parameters = new ConsumerParameters();
        parameters.setMaxThreads(Integer.valueOf(properties.getProperty("maxThreads")));
        parameters.setHostName(properties.getProperty("host"));
        parameters.setUserName(properties.getProperty("userName"));
        parameters.setPassword(properties.getProperty("password"));
        return parameters;
    }

    private static boolean validInput(Properties properties) {
        if (properties == null || properties.size() == 0) {
            System.out.println("Invalid parameters. Please check the file: " + FILE_NAME);
            return false;
        }
        return true;
    }
}