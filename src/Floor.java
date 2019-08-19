public class Floor {
    private static int currentFloor = 1;
    private static boolean button; // up = true, down = false


    public static int getCurrentFloor() {
        return currentFloor;
    }

    public static boolean getButton(int floor) {
        if (Dispatcher.getPeopleNeedUp(floor) > 0 && Elevator.getState() == 1 ||
                Floor.getCurrentFloor() == 1) {
            setButton(true);
        } else if (Dispatcher.getPeopleNeedDown(floor) > 0 && Elevator.getState() == 2 ||
                Floor.getCurrentFloor() == Dispatcher.getFloorQuant()) {
            setButton(false);
        }
        return button;
    }


    public static void setCurrentFloor(int currentFloor) {
        Floor.currentFloor = currentFloor;
    }

    public static void setButton(boolean button) {
        Floor.button = button;
    }
}
