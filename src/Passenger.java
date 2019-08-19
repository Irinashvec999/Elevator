public class Passenger {
    private static int neededFloor;

    public static int getNeededFloor() {
        Passenger.neededFloor = Util.random(Dispatcher.getFloorQuant(), 1);
        return neededFloor;
    }
}
