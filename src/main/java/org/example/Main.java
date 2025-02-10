package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/l6flats?serverTimezone=Europe/Berlin";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "password";
    static Connection conn;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try{
            try{
                conn = DriverManager.getConnection(DB_CONNECTION,DB_USER,DB_PASSWORD);
                initDB();
                while (true){
                    System.out.println("1. Add new flat.");
                    System.out.println("2. View all flats.");
                    System.out.println("3. Find flat by parameters.");
                    System.out.print("Your choice: ");
                    String s = scanner.nextLine();
                    switch (s){
                        case "1":
                            addFlat(scanner);
                            break;
                        case "2":
                            viewAllFlats();
                            break;
                        case "3":
                            FindFlatsByParam(scanner);
                            break;
                        default:
                            return;
                    }
                }
            }finally {
                scanner.close();
                if(conn!=null) conn.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            return;
        }
    }
    private static void initDB() throws SQLException{
        Statement st = conn.createStatement();
        try{
            st.execute("DROP TABLE IF EXISTS Flats");
            st.execute("CREATE TABLE Flats (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, district VARCHAR(20), " +
                    "address VARCHAR(50) NOT NULL, roomNum INT, square DECIMAL, price INT)");
        }finally {
            st.close();
        }
    }
    private static void addFlat(Scanner scanner) throws SQLException {
        System.out.print("Enter flat district: ");
        String district = scanner.nextLine();
        System.out.print("Enter flat address: ");
        String address = scanner.nextLine();
        System.out.print("Enter number of rooms: ");
        String sroomNum = scanner.nextLine();
        System.out.print("Enter square: ");
        String ssquare = scanner.nextLine();
        System.out.print("Enter price: ");
        String sprice = scanner.nextLine();
        int roomNum = Integer.parseInt(sroomNum);
        float square = Float.parseFloat(ssquare);
        int price = Integer.parseInt(sprice);

        String sql = "INSERT INTO Flats (district," +
                "address, roomNum, square, price) VALUES(?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setInt(3, roomNum);
            ps.setFloat(4, square);
            ps.setInt(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }
    private static void viewAllFlats() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Flats");
        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();
                System.out.println(" ---------------------------------------------------------------------");
                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(" | "+md.getColumnName(i) + "\t");
                System.out.println(" | ");
                System.out.println(" ---------------------------------------------------------------------");
                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(" | "+rs.getString(i) + "\t");
                    }
                    System.out.println(" | ");
                    System.out.println(" ---------------------------------------------------------------------");
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
    private static void FindFlatsByParam(Scanner scanner) throws SQLException {
        System.out.println("Select a parameter to search by:");
        System.out.println("1 - id, 2 - district, 3 - address, 4 - roomNum, 5 - square, 6 - price.");
        System.out.print("Your choice: ");
        String s = scanner.nextLine();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Flats " +
                    "WHERE id = ? or district = ? or address = ? or roomNum = ? or square = ? or price = ?");
            try {
                switch (s) {
                    case "1":
                        System.out.print("Enter id: ");
                        String sid = scanner.nextLine();
                        int id = Integer.parseInt(sid);
                        ps.setInt(1, id);
                        break;
                    case "2":
                        System.out.print("Enter district: ");
                        String district = scanner.nextLine();
                        ps.setString(2, district);
                        break;
                    case "3":
                        System.out.print("Enter address: ");
                        String address = scanner.nextLine();
                        ps.setString(3, address);
                        break;
                    case "4":
                        System.out.print("Enter number of rooms: ");
                        String sroomNum = scanner.nextLine();
                        int roomNum = Integer.parseInt(sroomNum);
                        ps.setInt(4, roomNum);
                        break;
                    case "5":
                        System.out.print("Enter square: ");
                        String ssquare = scanner.nextLine();
                        float square = Float.parseFloat(ssquare);
                        ps.setFloat(5, square);
                        break;
                    case "6":
                        System.out.print("Enter price: ");
                        String sprice = scanner.nextLine();
                        int price = Integer.parseInt(sprice);
                        ps.setInt(6, price);
                        break;
                    default:
//                    break;
                        return;
                }
                ResultSet rs = ps.executeQuery();

                try {
                    ResultSetMetaData md = rs.getMetaData();
                    System.out.println(" ---------------------------------------------------------------------");
                    for (int i = 1; i <= md.getColumnCount(); i++)
                        System.out.print(" | " + md.getColumnName(i) + "\t");
                    System.out.println(" | ");
                    System.out.println(" ---------------------------------------------------------------------");
                    while (rs.next()) {
                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            System.out.print(" | " + rs.getString(i) + "\t");
                        }
                        System.out.println(" | ");
                        System.out.println(" ---------------------------------------------------------------------");
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ps.close();
            }
        }catch (Exception ex) {
            System.out.println(ex);
        }
    }
}