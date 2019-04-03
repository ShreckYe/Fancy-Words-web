package fancywords;

import java.sql.SQLException;

/**
 * Created by Shrec on 6/10/2017.
 */
public class TestFancyWordsServer {
    public static void main(String args[]) {
        try {
            FancyWordsServer server = FancyWordsServer.getInstanceAndStart();
            System.out.println(server.requestAllEntries());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
