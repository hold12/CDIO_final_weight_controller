package ctrl;

import lang.Lang;
import java.io.IOException;

public class ConnectionController {
    private boolean connected;

    public boolean connect(IWeightController weightClient, String ip, int port) {
        if (port == -1)
            return false;

        if (connected) return true;
        System.out.println(Lang.msg("connecting") + " " + ip + ":" + port + "...");

        try {
            weightClient.connect(ip, port);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionConnect"));
            System.out.println(Lang.msg("exiting"));
            return false;
        }

        System.out.println(Lang.msg("connectionEstablished"));
        connected = true;
        return true;
    }

}
