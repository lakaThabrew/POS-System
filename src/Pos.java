import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletionException;

class POS {
    Map<String, item> inventory = new HashMap<>();
    private pendingBills pendingBills = new pendingBills();
    ReportGenerator generator;

    public POS(String file, ReportGenerator generator) 
    {
        loadfromcsv(file);
        this.generator = generator;
    }

    private void loadfromcsv(String file) 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            String data;
            String header = reader.readLine();
            if (header == null) 
            {
                throw new NoSuchElementException(); 
            }
            while ((data = reader.readLine()) != null) 
            {
                String[] line = data.split(",");
                String code = line[0];
                String name = line[1];
                double price = Double.parseDouble(line[2]);
                String size = line[3];
                String dom = line[4];
                String doe = line[5];
                String manufacturer = line[6];
                
                item newItem = new item(code, name, manufacturer, price, size, dom, doe);
                inventory.put(code, newItem);
            }
        } 
        catch (IOException | CompletionException e) 
        {
            System.out.println("Error reading CSV File: " + e.getMessage());
        }
    }

    public item getitemfromcode(String code) 
    {
        return inventory.get(code);
    }

    public void start(Scanner input) 
    {
        try{
            System.out.print("Enter Cashier Name: ");
            String cashier = input.nextLine();
            System.out.print("Enter Branch: ");
            String branch = input.nextLine();
            System.out.print("Enter Customer Name: ");
            String customer = input.nextLine();

            System.out.println("Welcome to SuperSaver POS");
            System.out.println("Do you want to load a pending bill? (y/n)");
            Bill bill;
            double total = 0;
            double discount = 0;
            if (input.nextLine().equalsIgnoreCase("y")) 
            {
                System.out.print("Enter Customer Name: ");
                String customername = input.nextLine();
                bill = pendingBills.get_Bill(customername);
                if (bill != null) 
                {
                    System.out.println("Pending Bill found for customer " + customername);
                    total = bill.total;
                    discount = bill.total_discount;
                } 
                else 
                {
                    System.out.println("No pending bill found for customer " + customername);
                    bill = new Bill(cashier, branch, customer, this);
                    total = 0;
                    discount = 0;
                }
            }
            else
            {
                bill = new Bill(cashier, branch, customer, this);
                total = 0;
                discount = 0;
            }
            boolean pending_flag = false;
            while (true) 
            {
                System.out.println("Enter item code (or 'done' to finish ): ");
                String code = input.nextLine();
                if (code.equals("done")) 
                {
                    break;
                }

                while (true)
                {
                    if (inventory.containsKey(code)) 
                    {
                        break;
                    } 
                    else 
                    {
                        System.out.println("Item not found! Please enter a valid item code.");
                        code = input.nextLine();
                    }
                }

                System.out.print("Enter Quantity: ");
                double qty = input.nextDouble();
                System.out.print("Enter Discount: ");
                double dis = input.nextDouble();
                input.nextLine();

                buy_item new1 = new buy_item(inventory.get(code), qty, dis);
                bill.additem(new1);
                total += new1.netPrice();
                bill.total = total;
                discount += new1.discountPrice();
                bill.total_discount = discount;
                System.out.println("Item added: " + new1.product.item_name);

                System.out.println("Save bill as pending (y/n): ");
                String response = input.nextLine();
                if (response.equalsIgnoreCase("y")) 
                {
                    pendingBills.insert_new(customer, bill);
                    pending_flag = true;
                    break;
                }
            }

            if (!pending_flag) 
            {   
                bill.saveAsPDF();
                System.out.println("\nBill saved as pdf");
                System.out.println(String.format("\nTotal Bill Amount: Rs. %.2f",bill.total));
                generator.recordBillRevenue(LocalDateTime.now(), bill.total);;
            }
        }
        catch (Exception e)
        {
            System.out.println("Something goes wrong " + e.getMessage());
        }
    }
}