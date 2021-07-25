package cc.l89669.nonupdate;

import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.security.Permission;

import lombok.SneakyThrows;

public class NetworkManager extends SecurityManager {

    @Override
    @SneakyThrows
    public void checkPermission(final Permission perm) {
        if (perm instanceof SocketPermission) {
            SocketPermission socketperm = (SocketPermission) perm;
            NonUpdate.LOGGER.info("[NonUpdate] Attemption: " + socketperm.getActions() + " " + socketperm.getName());
            if (socketperm.getName().indexOf(':') == -1 && socketperm.getName().indexOf('.') == -1)
                return; // sockets, for some special uses
            if (socketperm.getName().startsWith("localhost:") || socketperm.getName().startsWith("127.0.0.1:"))
                return; // local host, never lags
            if (socketperm.getActions().contains("listen") || socketperm.getActions().contains("accept"))
                return; // server is trying to communicate with players! never block them!
            if (socketperm.getActions().contains("connect") || socketperm.getActions().contains("resolve")) {
                boolean defaultBlocking = NonUpdate.configuration.defaultBlocking();
                boolean blocking = NonUpdate.configuration.checkURL(socketperm.getName().toLowerCase());
                if (defaultBlocking)
                    blocking = !blocking;
                NonUpdate.LOGGER.info("[NonUpdate] Status: blocking={}, defaultBlocking={}", blocking, defaultBlocking);
                if (blocking)
                    throw new MalformedURLException("Socket connection blocked");
            }
        }
    }

}
