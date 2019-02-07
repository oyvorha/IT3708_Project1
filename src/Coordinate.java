public class Coordinate {

        private int x;
        private int y;


        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getEuclidianDistance(Coordinate node2){
                return Math.sqrt(Math.pow((this.x - node2.getX()), 2) + Math.pow((this.y-node2.getY()), 2));
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
}
