public class Customer {

    private int customerNo;
    private int demand;
    private Vehicle visitingVehicle;

    Customer(int customer_no, int demand) {
        this.customerNo = customer_no;
        this.demand = demand;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public int getDemand() {
        return demand;
    }

    public void setVisitingVehicle(Vehicle visitingVehicle) {
        this.visitingVehicle = visitingVehicle;
    }
}
