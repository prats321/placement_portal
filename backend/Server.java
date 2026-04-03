import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;

public class Server {

    static Connection conn;

    public static void main(String[] args) throws Exception {

        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/placement_db",
                "postgres",
                "postgres123");

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // ================= REGISTER =================
        server.createContext("/register", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()));

                String data = br.readLine(); // name,cgpa,skills,email,contact
                String[] parts = data.split(",");

                String name = parts[0];
                double cgpa = Double.parseDouble(parts[1]);
                String skills = parts[2];
                String email = parts[3];
                String contact = parts[4];

                try {
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO students(name, cgpa, skills, email, contact) VALUES (?, ?, ?, ?, ?)");

                    ps.setString(1, name);
                    ps.setDouble(2, cgpa);
                    ps.setString(3, skills);
                    ps.setString(4, email);
                    ps.setString(5, contact);

                    ps.executeUpdate();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String response = "Student Saved";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            }
        });

        // ================= GET ALL =================
        server.createContext("/students", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            StringBuilder result = new StringBuilder();

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM students");

                while (rs.next()) {
                    result.append(rs.getString("name"))
                            .append(" | CGPA: ")
                            .append(rs.getDouble("cgpa"))
                            .append(" | Skills: ")
                            .append(rs.getString("skills"))
                            .append(" | Email: ")
                            .append(rs.getString("email"))
                            .append(" | Contact: ")
                            .append(rs.getString("contact"))
                            .append("\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = result.toString();
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        // ================= SEARCH =================
        server.createContext("/search", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            String query = exchange.getRequestURI().getQuery();

            String keyword = "";
            double minCgpa = 0;

            try {
                if (query != null) {
                    String[] params = query.split("&");

                    for (String param : params) {
                        String[] keyValue = param.split("=");

                        if (keyValue.length < 2)
                            continue;

                        if (keyValue[0].equals("skill") && !keyValue[1].isEmpty()) {
                            keyword = keyValue[1];
                        }

                        if (keyValue[0].equals("cgpa") && !keyValue[1].isEmpty()) {
                            minCgpa = Double.parseDouble(keyValue[1]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            StringBuilder result = new StringBuilder();

            try {
                PreparedStatement ps;

                // 🔥 CASE 1: BOTH EMPTY → return all
                if (keyword.isEmpty() && minCgpa == 0) {
                    ps = conn.prepareStatement("SELECT * FROM students");

                }
                // 🔥 CASE 2: ONLY CGPA
                else if (keyword.isEmpty()) {
                    ps = conn.prepareStatement("SELECT * FROM students WHERE cgpa >= ?");
                    ps.setDouble(1, minCgpa);

                }
                // 🔥 CASE 3: ONLY SKILL
                else if (minCgpa == 0) {
                    ps = conn.prepareStatement(
                            "SELECT * FROM students WHERE skills ILIKE ? OR name ILIKE ?");
                    ps.setString(1, "%" + keyword + "%");
                    ps.setString(2, "%" + keyword + "%");

                }
                // 🔥 CASE 4: BOTH
                else {
                    ps = conn.prepareStatement(
                            "SELECT * FROM students WHERE (skills ILIKE ? OR name ILIKE ?) AND cgpa >= ?");
                    ps.setString(1, "%" + keyword + "%");
                    ps.setString(2, "%" + keyword + "%");
                    ps.setDouble(3, minCgpa);
                }

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    result.append(rs.getString("name"))
                            .append(" | CGPA: ")
                            .append(rs.getDouble("cgpa"))
                            .append(" | Skills: ")
                            .append(rs.getString("skills"))
                            .append(" | Email: ")
                            .append(rs.getString("email"))
                            .append(" | Contact: ")
                            .append(rs.getString("contact"))
                            .append("\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = result.toString();
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.start();
        System.out.println("Backend running on http://localhost:8080 🚀");
    }
}