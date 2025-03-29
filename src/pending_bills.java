import java.util.HashMap;
import java.util.Map;

class pendingBills 
{
    Map<String, Bill> pending_Bills = new HashMap<>();

    public void insert_new(String customername, Bill bill) 
    {
        pending_Bills.put(customername, bill);
    }

    public Bill get_Bill(String customername) 
    {
        return pending_Bills.remove(customername);
    }
}