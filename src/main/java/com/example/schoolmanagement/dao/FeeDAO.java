package com.example.schoolmanagement.dao;

import com.example.schoolmanagement.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;

public class FeeDAO {

    public double getTotalEarnings() {
        String query = "SELECT SUM(amount) FROM fees";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public ObservableList<XYChart.Data<String, Number>> getMonthlyEarningsForYear(int year) {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
        String query = "SELECT strftime('%m', payment_date) as month, SUM(amount) as total " +
                       "FROM fees WHERE strftime('%Y', payment_date) = ? " +
                       "GROUP BY month ORDER BY month";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, String.valueOf(year));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int monthNum = Integer.parseInt(rs.getString("month"));
                String monthName = new DateFormatSymbols().getShortMonths()[monthNum - 1];
                double total = rs.getDouble("total");
                data.add(new XYChart.Data<>(monthName, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}