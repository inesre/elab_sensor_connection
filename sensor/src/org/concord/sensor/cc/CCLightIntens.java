package org.concord.sensor.cc;

import org.concord.sensor.*;
import org.concord.waba.extra.util.*;

import org.concord.framework.data.stream.*;

public class CCLightIntens extends Sensor
	implements CCModes
{
	float  			[]lightData = new float[CCSensorProducer.BUF_SIZE/2];
	int  			[]lightIntData = new int[CCSensorProducer.BUF_SIZE/2];

	/*
	  Lux=(input(mV)-offset(mV))/sensitivity(mV/Lux)

		                    calculated	standard	maximum	
		offset	sensitivity	range	    deviation	deviation	
		5.11	0.0206	    121209	    1.2%	     1.5%	   125k Lux Range
		6.42	0.5655	      4409	    0.8%	     1.3%      4k Lux range
	*/

	// A = 1/sensitivity
	// B = -offset/sensitivity
	float AHigh  = 1f/0.0206f;
	float BHigh  = -5.11f/0.0206f;
	float ALow   = 1f/0.5655f;
	float BLow   = -6.42f/0.5655f;

	public final static int		HIGH_LIGHT_MODE 			= 0;
	public final static int		LOW_LIGHT_MODE 			= 1;
	int				lightMode = HIGH_LIGHT_MODE;

	PropObject rangeProp = new PropObject("Range", "Range", PROP_RANGE, rangeNames);
	PropObject speedProp = new PropObject("Speed", "Speed", PROP_SPEED, speed1Names);

	public static String [] rangeNames = {"Bright Light", "Dim Light"};
	public static String [] speed1Names = {3 + speedUnit, 200 + speedUnit, 400 + speedUnit};
	public static String [] speed2Names = {3 + speedUnit, 200 + speedUnit};

	CCLightIntens(boolean init, short type, SensorProducer p)
	{
		super(init, type, p);

	    activeChannels = 2;
		defQuantityName = "Intensity";

		dDesc.setNextSampleOffset(2);
		dDesc.setChannelPerSample(2);
		dDesc.setDt(0.0f);
		dDesc.setDataOffset(0);

		dEvent.setDataDescription(dDesc);
		dEvent.setNumSamples(1);
		dEvent.setData(lightData);
		dEvent.setIntData(lightIntData);

		addProperty(rangeProp);
		addProperty(speedProp);

		if(init){
			lightMode = 0;

			calibrationDesc = new CalibrationDesc();
			calibrationDesc.addCalibrationParam(new CalibrationParam(0,AHigh));
			calibrationDesc.addCalibrationParam(new CalibrationParam(1,BHigh));
			calibrationDesc.addCalibrationParam(new CalibrationParam(2,ALow));
			calibrationDesc.addCalibrationParam(new CalibrationParam(3,BLow));
		}

		unit = "lx";
	}

	public boolean visValueChanged(PropObject po)
	{
		int index = po.getVisIndex();
		if(po == rangeProp){
			if(index == 0){
				speedProp.setVisPossibleValues(speed1Names);
			} else {
				speedProp.setVisPossibleValues(speed2Names);
			}
		}

		return true;
	}

	public CalibrationDesc getCalibrationDesc()
	{
		int lightMode = rangeProp.getIndex();

		CalibrationParam cp = calibrationDesc.getCalibrationParam(0);
		if(cp != null) cp.setAvailable(lightMode == HIGH_LIGHT_MODE);
		cp = calibrationDesc.getCalibrationParam(1);
		if(cp != null) cp.setAvailable(lightMode == HIGH_LIGHT_MODE);
		cp = calibrationDesc.getCalibrationParam(2);
		if(cp != null) cp.setAvailable(lightMode == LOW_LIGHT_MODE);
		cp = calibrationDesc.getCalibrationParam(3);
		if(cp != null) cp.setAvailable(lightMode == LOW_LIGHT_MODE);

		return calibrationDesc;
	}

	public Object getInterfaceMode()
	{
		int intMode =  A2D_24_MODE;
		int speedIndex = speedProp.getIndex();

		if(speedIndex == 0){
			intMode = A2D_24_MODE;
			activeChannels = 2;
		} else if(speedIndex == 1){
			intMode = A2D_10_2_CH_MODE;
			activeChannels = 2;
		} else if(speedIndex == 2){
			intMode = A2D_10_CH_0_MODE;
			activeChannels = 1;
		}

		interfaceMode = CCInterface2.getMode(getInterfacePort(), intMode);
		return interfaceMode;
	}

	public int getPrecision()
	{
		return 0;
	}

	public int  getActiveCalibrationChannels(){return 1;}

	public boolean startSampling(DataEvent e)
	{
		lightMode = rangeProp.getIndex();

		dEvent.type = e.type;
		dDesc.setDt(e.getDataDescription().getDt());
		dDesc.setChannelPerSample(e.getDataDescription().getChannelPerSample());
		dDesc.setChannelPerSample(1);
		dDesc.setNextSampleOffset(1);

		dDesc.setTuneValue(e.getDataDescription().getTuneValue());
	    return super.startSampling(dEvent);
    }

	public boolean dataArrived(DataEvent e)
	{
		DataStreamDescription eDesc = e.getDataDescription();

		dEvent.type = e.type;
		int[] data = e.getIntData();
		int nOffset = eDesc.getDataOffset();

		int nextSampleOff = eDesc.getNextSampleOffset();

		int  	chPerSample = eDesc.getChannelPerSample();
		int ndata = e.getNumSamples()*nextSampleOff;

		if(ndata < nextSampleOff) return false;
		int dataIndex = 0;	
		for(int i = 0; i < ndata; i+=nextSampleOff){
			if(lightMode == HIGH_LIGHT_MODE){
				int v = data[nOffset+i];
				lightIntData[dataIndex] = v;
				lightData[dataIndex] = AHigh*dDesc.getTuneValue()*(float)v+BHigh;
			}else{
				int v = data[nOffset+i+1];
				lightIntData[dataIndex] = v;
				lightData[dataIndex] = ALow*dDesc.getTuneValue()*(float)v+BLow;
			}
			if(lightData[dataIndex] < 0f){
				lightData[dataIndex] = 0f;
			}
			dataIndex++;
		}
		dEvent.setNumSamples(dataIndex);

		return super.dataArrived(dEvent);
	}

	public void  calibrationDone(float []row1,float []row2,float []calibrated)
	{
		if(row1 == null || calibrated == null) return;
		float x1 = row1[0];
		float x2 = row1[1];
		float y1 = calibrated[0];
		float y2 = calibrated[1];
		float A = (y2 - y1)/(x2 - x1);
		float B = y2 - A*x2;
		if(lightMode == HIGH_LIGHT_MODE){
			AHigh = A;
			BHigh = B;
			if(calibrationDesc != null){
				CalibrationParam p = calibrationDesc.getCalibrationParam(0);
				if(p != null) p.setValue(AHigh);
				p = calibrationDesc.getCalibrationParam(1);
				if(p != null) p.setValue(BHigh);
			}
		}else if(lightMode == LOW_LIGHT_MODE){
			ALow = A;
			BLow = B;
			if(calibrationDesc != null){
				CalibrationParam p = calibrationDesc.getCalibrationParam(2);
				if(p != null) p.setValue(ALow);
				p = calibrationDesc.getCalibrationParam(3);
				if(p != null) p.setValue(BLow);
			}
		}
	}

	public void calibrationDescReady(){
		if(calibrationDesc == null) return;
		CalibrationParam p = calibrationDesc.getCalibrationParam(0);
		if(p != null && p.isValid()){
			AHigh = p.getValue();
		}
		p = calibrationDesc.getCalibrationParam(1);
		if(p != null && p.isValid()){
			BHigh = p.getValue();
		}
		p = calibrationDesc.getCalibrationParam(2);
		if(p != null && p.isValid()){
			ALow = p.getValue();
		}
		p = calibrationDesc.getCalibrationParam(3);
		if(p != null && p.isValid()){
			BLow = p.getValue();
		}
	}
}
