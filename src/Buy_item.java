class buy_item {
    item product;
    double quantity;
    double discount;
    double unit_price;
    public String name;
    double total_price; 
    
    public buy_item(item product, double qty, double discount) {
        this.product = product;
        this.quantity = qty;
        this.discount = discount;
        this.unit_price = product.price;
        this.name = product.item_name;
        total_price = unit_price * quantity;
    }
        
    public double netPrice(){
        return total_price - this.discountPrice();
    }
    public double discountPrice(){
        return total_price * this.discount / 100;
    }
}
    