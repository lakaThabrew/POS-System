import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class ReportGenerator {
    private Map<LocalDateTime, Double> revenueRecords = new HashMap<>();

    public void recordBillRevenue(LocalDateTime date, double amount) {
        revenueRecords.put(date, revenueRecords.getOrDefault(date, 0.0) + amount);
    }

    public void generateRevenueReport(LocalDateTime start, LocalDateTime end) {
        double totalRevenue = 0;
        System.out.println("Generating revenue report from " + start + " to " + end);

        for (Map.Entry<LocalDateTime, Double> entry : revenueRecords.entrySet()) {
            if (!entry.getKey().isBefore(start) && !entry.getKey().isAfter(end)) {
                totalRevenue += entry.getValue();
            }
        }

        System.out.println(String.format("Total Revenue: Rs. %.2f ", totalRevenue));
        sendEmailReport(totalRevenue, start, end);
    }

    private void sendEmailReport(double revenue, LocalDateTime start, LocalDateTime end) {
        System.out.println("Emailing report to salesteam@supersaving.lk : Revenue from " + start + " to " + end + " is $" + revenue);
    }
}