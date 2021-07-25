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
            LoggingLevel logLevel = NonUpdate.configuration.loggingLevel();
            if (logLevel.contains(LoggingLevel.VERBOSE))
                NonUpdate.LOGGER.info("[NonUpdate] Attemption: " + socketperm.getActions() + " " + socketperm.getName());
            if (socketperm.getName().indexOf(':') == -1 && socketperm.getName().indexOf('.') == -1)
                return; // sockets, for some special uses
            if (socketperm.getName().startsWith("localhost:") || socketperm.getName().startsWith("127.0.0.1:"))
                return; // local host, never lags
            if (socketperm.getActions().contains("listen") || socketperm.getActions().contains("accept"))
                return; // server is trying to communicate with players! never block them!
            if (socketperm.getActions().contains("connect") || socketperm.getActions().contains("resolve")) {
                boolean blocking = NonUpdate.configuration.checkBlockURL(socketperm.getName().toLowerCase());

                if (logLevel.contains(LoggingLevel.VERBOSE)) {
                    NonUpdate.LOGGER.info("[NonUpdate] Status: blocking={}, defaultBlocking={}", blocking, NonUpdate.configuration.defaultBlocking());
                } else if (blocking && logLevel.contains(LoggingLevel.DETAILED)) {
                    NonUpdate.LOGGER.info("[NonUpdate] Attemption: " + socketperm.getActions() + " " + socketperm.getName());
                    NonUpdate.LOGGER.info("[NonUpdate] Status: blocking={}, defaultBlocking={}", blocking, NonUpdate.configuration.defaultBlocking());
                } else if (blocking && logLevel.contains(LoggingLevel.BLOCKED)) {
                    NonUpdate.LOGGER.info("[NonUpdate] Blocked: {}", socketperm.getName());
                }
                if (blocking)
                    throw new MalformedURLException("Socket connection blocked");
            }
        }
    }

}
