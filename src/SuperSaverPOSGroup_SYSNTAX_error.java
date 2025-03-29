import java.time.LocalDateTime;
import java.util.Scanner;

/*SuperSaverPOSGroup_SYSNTAX_error
Functional Requirements
    Item codes: - cashiers enter item codes to add groceries to a bill.
    Quantity: - cashiers enter the item quantity to add groceries to a bill.
    Product details: -the system fetches product details (price, discount) from a CSV file.
    Discount: - discounts up to 75% can be applied.
    Billing process: -bills are generated and saved as a PDF file to be printed for the customer.
    Pending bills: -Retrieve the pending bill later when the customer returns.  
    Report Generation: - Generate total revenue reports and send the report via email to salesteam@supersaving.lk.

Non-Functional Requirements
    Reliability: - The system should function correctly under all conditions, with minimal downtime.
    Security: - data (bills, reports) should be stored securely 
    Availability: - Should be available 24/7 for supermarket operations.
    Usability: - The interface should be simple for cashiers to operate.
    Maintainability: - Should be easy to update and maintain.
    Scalability: - Should support multiple cashiers and customers simultaneously. */

public class SuperSaverPOSGroup_SYSNTAX_error{
    public static void main(String[] args) {
        ReportGenerator reportGenerator = new ReportGenerator();
        Scanner input = new Scanner(System.in);
        String response;
        POS mypos = new POS("F:\\acadamic\\Semester 02\\Programme Contruction\\Inclass Lab_ IO stream and serialization_pos system\\POS System\\src\\example.csv", reportGenerator);
        while (true)
        {
            mypos.start(input);
            System.out.println("Do you want to continue next one? (y/n)");
            response = input.nextLine().toUpperCase();
            if (response.equals("N")) 
            {
                System.out.println("System is ending");
                break;
            }
        }
        System.out.println("Do you want to generate a report ? (Y/N) : ");
        response = input.nextLine().toUpperCase();
        if (response.equals("Y")) 
        {
            reportGenerator.generateRevenueReport(LocalDateTime.now().minusDays(7), LocalDateTime.now());
        }
        input.close();
   
    }
}
