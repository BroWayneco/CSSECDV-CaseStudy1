package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class SQLite {
    
    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";
    
    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0,\n"
            + " salt TEXT ,\n"
            + " hash TEXT\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES('" + username + "','" + name + "','" + stock + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES('" + event + "','" + username + "','" + desc + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES('" + name + "','" + stock + "','" + price + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addUser(String username, String password, String confpass, String salt, String hash) {
        if(isValidUsername(username) && isValidPassword(password)) {
            String sql = "INSERT INTO users(username,password,salt,hash) VALUES('" + username + "','" + password + "','" + salt + "','" + hash + "')";
            
            try (Connection conn = DriverManager.getConnection(driverURL);
                Statement stmt = conn.createStatement()){
                stmt.execute(sql);
                JOptionPane.showMessageDialog(null, "User has been created!",
                "Message", JOptionPane.INFORMATION_MESSAGE);
        }  catch (Exception ex) {
            System.out.print(ex);
        }}

        else if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username and Password Cannot be Empty!",
                "Error!", JOptionPane.ERROR_MESSAGE);
        }
    
        else if(username.length() > 0 && password.length() > 0 && !password.equals(confpass)) {
                JOptionPane.showMessageDialog(null, "Password and Confirm Password must match!",
                "Error!", JOptionPane.ERROR_MESSAGE);
        }

        else if(!isValidUsername(username)) {
            JOptionPane.showMessageDialog(null,"Username : Username length must be between 6-30 characters, and must only be word characters.",
            "Error", JOptionPane.ERROR_MESSAGE);
        }

        else if(!isValidPassword(password)){
            JOptionPane.showMessageDialog(null, "Password : Password length must be at least 8 characters and 64 characters at most. It must also contain at least 1 upper case character, at least 1 lower case character, at least 1 digit, and at least 1 special character and no spaces",
            "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return histories;
    }
    
    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                                   rs.getString("event"),
                                   rs.getString("username"),
                                   rs.getString("desc"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logs;
    }
    
    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return products;
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, role, locked FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("password"),
                                   rs.getInt("role"),
                                   rs.getInt("locked")));
            }
        } catch (Exception ex) {}
        return users;
    }

    public ArrayList<Integer> getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username='" + username + "';";
        ArrayList<Integer> roles = new ArrayList<Integer>();
        ArrayList<Integer> roles0 = new ArrayList<Integer>() ;
        roles0.add(0);

        int isUser = 0;

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    isUser = 1;
                    roles.add(rs.getInt("role"));
                } 
        } catch (Exception ex) {}

        if(isUser == 1)
            return roles;
        else {
            JOptionPane.showMessageDialog(null, "Incorrect Username or Password!",
            "Error!", JOptionPane.ERROR_MESSAGE);

            return roles0; }
    }

    public String getPassword(String username, String password) {
        String sql = "SELECT password FROM users WHERE username='" + username + "';";
        String passwords = "qwerty1234";

        int isUser = 0;
    
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    isUser = 1;
                    passwords = (rs.getString("password"));
                } 
        } catch (Exception ex) {}

        if(isUser == 1)
            return passwords;
        else {
            JOptionPane.showMessageDialog(null, "Incorrect Username or Password!",
            "Error!", JOptionPane.ERROR_MESSAGE);
            
            return passwords; }
    }
    
    public void addUser(String username, String password, int role) {
        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + password + "','" + role + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username='" + username + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User " + username + " has been deleted.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public Product getProduct(String name){
        String sql = "SELECT name, stock, price FROM product WHERE name='" + name + "';";
        Product product = null;
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            product = new Product(rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price"));
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return product;
    }

    public void editUserRole(String username, int role) {
        String sql = "UPDATE users SET role = '" + role + "' WHERE username='" + username + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User " + username + " role has been changed to " + role);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public void editProduct(String name, int stock, double price, String origName) {
        String firstName = origName;
        String sql = "UPDATE product SET stock = '" + stock + "'WHERE name ='" + name + "';";
    
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Product " + name + " has been changed.");
        } 
        catch (Exception ex) {
            System.out.print(ex);
        }

        sql =  "UPDATE product SET price = '" + price+ "'WHERE name ='" + name + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Product " + name + " has been changed.");
        } 
        catch (Exception ex) {
                System.out.print(ex);
            }

        sql = "UPDATE product SET name = '" + name + "'WHERE name ='" + firstName + "';";
        

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Product " + name + " has been changed.");
        } 
        catch (Exception ex) {
                System.out.print(ex);
            }
    }

    public void removeProduct(String productName) {
        String sql = "DELETE FROM product WHERE name='" + productName + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Product " + productName + " has been deleted.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public static boolean isValidPassword(String password) {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[@#$%^&+=])"
                       + "(?=\\S+$).{8,20}$";
 
        Pattern p = Pattern.compile(regex);
 
        if (password == null) {
            return false;
        }
 
        Matcher m = p.matcher(password);
 
        return m.matches();
    }

    public static boolean isValidUsername(String username) {
        // Regex to check valid password.
        String regex = "^[A-Za-z]\\w{5,29}$"; 
 
        Pattern p = Pattern.compile(regex);
 
        if (username == null) {
            return false;
        }
 
        Matcher m = p.matcher(username);
 
        return m.matches();
    }

    public String getForgottenPassword(String username) {
        String sql = "SELECT password FROM users WHERE username='" + username + "';";
        String passwords = "name";

        int isUser = 0;
    
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    isUser = 1;
                    passwords = (rs.getString("password"));
                } 
        } catch (Exception ex) {}

        if(isUser == 1)
            return passwords;
        else {
            JOptionPane.showMessageDialog(null, "Incorrect Username or Password!",
            "Error!", JOptionPane.ERROR_MESSAGE);
            
            return passwords; }
    }

    public String getSalt(String username) {
        String sql = "SELECT salt FROM users WHERE username='" + username + "';";
        String userSalt = "";

        int isUser = 0;
    
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    isUser = 1;
                    userSalt = (rs.getString("salt"));
                } 
        } catch (Exception ex) {}

        if(isUser == 1) {
            return userSalt;
        }

        else {
            JOptionPane.showMessageDialog(null, "Incorrect Username or Password!",
            "Error!", JOptionPane.ERROR_MESSAGE);
            
            return userSalt; }
    }

    public String getHash(String username) {
        String sql = "SELECT hash FROM users WHERE username='" + username + "';";
        String userHash = "";

        int isUser = 0;
    
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    isUser = 1;
                    userHash = (rs.getString("hash"));
                } 
        } catch (Exception ex) {}

        if(isUser == 1) {
            return userHash;
        }

        else {
            JOptionPane.showMessageDialog(null, "Incorrect Username or Password!",
            "Error!", JOptionPane.ERROR_MESSAGE);
            
            return userHash; }
    }
}

//      PREPARED STATEMENT EXAMPLE
//      String sql = "INSERT INTO users(username,password) VALUES(?,?)";
//      PreparedStatement pstmt = conn.prepareStatement(sql)) {
//      pstmt.setString(1, username);
//      pstmt.setString(2, password);
//      pstmt.executeUpdate();