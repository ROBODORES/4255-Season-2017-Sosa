/**
 * most recent code
 * Bot Name: SOSA
 */
package org.usfirst.frc.team9255.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
	

public class Robot extends IterativeRobot {
	Joystick jL = new Joystick (0);
    Joystick jR = new Joystick (1);
    Joystick jS = new Joystick (2);
    
    I2C pixyPort = new I2C(edu.wpi.first.wpilibj.I2C.Port.kOnboard, 0x54);
    Pixy cam = new Pixy(pixyPort);    
    int drivedist = (int)((8)*490); //Enter feet in the parenthesis
    int autodrive = (int)((6)*490);
    int turndist = 15;
    
    //Pixy Variables
    double gearset = 250.0;
    int[] pobjectx = new int[20];
    int[] pobjecty = new int[20];
    int lset;
    int rset;
    int pfreq = 1;
    boolean deploy = false;
    
    boolean gearReady = false;
    
    String autoStep;
    String gearStep;
    String mode = "regular";
    String shootStep = "center";
    
    Encoder enc1 = new Encoder(0, 1, true, Encoder.EncodingType.k4X);
    Encoder enc2 = new Encoder(2, 3, true, Encoder.EncodingType.k4X);
    DigitalInput linput = new DigitalInput(4);
    DigitalInput rinput = new DigitalInput(5);
    
    
    byte[] buff = new byte[1];
    
    Solenoid hold = new Solenoid(0);
    Solenoid push = new Solenoid(1);
    Solenoid hopper = new Solenoid(2);
    
    CANTalon lD1 = new CANTalon(3);
    CANTalon lD2 = new CANTalon(4);
    CANTalon rD1 = new CANTalon(1);
    CANTalon rD2 = new CANTalon(2);
    
    Spark shooter = new Spark(0);
    Spark climber = new Spark(1);
   
    AnalogInput sonar = new AnalogInput(0);
    AnalogInput pressure = new AnalogInput(1);
    
    int pushtimer = 20;
    int shootimer = 120;
    double trackSpeed = 1.0;
   
    boolean dir = true;
    boolean clicked = false;
    boolean aut = true;
    boolean gear = false;
    boolean goshoot = false;
    
    String s = "SPEED";
    
    boolean tclicked = false;
    boolean torque = false;
    
	final String leftauto = "left";
	final String middleauto = "middle";
	final String rightauto = "right";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();

    double son = sonar.getVoltage();
    
    CameraServer camserv;
    
	@Override
	public void robotInit() {
		
	    enc1.setMaxPeriod(.1); enc1.setMinRate(10); enc1.setDistancePerPulse(5); enc1.setReverseDirection(true); enc1.setSamplesToAverage(7);
	    enc2.setMaxPeriod(.1); enc2.setMinRate(10); enc2.setDistancePerPulse(5); enc2.setReverseDirection(true); enc2.setSamplesToAverage(7);
	    
	    AnalogInput.setGlobalSampleRate(62500);
	    
	    camserv = CameraServer.getInstance();
	    camserv.startAutomaticCapture(0);
	    camserv.startAutomaticCapture(1);
	}

	@Override
	public void autonomousInit() {
		if (!linput.get() && rinput.get()){
			autoSelected = leftauto;
		}
		else if (linput.get() && !rinput.get()){
			autoSelected = rightauto;
		}
		else{
			autoSelected = middleauto;
		}
		System.out.println("Auto selected: " + autoSelected);
		reset();
		autoStep = "forward";
		deploy = false;
		gear = true;
		hold.set(true);
		push.set(false);
		
	}

