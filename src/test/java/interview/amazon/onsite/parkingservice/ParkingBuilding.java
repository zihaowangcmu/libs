package interview.amazon.onsite.parkingservice;

import java.util.ArrayList;
import java.util.List;

public class ParkingBuilding {

    ParkingManager parkingManager;
    List<Sensor> enterSensors;
    List<Sensor> exitSensors;

    public void init() {
        this.parkingManager = new ParkingManager();
        initEnterSensors();
        initExitSensors();
    }

    private void initEnterSensors() {
        this.enterSensors = new ArrayList<>();
        // add all enter sensors
    }

    private void initExitSensors() {
        this.exitSensors = new ArrayList<>();
        // add all exit sensors
    }
}
