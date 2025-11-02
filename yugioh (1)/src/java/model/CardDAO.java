/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author xuhoa
 */
public class CardDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public ArrayList<CardDTO> getAllCard() {
        ArrayList<CardDTO> list = new ArrayList<>();
        try {
            // 1 - Tao ket noi
            Connection conn = DBUtils.getConnection();

            // 2 - Tao cau lenh
            String sql = "SELECT * FROM tblCards";

            // 3 - Tao statement de co the run cau lenh
            PreparedStatement pst = conn.prepareStatement(sql);

            // 4 - Thuc thi cau lenh
            ResultSet rs = pst.executeQuery();

            // 5 - Kiem tra
            while (rs.next()) {
                CardDTO card = new CardDTO();
                card.setCardID(rs.getString("CardCode"));
                card.setCardName(rs.getString("CardName"));
                card.setRarity(rs.getString("Rarity"));
                card.setPrice(rs.getDouble("Price"));
                card.setImage(rs.getString("ImageUrl"));
                card.setSetID(rs.getInt("SetID"));

                list.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        CardDAO dao = new CardDAO();
        List<CardDTO> list = dao.getAllCard();
        for (CardDTO o : list) {
            System.out.println(o);
        }
    }

    public CardDTO getMostExpensiveCard() {
        CardDTO card = null;
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT TOP 1 * FROM tblCards ORDER BY price DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                card = new CardDTO();
                card.setCardID(rs.getString("cardCode"));
                card.setCardName(rs.getString("cardName"));
                card.setRarity(rs.getString("rarity"));
                card.setPrice(rs.getDouble("price"));
                card.setImage(rs.getString("ImageUrl"));
                card.setSetID(rs.getInt("SetID"));

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

    public List<CardDTO> getCardsBySetID(String setID) {
        List<CardDTO> list = new ArrayList<>();
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM tblCards WHERE SetID = ?")) {
            ps.setString(1, setID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CardDTO c = new CardDTO();
                    c.setCardID(rs.getString("CardCode"));
                    c.setCardName(rs.getString("CardName"));
                    c.setRarity(rs.getString("Rarity"));
                    c.setPrice(rs.getDouble("Price"));
                    c.setImage(rs.getString("ImageUrl"));
                    c.setSetID(rs.getInt("SetID"));
                    list.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public CardDTO getCardByID(String cardID) {
        CardDTO card = null;
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM tblCards WHERE CardCode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cardID);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                card = new CardDTO();
                card.setCardID(rs.getString("cardCode"));
                card.setCardName(rs.getString("cardName"));
                card.setRarity(rs.getString("rarity"));
                card.setPrice(rs.getDouble("price"));
                card.setImage(rs.getString("ImageUrl"));
                card.setSetID(rs.getInt("SetID"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

}
