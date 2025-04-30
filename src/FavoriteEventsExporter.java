
import java.sql.SQLException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class FavoriteEventsExporter {

    public static void exportToPDF(int userId, String username) {
        Document document = new Document();

        try {
            String outputPath = System.getProperty("user.home") + "/Desktop/favorite_events.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // عنوان
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("📋 Favorite Events for " + username + "\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // جدول
            PdfPTable table = new PdfPTable(4); // 4 أعمدة
            table.setWidthPercentage(100);

            // رؤوس الأعمدة
            String[] headers = {"Event Name", "Date", "Location", "Category"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // جلب البيانات من قاعدة البيانات
            Connection conn = DBConnection.getConnection();
            String query = "SELECT name, date, location, category FROM favorite_events WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.addCell(rs.getString("name"));
                table.addCell(rs.getString("date"));
                table.addCell(rs.getString("location"));
                table.addCell(rs.getString("category"));
            }

            document.add(table);
            document.close();
            conn.close();

            JOptionPane.showMessageDialog(null, "✅ Exported to Desktop as favorite_events.pdf");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Error while exporting PDF");
        }
    }
}
