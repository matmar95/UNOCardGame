package game.network;

import utils.Logger;

public class HeartbeatService implements Runnable {
    private final Logger LOG = new Logger(HeartbeatService.class);

    private final int DEFAULT_SAMPLING_PERIOD = 5; //seconds

    @Override
    public void run() {
        LOG.info("Heartbeat service started");
        while (true) {
            try {
                LOG.info("Performing nodes healthcheck...");
                NetworkManager.getInstance().nodesHealthCheck();
                Thread.sleep(DEFAULT_SAMPLING_PERIOD * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
