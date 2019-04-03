package fancywords;

import java.sql.SQLException;

/**
 * Created by Shrec on 6/10/2017.
 */
public class JspServerContext {
    public static FancyWordsServer server;

    static {
        try {
            server = FancyWordsServer.getInstanceAndStart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static FancyWordsServer getServer() {
        if (server == null)
            try {
                server = FancyWordsServer.getInstanceAndStart();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return server;
    }
}
