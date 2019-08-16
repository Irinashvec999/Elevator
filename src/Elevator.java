import java.util.Arrays;
import java.util.Collections;

public class Elevator {
    private static int state = 0; // up = 1, down = 2, waiting = 0
    private static final int MAX_CAPACITY = 5;
    private static int currentCapacity = 0;
    private static int[] comingPeople = new int[MAX_CAPACITY];


    public static int getCurrentCapacity() {
        return currentCapacity;
    }


    public static void addToComingPeople(int[] peopleArr) {

        for (int k = 0; k < MAX_CAPACITY; k++) {
            if (comingPeople[k] == 0) {
                comingPeople[k] = peopleArr[k];
            }
        }
        Arrays.sort(comingPeople);
    }


    public static int[] getComingPeople() {
        return comingPeople;
    }

    public static int getState() {
        return state;
    }

    public static int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public static void setCurrentCapacity(int currentCapacity) {
        Elevator.currentCapacity = currentCapacity;
    }

    public static void setState(int state) {
        Elevator.state = state;
    }
}
