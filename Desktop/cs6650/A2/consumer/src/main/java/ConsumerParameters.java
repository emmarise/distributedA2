public class ConsumerParameters {
    private String hostName;
    private String userName;
    private String password;
    private int maxThreads;

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }
}