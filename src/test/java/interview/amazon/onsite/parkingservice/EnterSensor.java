package interview.amazon.onsite.parkingservice;

public class EnterSensor implements Sensor{

    @Override
    public Car sense(SensorInfo sensorInfo) {
        return new Car(sensorInfo.getLp(), sensorInfo.getType());
    }
}
