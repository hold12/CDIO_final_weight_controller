package ctrl;

import lang.Lang;
import java.io.IOException;

public class ConnectionController {
    private boolean connected;

    public boolean connect(IWeightController weightClient, String ip, int port) {
		if (connected) return true;

        if (!validIP(ip)) {
            System.out.println(Lang.msg("invalidIP") + " " + ip);
            return false;
        }

		if ((port < 1) || (port > 62535)) {
			System.out.println(Lang.msg("invalidPort") + " " + port);
			return false;
		}

        try {
			System.out.println(Lang.msg("connecting") + " " + ip + ":" + port + "...");
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

    private boolean validIP (String ip) {
        if (ip == null || ip.isEmpty())
            return false;

        if (ip.equals("localhost"))
        	return true;

        String[] parts = ip.split("\\.");
        if (parts.length != 4)
            return false;

        try {
            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255))
                    return false;
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
