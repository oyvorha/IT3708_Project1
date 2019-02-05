import java.util.ArrayList;
import java.util.Objects;

public class Customer implements Node {

    private int customerNo;
    private int demand;
    private int serviceDuration;
    private Coordinate coordinate;

    Customer(int customerNo, int serviceDuration, int demand, Coordinate coordinate) {
        this.customerNo = customerNo;
        this.serviceDuration = serviceDuration;
        this.demand = demand;
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "Customer "+getCustomerNo();
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


}