	@Override
	public void autonomousPeriodic() {
		double renc = -enc1.getRaw();
		double lenc = enc2.getRaw();
		SmartDashboard.putString("AutoMode", autoStep);
		System.out.println(lenc + "  " + renc);
		double dist = sonar.getVoltage();
		printPressure();
		hopper.set(false);
		
		
		switch (autoSelected) {
		case leftauto:
			switch (autoStep) {
			case "forward":
				hold.set(true);
				push.set(false);
					lD1.set(-0.4);
					lD2.set(-0.4);
					rD1.set(-0.4);
					rD2.set(-0.4);
				if (lenc+renc < -2513.0*5.0) {
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
				}
				if (lenc+renc < -2513.0*2.0) {
					hold.set(false);
				} else {
					hold.set(true);
				}
				if (lenc+renc < -2540.0*2.0) {
					push.set(true);
				} else {
					push.set(false);
				}
				break;
			}
			break;
		case middleauto:
		default:
			//CalGames Auto
			/*switch (autoStep) {
			case "forward":
				hold.set(true);
				push.set(false);
				deploy = false;
				
				if (dist > 0.3) {
					if (lenc+renc <= drivedist*2) {
						lD1.set(trackSpeed);
						lD2.set(trackSpeed);
						rD1.set(trackSpeed);
						rD2.set(trackSpeed);
					} else {
						lD1.set(0);
						lD2.set(0);
						rD1.set(0);
						rD2.set(0);
					}
				} 
				else {
					autoStep = "gear";
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
					reset();
				}
				break;
			case "gear":
				if (gear) {
					hold.set(false);
					if (pushtimer <= 0) {
					  push.set(true);
					  if (pushtimer <= -20) {
						  lD1.set(-0.6);
					      lD2.set(-0.6);
						  rD1.set(-0.6);
						  rD2.set(-0.6);
						  if (pushtimer <= -40) {
							  gear = false;
						  } 
						  else {
							  pushtimer--;
						  }
					  } 
					  else {
						  pushtimer--;
					  }
					} 
					else {
					  pushtimer--;
					}
				} 
				else {
					pushtimer = 20;
					hold.set(true);
					push.set(false);
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
				}
				break;
			}*/
			
			//System.out.println("deploy" + deploy + " see" + see);
			switch (autoStep) {
			case "forward":
				hold.set(true);
				push.set(false);
				
					autoStep = "track";
					deploy = false;
					reset();
				break;
			case "track":
				hold.set(true);
				push.set(false);
				
				if (!deploy) {
					if (dist < 0.8) {
						
						if (cam.canPixySee()) {
							System.out.println("can see");
							pobjectx = cam.getPixyX();
							//pobjecty = cam.getPixyY();
							System.out.println("Pixy x0: " + pobjectx[0]);
							System.out.println("Pixy x1: " + pobjectx[1]);
							if (pobjectx[0] == 0) {
									if (pobjectx[1] <= gearset+5 && pobjectx[1] >= gearset-5) {
										System.out.println("ready");
										lD1.set(0);
										lD2.set(0);
										rD1.set(0);
										rD2.set(0);
										deploy = true;
									} else {
										double perror = ourMethods.map((double)pobjectx[1], 0, 500, -0.75, 0.75);
										System.out.println("X =" + perror);
										if (perror < 0.3 && perror > 0.0) {
											perror = 0.3;
										}
										if (perror > -0.3 && perror < 0.0) {
											perror = -0.3;
										}
										lD1.set(perror);
										lD2.set(perror);
										rD1.set(-perror);
										rD2.set(-perror);
								}
							} else {
								if (pobjectx[1] == 0) {
								if (pobjectx[0] <= gearset+5 && pobjectx[0] >= gearset-5) {
									System.out.println("ready");
									lD1.set(0);
									lD2.set(0);
									rD1.set(0);
									rD2.set(0);
									deploy = true;
								} else {
									double perror = ourMethods.map((double)pobjectx[0], 0, 500, -0.75, 0.75);
									System.out.println("X =" + perror);
									if (perror < 0.3 && perror > 0.0) {
										perror = 0.3;
									}
									if (perror > -0.3 && perror < 0.0) {
										perror = -0.3;
									}
									lD1.set(perror);
									lD2.set(perror);
									rD1.set(-perror);
									rD2.set(-perror);
								}
								} else {
									if (pobjectx[0] < pobjectx[1]) {
										if (pobjectx[0] <= gearset+5 && pobjectx[0] >= gearset-5) {
											System.out.println("ready");
											lD1.set(0);
											lD2.set(0);
											rD1.set(0);
											rD2.set(0);
											deploy = true;
										} else {
											double perror = ourMethods.map((double)pobjectx[0], 0, 500, -0.75, 0.75);
											System.out.println("X =" + perror);
											if (perror < 0.3 && perror > 0.0) {
												perror = 0.3;
											}
											if (perror > -0.3 && perror < 0.0) {
												perror = -0.3;
											}
											lD1.set(perror);
											lD2.set(perror);
											rD1.set(-perror);
											rD2.set(-perror);
										}
									} else {
										if (pobjectx[1] <= gearset+5 && pobjectx[1] >= gearset-5) {
											System.out.println("ready");
											lD1.set(0);
											lD2.set(0);
											rD1.set(0);
											rD2.set(0);
											deploy = true;
										} else {
											double perror = ourMethods.map((double)pobjectx[1], 0, 500, -0.75, 0.75);
											System.out.println("X =" + perror);
											if (perror < 0.3 && perror > 0.0) {
												perror = 0.3;
											}
											if (perror > -0.3 && perror < 0.0) {
												perror = -0.3;
											}
											lD1.set(perror);
											lD2.set(perror);
											rD1.set(-perror);
											rD2.set(-perror);
									}
									}
								}
							}
								//}
						}
						else {
							deploy = true;
							    lD1.set(0);
							    lD2.set(0);
							    rD1.set(0);
							    rD2.set(0);
						}
					} 
					else { 
						lD1.set(trackSpeed/2);
						lD2.set(trackSpeed/2);
						rD1.set(trackSpeed/2);
						rD2.set(trackSpeed/2);
					}
				} 
				else {
					if (dist > 0.3) {
						lD1.set(trackSpeed/2);
						lD2.set(trackSpeed/2);
						rD1.set(trackSpeed/2);
						rD2.set(trackSpeed/2);
					} 
					else {
						autoStep = "gear";
						lD1.set(0);
						lD2.set(0);
						rD1.set(0);
						rD2.set(0);
					}
				}
				break;
			case "gear":
				if (gear) {
					hold.set(false);
					if (pushtimer <= 0) {
					  push.set(true);
					  if (pushtimer <= -20) {
						  lD1.set(-0.6);
					      lD2.set(-0.6);
						  rD1.set(-0.6);
						  rD2.set(-0.6);
						  if (pushtimer <= -40) {
							  gear = false;
						  } 
						  else {
							  pushtimer--;
						  }
					  } 
					  else {
						  pushtimer--;
					  }
					} 
					else {
					  pushtimer--;
					}
				} 
				else {
					pushtimer = 20;
					hold.set(true);
					push.set(false);
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
				}
				break;
			}
			break;
		case rightauto:
			switch (autoStep) {
			case "forward":
				hold.set(true);
				push.set(false);
					lD1.set(0.4);
					lD2.set(0.4);
					rD1.set(0.4);
					rD2.set(0.4);
				if (lenc+renc > 2513.0*2.0) {
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
					reset();
					autoStep = "turn";
				}
				break;
			case "turn":
				hold.set(true);
				push.set(false);
				for (int i = 0; i < 170; i++) {
					lD2.set(-0.6);
					lD1.set(-0.6);
					rD1.set(0.6);
					rD2.set(0.6);
				}
					autoStep = "track";
					deploy = false;
					//pixyData("front");
					reset();
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
				break;
			case "track":
				hold.set(true);
				push.set(false);
				
				if (!deploy) {
					if (dist < 0.8) {
						
						if (cam.canPixySee()) {
							System.out.println("can see");
							pobjectx = cam.getPixyX();
							//pobjecty = cam.getPixyY();
							System.out.println("Pixy x0: " + pobjectx[0]);
							System.out.println("Pixy x1: " + pobjectx[1]);
							if (pobjectx[0] == 0) {
									if (pobjectx[1] <= gearset+5 && pobjectx[1] >= gearset-5) {
										System.out.println("ready");
										lD1.set(0);
										lD2.set(0);
										rD1.set(0);
										rD2.set(0);
										deploy = true;
									} else {
										double perror = ourMethods.map((double)pobjectx[1], 0, 500, -0.75, 0.75);
										System.out.println("X =" + perror);
										if (perror < 0.3 && perror > 0.0) {
											perror = 0.3;
										}
										if (perror > -0.3 && perror < 0.0) {
											perror = -0.3;
										}
										lD1.set(perror);
										lD2.set(perror);
										rD1.set(-perror);
										rD2.set(-perror);
								}
							} else {
								if (pobjectx[1] == 0) {
								if (pobjectx[0] <= gearset+5 && pobjectx[0] >= gearset-5) {
									System.out.println("ready");
									lD1.set(0);
									lD2.set(0);
									rD1.set(0);
									rD2.set(0);
									deploy = true;
								} else {
									double perror = ourMethods.map((double)pobjectx[0], 0, 500, -0.75, 0.75);
									System.out.println("X =" + perror);
									if (perror < 0.3 && perror > 0.0) {
										perror = 0.3;
									}
									if (perror > -0.3 && perror < 0.0) {
										perror = -0.3;
									}
									lD1.set(perror);
									lD2.set(perror);
									rD1.set(-perror);
									rD2.set(-perror);
								}
								} else {
									if (pobjectx[0] < pobjectx[1]) {
										if (pobjectx[0] <= gearset+5 && pobjectx[0] >= gearset-5) {
											System.out.println("ready");
											lD1.set(0);
											lD2.set(0);
											rD1.set(0);
											rD2.set(0);
											deploy = true;
										} else {
											double perror = ourMethods.map((double)pobjectx[0], 0, 500, -0.75, 0.75);
											System.out.println("X =" + perror);
											if (perror < 0.3 && perror > 0.0) {
												perror = 0.3;
											}
											if (perror > -0.3 && perror < 0.0) {
												perror = -0.3;
											}
											lD1.set(perror);
											lD2.set(perror);
											rD1.set(-perror);
											rD2.set(-perror);
										}
									} else {
										if (pobjectx[1] <= gearset+5 && pobjectx[1] >= gearset-5) {
											System.out.println("ready");
											lD1.set(0);
											lD2.set(0);
											rD1.set(0);
											rD2.set(0);
											deploy = true;
										} else {
											double perror = ourMethods.map((double)pobjectx[1], 0, 500, -0.75, 0.75);
											System.out.println("X =" + perror);
											if (perror < 0.3 && perror > 0.0) {
												perror = 0.3;
											}
											if (perror > -0.3 && perror < 0.0) {
												perror = -0.3;
											}
											lD1.set(perror);
											lD2.set(perror);
											rD1.set(-perror);
											rD2.set(-perror);
									}
									}
								}
							}
								//}
						}
						else {
							pobjectx = cam.getPixyX();
							pobjecty = cam.getPixyY(); //keep searching
							    lD1.set(0);
							    lD2.set(0);
							    rD1.set(0);
							    rD2.set(0);
						}
					} 
					else { 
						lD1.set(trackSpeed/2);
						lD2.set(trackSpeed/2);
						rD1.set(trackSpeed/2);
						rD2.set(trackSpeed/2);
					}
				} 
				else {
					if (dist > 0.3) {
						lD1.set(trackSpeed/2);
						lD2.set(trackSpeed/2);
						rD1.set(trackSpeed/2);
						rD2.set(trackSpeed/2);
					} 
					else {
						autoStep = "gear";
						lD1.set(0);
						lD2.set(0);
						rD1.set(0);
						rD2.set(0);
					}
				}
				break;
			case "gear":
				if (gear) {
					hold.set(false);
					if (pushtimer <= 0) {
					  push.set(true);
					  if (pushtimer <= -20) {
						  lD1.set(-0.6);
					      lD2.set(-0.6);
						  rD1.set(-0.6);
						  rD2.set(-0.6);
						  if (pushtimer <= -40) {
							  gear = false;
						  } 
						  else {
							  pushtimer--;
						  }
					  } 
					  else {
						  pushtimer--;
					  }
					} 
					else {
					  pushtimer--;
					}
				} 
				else {
					pushtimer = 20;
					hold.set(true);
					push.set(false);
					lD1.set(0);
					lD2.set(0);
					rD1.set(0);
					rD2.set(0);
				}
				break;
			}
			break;
		}
	}

