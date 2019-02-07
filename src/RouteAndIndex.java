public class RouteAndIndex {

    private Route route;
    private int index;
    private Depot closestDepot;
    private boolean valid;

    public RouteAndIndex(Route route, int index, Depot closestDepot, boolean valid) {
        this.route = route;
        this.index = index;
        this.closestDepot = closestDepot;
        this.valid = valid;
    }

    public int getIndex() {
        return index;
    }

    public Route getRoute() {
        return route;
    }

    public Depot getClosestDepot() {
        return closestDepot;
    }

    public boolean getValid() {
        return this.valid;
    }
}
