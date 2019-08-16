import java.util.Arrays;
import java.util.Collections;

public class Dispatcher {
    private static int arr[][];
    private static int floorQuant;
    private static int passQuant;
    private static int peopleNeedUp = 0;
    private static int peopleNeedDown = 0;
    private static int freePlace;
    private static int[] temp;


    public static void main(String[] args) {

        //input data
        floorQuant = Util.random(20, 5);
        passQuant = Util.random(10, 0);
        arr = new int[floorQuant][passQuant];
        setArr();

        // Start
        System.out.println("\n==== Hello! This is an elevator in the building! ====\n");
        System.out.println("The height of building is " + floorQuant + " floors\n");
        System.out.println("Its " + passQuant + " people on the every floor\n");
        show();
        int i = 1;

        do {
            System.out.println("*** Step " + i + " ***");

            boarding();
            moving();
            exit();
            show();
            i++;
        } while (getZeros(arr) != passQuant * floorQuant);
        System.out.println("----There are no people in the building!----");
        System.exit(1);
    }

    public static void setArr() {
        for (int i = arr.length - 1, n = arr.length; i >= 0; i--, n--) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = Passenger.getNeededFloor();
                Arrays.stream(arr[i]).distinct();
                if (arr[i][j] == n) {
                    if (n <= floorQuant / 2) {
                        arr[i][j] = Util.random(floorQuant, n + 1);
                    } else {
                        arr[i][j] = Util.random(n, 1);
                    }
                }
            }
        }
    }

    public static void show() {
        for (int i = arr.length - 1, n = arr.length; i >= 0; i--, n--) {
            System.out.print(" floor" + n + "--");
            for (int j = 0; j < arr[i].length; j++) {
                System.out.printf("%3d", arr[i][j]);
            }
            System.out.print(" | ");
            if (Elevator.getState() == 1 && Floor.getCurrentFloor() == n) {
                System.out.print("^");
                for (int b = 0; b < Elevator.getMaxCapacity(); b++) {
                    System.out.printf("%3d", temp[b]);
                }
            } else if (Elevator.getState() == 2 && Floor.getCurrentFloor() == n) {
                System.out.print("V");
                for (int b = 0; b < Elevator.getMaxCapacity(); b++) {
                    System.out.printf("%3d", temp[b]);
                }
            }
            System.out.println();
        }
    }

    public static int getPeopleNeedUp(int floor) {
        int needUp = 0;
        for (int j = 0; j < arr[floor - 1].length; j++) {
            if (arr[floor - 1][j] >= floor && arr[floor - 1][j] > 0) {
                needUp++;
            }
        }
        return needUp;
    }

    public static int getPeopleNeedDown(int floor) {
        int needDown = 0;
        for (int j = 0; j < arr[floor - 1].length; j++) {
            if (arr[floor - 1][j] < floor && arr[floor - 1][j] > 0) {
                needDown++;
            }
        }
        return needDown;
    }

    public static void boarding() {
        /*peopleNeedUp = 0;
        peopleNeedDown = 0;*/
        if (Floor.getCurrentFloor() == 1) {
            Elevator.setState(1);
        } else if (Floor.getCurrentFloor() == floorQuant) {
            Elevator.setState(2);
        }

        if (passQuant == 0) { // there are no people
            Elevator.setState(0);
            System.out.println("\n ----Elevator is waiting now----\n");
            System.exit(1);
        } else if (passQuant > 0) {
            peopleNeedUp = getPeopleNeedUp(Floor.getCurrentFloor());
            peopleNeedDown = getPeopleNeedDown(Floor.getCurrentFloor());
            enter();
        }
    }


    public static void enter() {
        int[] elevator = new int[Elevator.getMaxCapacity()];
        int[] tempUp = new int[passQuant];
        int[] tempDown = new int[passQuant];

        getFreePlace();

        if (peopleNeedUp > 0 && Elevator.getState() == 1 || Elevator.getState() == 0 && peopleNeedUp > 0) {
            for (int j = 0; j < passQuant; j++) {
                if (arr[Floor.getCurrentFloor() - 1][j] >= Floor.getCurrentFloor() && arr[Floor.getCurrentFloor() - 1][j] > 0) {
                    tempUp[j] = arr[Floor.getCurrentFloor() - 1][j];
                }
            }
            tempUp = reverseSort(tempUp);

            if (peopleNeedUp >= freePlace) {
                for (int j = 0; j < freePlace; j++) {
                    if (tempUp[j] > 0) {
                        elevator[j] = tempUp[j];
                    }
                }
                Elevator.setCurrentCapacity(Elevator.getMaxCapacity());
            } else if (peopleNeedUp < freePlace) {
                for (int j = 0; j < peopleNeedUp; j++) {
                    if (tempUp[j] > 0) {
                        elevator[j] = tempUp[j];
                    }
                }
                Elevator.setCurrentCapacity(Elevator.getCurrentCapacity() + peopleNeedUp);
            }

        } else if (peopleNeedDown > 0 && Elevator.getState() == 2 || Elevator.getState() == 0 && peopleNeedDown > 0) {
            for (int j = 0; j < passQuant; j++) {
                if (arr[Floor.getCurrentFloor() - 1][j] < Floor.getCurrentFloor() && arr[Floor.getCurrentFloor() - 1][j] > 0) {
                    tempDown[j] = arr[Floor.getCurrentFloor() - 1][j];
                }
            }
            tempDown = reverseSort(tempDown);

            if (peopleNeedDown >= freePlace) {
                for (int j = 0; j < freePlace; j++) {
                    if (tempDown[j] > 0) {
                        elevator[j] = tempDown[j];
                    }
                }
                Elevator.setCurrentCapacity(Elevator.getMaxCapacity());
            } else if (peopleNeedDown < freePlace) {
                for (int j = 0; j < peopleNeedDown; j++) {
                    if (tempDown[j] > 0) {
                        elevator[j] = tempDown[j];
                    }
                }
                Elevator.setCurrentCapacity(Elevator.getCurrentCapacity() + peopleNeedDown);
            }
        }

        for (int j = 0; j < Elevator.getMaxCapacity(); j++) {
            for (int i = 0; i < arr[Floor.getCurrentFloor() - 1].length; i++) {
                if (arr[Floor.getCurrentFloor() - 1][i] != 0 && arr[Floor.getCurrentFloor() - 1][i] == elevator[j]) {
                    arr[Floor.getCurrentFloor() - 1][i] = 0;
                    break;
                }
            }
        }
        Elevator.addToComingPeople(elevator);
        temp = Elevator.getComingPeople();
    }

    public static int findMinNeeded() {
        int minNeededFloor = floorQuant+1;
        for (int j = 0; j < Elevator.getComingPeople().length; j++) {
            if (Elevator.getComingPeople()[j] != 0) {
                minNeededFloor = Math.min(minNeededFloor, Elevator.getComingPeople()[j]);
            }
        }
        return minNeededFloor;
    }

    public static int findMaxNeededFloor() {
        int maxNeededFloor = -1;
        for (int j = 0; j < Elevator.getComingPeople().length; j++) {
            if (Elevator.getComingPeople()[j] != 0) {
                maxNeededFloor = Math.max(maxNeededFloor, Elevator.getComingPeople()[j]);
            }
        }
        return maxNeededFloor;
    }

    public static int getFreePlace() {
        freePlace = Elevator.getMaxCapacity() - Elevator.getCurrentCapacity();
        return freePlace;
    }

    public static void moving() {

        // selection a floor for stopping
        int max = findMaxNeededFloor();
        int min = findMinNeeded();
        int floorUp = Floor.getCurrentFloor() + 1;
        int floorDown = Floor.getCurrentFloor() - 1;
        getFreePlace();

        // if elevator is moving up
        if (Elevator.getState() == 1) {
            if (freePlace == 0) {
                Floor.setCurrentFloor(min);
            } else {
                int nextFloor = 0;
                for (int i = floorUp, k = floorQuant; i <= floorQuant; i++, k--) {
                    if (Floor.getButton(i) == true) {
                        nextFloor = i;
                        break;
                    } else {
                        if (min != floorQuant+1) {
                            nextFloor = min;
                            break;
                        } else {
                            if (Floor.getButton(k) == false) {
                                nextFloor = k;
                                break;
                            } else {
                                Elevator.setState(2);
                            }
                        }
                    }
                }
                Floor.setCurrentFloor(nextFloor);
            }
            // if elevator is moving down
        } else if (Elevator.getState() == 2) {
            if (freePlace == 0) {
                Floor.setCurrentFloor(max);
            } else {
                int nextFloor = 0;
                for (int k = floorDown, i = 1; k >= 1; k--, i++) {
                    if (Floor.getButton(k) == false) {
                        nextFloor = k;
                        break;
                    } else {
                        if (max != -1) {
                            nextFloor = max;
                            break;
                        } else {
                            if (Floor.getButton(i) == true) {
                                nextFloor = i;
                                break;
                            } else {
                                Elevator.setState(1);
                                nextFloor = 1;
                            }
                        }
                    }
                }
                Floor.setCurrentFloor(nextFloor);
            }
        }
    }

    public static void exit() {
        int count = 0;
        for (int j = 0; j < Elevator.getMaxCapacity(); j++) {
            if (Elevator.getComingPeople()[j] == Floor.getCurrentFloor()) {
                count++;
                Elevator.getComingPeople()[j] = 0;
            }
        }
        Elevator.setCurrentCapacity(Elevator.getCurrentCapacity() - count);
    }

    public static int getZeros(int[][] numbers) {
        int count = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                if (numbers[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int getFloorQuant() {
        return floorQuant;
    }

    public static int[] reverseSort(int[] a) {
        a = Arrays.stream(a).boxed()
                .sorted(Collections.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();
        return a;
    }
}

