package org.concord.sensor.vernier.labpro;

import org.concord.sensor.device.SensorDeviceTest;

public class LabProSensorDeviceTest extends SensorDeviceTest {

	@Override
	public void setup() {
		device = new LabProSensorDevice();
		openString = "usb";
		msToWaitBeforeReadingData = 1000;
	}

}
