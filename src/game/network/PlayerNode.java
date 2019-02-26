package game.network;

import java.io.Serializable;

public class PlayerNode implements Serializable {
    private String username;
    private String ipAddress;
    private int port;
    private boolean alive;
    private String avatar;

    public PlayerNode(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.alive = true;
    }

    public PlayerNode(String username, String ipAddress, int port) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;
        this.alive = true;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getNetworkAddress() {
        return ipAddress + ":" + port;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar(){
        return avatar;
    }
    @Override public boolean equals(Object obj) {
        if (obj instanceof PlayerNode) {
            return this.getNetworkAddress().equals(((PlayerNode) obj).getNetworkAddress());
        } else {
            return super.equals(obj);
        }
    }

    @Override public String toString() {
        return "PlayerNode{"
                + "username='"
                + username
                + '\''
                + ", ipAddress='"
                + ipAddress
                + '\''
                + ", port="
                + port
                + ", alive="
                + alive
                + '}';
    }
}

