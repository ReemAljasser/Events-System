

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class AdminEventsExporter {

    public static void exportToPDF() {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Admin_Events_Summary.pdf"));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            document.add(new Paragraph("Tech Inspire - All Events Summary", titleFont));
            document.add(new Paragraph(" ")); // مسافة

            // اتصال بقاعدة البيانات
            Connection conn = DBConnection.getConnection();

            // عدد الفعاليات
            Statement stmt = conn.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) AS total FROM events");
            int totalEvents = rsCount.next() ? rsCount.getInt("total") : 0;

            // أكثر فئة انتشارًا
            ResultSet rsPopular = stmt.executeQuery("SELECT category, COUNT(*) AS count FROM events GROUP BY category ORDER BY count DESC LIMIT 1");
            String popularCategory = rsPopular.next() ? rsPopular.getString("category") : "N/A";

            // عدد المستخدمين اللي عندهم مفضلة
            ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(DISTINCT user_id) AS total_users FROM favorite_events");
            int usersWithFavorites = rsUsers.next() ? rsUsers.getInt("total_users") : 0;

            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            document.add(new Paragraph("Total Events: " + totalEvents, normalFont));
            document.add(new Paragraph("Most Popular Category: " + popularCategory, normalFont));
            document.add(new Paragraph("Total Users with Favorites: " + usersWithFavorites, normalFont));
            document.add(new Paragraph("Exported on: " + today, normalFont));
            document.add(new Paragraph("-------------------------------------------------------------"));

            // الجدول
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{3, 2, 2, 2});

            table.addCell(new PdfPCell(new Phrase("Event Name", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Category", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Date", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Location", headerFont)));

            ResultSet rs = stmt.executeQuery("SELECT name, category, date, location FROM events");

            while (rs.next()) {
                table.addCell(new Phrase(rs.getString("name"), normalFont));
                table.addCell(new Phrase(rs.getString("category"), normalFont));
                table.addCell(new Phrase(rs.getString("date"), normalFont));
                table.addCell(new Phrase(rs.getString("location"), normalFont));
            }

            document.add(table);
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
