import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

class Bill 
{
    String cashiername, branch, customername;
    List<buy_item> items = new ArrayList<>();
    LocalDateTime datetime;
    double total;
    POS system;
    double total_discount = 0;

    Bill(String cashiername, String branch, String customername, POS System) 
    {
        this.cashiername = cashiername;
        this.branch = branch;
        this.customername = customername;
        this.datetime = LocalDateTime.now();
        this.system = System;
    }
         
    public void additem(buy_item item) 
    {
        items.add(item);
    }
          
    public void saveAsPDF() 
    {
        try (PDDocument document = new PDDocument()) 
        {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDTrueTypeFont customFont = PDTrueTypeFont.load(document, new File("F:\\acadamic\\Semester 02\\Programme Contruction\\Inclass Lab_ IO stream and serialization_pos system\\POS System\\src\\Courier Prime.ttf"), WinAnsiEncoding.INSTANCE);
            
            float pageWidth = page.getMediaBox().getWidth();
        
            contentStream.setFont(customFont, 18);
            contentStream.beginText();
            float titleWidth = (customFont.getStringWidth("SuperSaver POS - Invoice") / 1000) * 18;
            contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, 750);
            contentStream.showText("SuperSaver POS - Invoice");
            
            contentStream.newLineAtOffset(-((pageWidth - titleWidth) / 2 - 50), -30);
            contentStream.showText("===============================================================================");
            
            contentStream.setFont(customFont, 12);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Cashier: " + this.cashiername);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Branch: " + branch);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Customer: " + (this.customername.isEmpty() ? "Guest" : customername));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Date: " + this.datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("----------------------------------------------------------------------------");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Items Purchased:");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("----------------------------------------------------------------------------");
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(customFont, 10);
            

            String formatHeader = "%-20s %-12s %-10s %-12s %-12s %-12s";
            contentStream.showText(String.format(formatHeader, "Item Name", "Unit Price", "Quantity", "Total-Rs", "Discount-Rs", "Net Total-Rs"));
            contentStream.newLineAtOffset(0, -20);
            
            for (buy_item item_inbill : items) 
            {
                String formatRow = "%-20s %-12.2f %-10.3f %-12.2f %-12.2f %-12.2f";
                contentStream.showText(String.format(formatRow, 
                    item_inbill.name, 
                    item_inbill.unit_price, 
                    item_inbill.quantity, 
                    item_inbill.total_price, 
                    item_inbill.discountPrice(), 
                    item_inbill.netPrice()
                ));
                contentStream.newLineAtOffset(0, -20);
            }
            contentStream.setFont(customFont, 15);
            contentStream.showText("-------------------------------------------------------------");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(String.format("Total: Rs. %.2f /=", this.total));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(String.format("Total Discount: Rs. %.2f /=", this.total_discount));
            contentStream.setFont(customFont, 10);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")));
            contentStream.endText();
            contentStream.close();
            
            String filename = "Bill_" + this.customername + "_" + this.datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".pdf";
            document.save(filename);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
