package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // BẮT BUỘC: Nạp driver thủ công
                Class.forName("org.sqlite.JDBC");

                String url = "jdbc:sqlite:D:/ZChessManagementSystem/db.db";
                connection = DriverManager.getConnection(url);
                System.out.println("✅ Kết nối SQLite thành công!");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Không tìm thấy driver SQLite!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Đã đóng kết nối cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