	@Override
	public void teleopPeriodic() {
		SmartDashboard.putString("TeleopMode", mode);
		SmartDashboard.putNumber("Distance", sonar.getVoltage());
		SmartDashboard.putString("Torque", s);
		SmartDashboard.putBoolean("Close Enough", gearReady);
		printPressure();
		
		
		gearReady = (sonar.getVoltage() <= 0.3);
		
			deploy = false;
		
		if (jS.getRawButton(2)) {
			climber.set(1);
		}
		else {
			climber.set(0);
		}
		
		if (jS.getRawButton(3)) {
			gear = true;
		}
		else{
			gear = false;
		}
		
		if(jL.getRawButton(2)){
			clicked = true;
		}
		
		if ((!jL.getRawButton(2)) && clicked) {
			if (dir) {
				dir = false;
			} else {
				dir = true;
			}
			clicked = false;
		}
		
		if(jR.getRawButton(2)){
			tclicked = true;
		}
		
		if ((!jR.getRawButton(2)) && tclicked) {
			if (torque) {
				torque = false;
			} else {
				torque = true;
			}
			tclicked = false;
		}
		
		hopper.set(!torque);
		
		if (torque) {
			s = "STRENGTH";
		} else {
			s = "SPEED";
		}
		
			if (!jL.getRawButton(1) && !jR.getRawButton(1) && dir == true) {
				lD1.set(-jL.getY() * 0.75);
				lD2.set(-jL.getY() * 0.75);
				rD1.set(-jR.getY() * 0.75);
				rD2.set(-jR.getY() * 0.75);
			}
			else if (!jL.getRawButton(1) && !jR.getRawButton(1) && dir == false) {
				rD1.set(jL.getY() * 0.75);
				rD2.set(jL.getY() * 0.75);
				lD1.set(jR.getY() * 0.75);
				lD2.set(jR.getY() * 0.75);
			}
			else if (!jL.getRawButton(1) && jR.getRawButton(1) && dir == true) {
				lD1.set(-jL.getY());
				lD2.set(-jL.getY());
				rD1.set(-jR.getY());
				rD2.set(-jR.getY());
			}
			else if (!jL.getRawButton(1) && jR.getRawButton(1) && dir == false) {
				rD1.set(jL.getY());
				rD2.set(jL.getY());
				lD1.set(jR.getY());
				lD2.set(jR.getY());
			}
			else if (jL.getRawButton(1) && !jR.getRawButton(1) && dir == true){
				lD1.set(-jL.getY() * 0.5);
				lD2.set(-jL.getY() * 0.5);
				rD1.set(-jR.getY() * 0.5);
				lD2.set(-jR.getY() * 0.5);
			}
			else if (jL.getRawButton(1) && !jR.getRawButton(1) && dir == false){
				rD1.set(jL.getY() * 0.5);
				rD2.set(jL.getY() * 0.5);
				lD1.set(jR.getY() * 0.5);
				lD2.set(jR.getY() * 0.5);
			}
			
			if (gear) {
				hold.set(false);
				if (pushtimer <= 0) {
				  push.set(true);
				  if (pushtimer <= -20) {
						  gear = false;
				  } else {
					  pushtimer--;
				  }
				} else {
				  pushtimer--;
				}
			} else {
				pushtimer = 20;
				hold.set(true);
				push.set(false);
			}
		}
	
	
	public void reset(){
		enc1.reset();enc2.reset();
	}
	
