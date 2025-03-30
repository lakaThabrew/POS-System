import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

class ReportGenerator {
    private Map<LocalDateTime, Double> revenueRecords = new HashMap<>();

    public void recordBillRevenue(LocalDateTime date, double amount) {
        revenueRecords.put(date, revenueRecords.getOrDefault(date, 0.0) + amount);
    }

    public void generateRevenueReport(LocalDateTime start, LocalDateTime end) throws IOException {
        double totalRevenue = 0;
        System.out.println("Generating revenue report from " + start + " to " + end);

        for (Map.Entry<LocalDateTime, Double> entry : revenueRecords.entrySet()) {
            if (!entry.getKey().isBefore(start) && !entry.getKey().isAfter(end)) {
                totalRevenue += entry.getValue();
            }
        }

        System.out.println(String.format("Total Revenue: Rs. %.2f ", totalRevenue));
        CreatePDF(start, end, totalRevenue);
        sendEmailReport(totalRevenue, start, end);
    }

    private void sendEmailReport(double revenue, LocalDateTime start, LocalDateTime end) {
        System.out.println("Emailing report to salesteam@supersaving.lk : Revenue from " + start + " to " + end
                + " is $" + revenue);
    }

    private void CreatePDF(LocalDateTime start, LocalDateTime end, double total) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDTrueTypeFont customFont = PDTrueTypeFont.load(document, new File("F:\\acadamic\\Semester 02\\Programme Contruction\\Inclass Lab_ IO stream and serialization_pos system\\POS System\\src\\Courier Prime.ttf"), WinAnsiEncoding.INSTANCE);
    
        float pageWidth = page.getMediaBox().getWidth();
    
        contentStream.setFont(customFont, 18);
        contentStream.beginText();
        float titleWidth = (customFont.getStringWidth("SuperSaver_SYSNTAX_error POS - Income Report") / 1000) * 18;
        contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, 750);
        contentStream.showText("SuperSaver_SYSNTAX_error POS - Income Report");
    
        contentStream.newLineAtOffset(-((pageWidth - titleWidth) / 2 - 50), -30);
        contentStream.showText("===============================================");
    
        contentStream.setFont(customFont, 12);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Start Time :" + start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("End Time: " + end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("-------------------------------------------------------------");
        contentStream.newLineAtOffset(0, -20);
        for (Map.Entry<LocalDateTime, Double> entry : revenueRecords.entrySet()) 
        {
            if (!entry.getKey().isBefore(start) && !entry.getKey().isAfter(end)) 
            {
                String formatRow = "%-20s                               Rs.  %.2f ";
                contentStream.showText(String.format(formatRow, entry.getKey().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")), entry.getValue()));
                contentStream.newLineAtOffset(0, -20);
            }
        }
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("-------------------------------------------------------------");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText(String.format("Total: Rs. %.2f /= from ", total));
        contentStream.showText(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss ")));
        contentStream.showText(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")));
        contentStream.newLineAtOffset(0, -20);
        contentStream.setFont(customFont, 10);
        contentStream.showText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")));
        contentStream.endText();
        contentStream.close();

        String sanitizedStart = start.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String sanitizedEnd = end.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String filename = "Report_from_" + sanitizedStart + "_to_" + sanitizedEnd + ".pdf";
    
        document.save(filename);
        System.out.println("Your report is printed.  ");
    }
    
}