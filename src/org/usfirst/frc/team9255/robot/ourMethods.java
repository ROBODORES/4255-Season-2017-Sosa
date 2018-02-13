package org.usfirst.frc.team9255.robot;

public class ourMethods {
	public static double map(double value, double fromMin, double fromMax, double toMin, double toMax)	{
		return (((value-fromMin)/fromMax)*(toMax-toMin))+toMin;
	}
}
