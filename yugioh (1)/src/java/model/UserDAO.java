/*
 * UserDAO.java - dùng PBKDF2WithHmacSHA256 để hash mật khẩu với salt cố định
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import utils.DBUtils;

public class UserDAO {

    // 🔹 Salt cố định cho toàn hệ thống (có thể thay bằng salt riêng từng user nếu muốn)
    public static final String SALT = "prj@301#2025";

    public UserDAO() {
    }

    // ======================================================
    // 🔹 Hàm lấy user theo ID
    // ======================================================
    public UserDTO getUserById(String userName) {
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM tblUsers WHERE userID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserName(rs.getString("userID"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setFullName(rs.getString("fullName"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getBoolean("status"));
                user.setRole(rs.getString("role"));
                user.setCreated_time(rs.getTimestamp("created_at").toLocalDateTime());
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserDTO getUserByEmail(String userName) {
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM tblUsers WHERE email=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserName(rs.getString("userID"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setFullName(rs.getString("fullName"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getBoolean("status"));
                user.setRole(rs.getString("role"));
                user.setCreated_time(rs.getTimestamp("created_at").toLocalDateTime());
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======================================================
    // 🔹 Login: so sánh password người dùng nhập với passwordHash trong DB
    // ======================================================
    public boolean login(String userName, String password) {
        UserDTO user;
        try {
            if (getUserById(userName) != null || getUserByEmail(userName) != null) {
                if (getUserById(userName) != null) {
                    user = getUserById(userName);
                } else {
                    user = getUserByEmail(userName);
                }
                return verifyPassword(password, user.getPasswordHash(), SALT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // 🔹 Lấy tất cả user
    // ======================================================
    public ArrayList<UserDTO> getAllUser() {
        ArrayList<UserDTO> list = new ArrayList<>();
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM tblUsers";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserName(rs.getString("userID"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setFullName(rs.getString("fullName"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getBoolean("status"));
                user.setRole(rs.getString("role"));
                user.setCreated_time(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ======================================================
    // 🔹 Tìm user theo tên
    // ======================================================
    public ArrayList<UserDTO> getAllUserByName(String name) {
        ArrayList<UserDTO> list = new ArrayList<>();
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM tblUsers WHERE fullName LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + name + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserName(rs.getString("userID"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setFullName(rs.getString("fullName"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getBoolean("status"));
                user.setRole(rs.getString("role"));
                user.setCreated_time(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ======================================================
    // 🔹 Thêm user mới (mật khẩu được mã hóa)
    // ======================================================
    public boolean insert(UserDTO user) {
        try ( Connection c = DBUtils.getConnection()) {
            String sql = "INSERT INTO tblUsers(userID, email, passwordHash, fullName, phone, status, role, created_at) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, GETDATE())";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashPassword(user.getPasswordHash())); // hash trước khi lưu
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhone());
            ps.setBoolean(6, user.isStatus());
            ps.setString(7, user.getRole());

            int i = ps.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // 🔹 Cập nhật user
    // ======================================================
    public boolean update(UserDTO user) {
        try ( Connection c = DBUtils.getConnection()) {
            String sql = "UPDATE tblUsers "
                    + "SET fullName=?, email=?, phone=?, role=?, status=?, passwordHash=? "
                    + "WHERE userID=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isStatus());
            ps.setString(6, hashPassword(user.getPasswordHash()));
            ps.setString(7, user.getUserName());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // 🔹 Xóa user cứng
    // ======================================================
    public boolean hardDelete(String userID) {
        try ( Connection c = DBUtils.getConnection()) {
            String sql = "DELETE FROM tblUsers WHERE userID=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, userID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // 🔹 Xóa mềm (status = 0)
    // ======================================================
    public boolean softDelete(String userID) {
        try ( Connection c = DBUtils.getConnection()) {
            String sql = "UPDATE tblUsers SET status=0 WHERE userID=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, userID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // 🔹   hash
    // ======================================================
    public static String hashPassword(String password) throws Exception {
        int iterations = 65536;
        int keyLength = 256;
        byte[] saltBytes = SALT.getBytes("UTF-8");

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(hash);
    }

    // ======================================================
    // 🔹 Kiểm tra mật khẩu
    // ======================================================
    public static boolean verifyPassword(String inputPassword, String storedHash, String salt) throws Exception {
        String newHash = hashPassword(inputPassword);
        return newHash.equals(storedHash);
    }

}
