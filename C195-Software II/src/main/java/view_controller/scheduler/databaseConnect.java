package view_controller.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;

public class databaseConnect {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "client_schedule";
        String databaseUser = "sqlUser";
        String databasePassword = "Passw0rd!";
        String url = "jdbc:mysql://localhost:3306/"+databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser, databasePassword);
        } catch (Exception e) {
            System.out.println("DB Connect Failed: "+url);
            System.out.println("db username: "+databaseUser);
            System.out.println("db password: "+databasePassword);
            e.printStackTrace();
        }

        return databaseLink;
    }
}