	public void drive(int dist, double speed, boolean dir){
		reset();
		double lenc = -enc1.getRaw();
		double renc = enc2.getRaw();
		if (dir){
			while (lenc < dist && renc < dist){
				lenc = -enc1.getRaw();
				renc = enc2.getRaw();
				rD1.set(speed);
				rD2.set(speed);
				lD1.set(speed);
				lD2.set(speed);
			}
		}
		else if (!dir){
			while (lenc > -dist && renc > -dist){
				lenc = -enc1.getRaw();
				renc = enc2.getRaw();
				rD1.set(-speed);
				rD2.set(-speed);
				lD1.set(-speed);
				lD2.set(-speed);
			}
		}
	}
	
	public void turn(int deg, double speed, boolean dir){
		reset();
		double lenc = -enc1.getRaw(); double renc = enc2.getRaw();
		if(dir){
			while (lenc < deg && renc > -deg){
				lenc = -enc1.getRaw(); renc = enc2.getRaw();
				lD1.set(speed);
				lD2.set(speed);
				rD1.set(-speed);
				rD2.set(-speed);
			}
		}
		else{
			while(lenc > -deg && renc < deg){
				lenc = -enc1.getRaw(); renc = enc2.getRaw();
				lD1.set(-speed);
				lD2.set(-speed);
				rD1.set(speed);
				rD2.set(speed);
			}
		}
	}

	public void printPressure() {
		int p = (int)(250*(pressure.getVoltage()/5) - 25);
		SmartDashboard.putNumber("Pressure", p);
	}
	
	public void testPeriodic() {
		int[] x = cam.getPixyX();
		int[] y = cam.getPixyY();
		boolean see = cam.canPixySee();
		
		System.out.println("Can see?" + see);
		for (int i = 0; i < x.length; i++) {
			System.out.println("X[" + i + "] = " + x[i]);
			System.out.println("Y[" + i + "] = " + y[i]);
		}
	}
} 