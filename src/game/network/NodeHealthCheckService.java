package game.network;

import utils.Logger;

public class NodeHealthCheckService implements Runnable {
    private final Logger LOG = new Logger(NodeHealthCheckService.class);

    private final int DEFAULT_SAMPLING_PERIOD = 1; //seconds
    private int val = 0;

    @Override
    public void run() {
        LOG.info("Heartbeat service started");
        while (true) {
            try {
                if(!(val++<10)) {
                    LOG.info("Performing nodes healthcheck...");
                    NetworkManager.getInstance().nodesHealthCheck(true);
                    val = 0;
                } else {
                    NetworkManager.getInstance().nodesHealthCheck(false);
                }
                Thread.sleep(DEFAULT_SAMPLING_PERIOD * 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
