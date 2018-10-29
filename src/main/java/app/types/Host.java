package webapp.types;

public class Host {

    private String npm;
    private String ip;

    public Host(String npm, String ip) {
        this.npm = npm;
        this.ip = ip;
    }

    public String getNpm() {
        return this.npm;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }
}