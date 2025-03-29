class item {
    String item_code, item_name, manufacturer_name;
    double price;
    String manufacturing_date, expiry_data, weightOrSize;

    public item(String item_code,String name, String manufacturer_name, double price,String weightOrSize, String manufacturing_date, String expiry_data){
        this.item_code = item_code;
        this.item_name = name;
        this.manufacturer_name = manufacturer_name;
        this.expiry_data = expiry_data;
        this.price = price;
        this.manufacturing_date = manufacturing_date;
        this.weightOrSize = weightOrSize;
    }
}