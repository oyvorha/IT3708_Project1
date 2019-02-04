import java.util.ArrayList;
import java.util.Objects;

public class Customer {

    private int customerNo;
    private int demand;
    private Vehicle visitingVehicle;
    private Coordinate coordinate;

    Customer(int customer_no, int demand, Coordinate coordinate) {
        this.customerNo = customer_no;
        this.demand = demand;
        this.coordinate = coordinate;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public int getDemand() {
        return demand;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setVisitingVehicle(Vehicle visitingVehicle) {
        this.visitingVehicle = visitingVehicle;
    }

    public boolean visited() {
        return !Objects.isNull(this.visitingVehicle);
    }

}
