
package org.usfirst.frc.team9255.robot;

import edu.wpi.first.wpilibj.I2C;

public class Pixy {
	I2C port;
	
	public Pixy(I2C port) {
		this.port = port;
	}
	
	public boolean canPixySee() {
		boolean track = true;
		byte[] buff = new byte[1];
		int object = 0;
		int obnum = 20;
	    int[] pchecksum = new int[obnum];
	    int[] psignature = new int[obnum];
	    int[] pobjectx = new int[obnum];
	    int[] pobjecty = new int[obnum];
	    int[] pobjectw = new int[obnum];
	    int[] pobjecth = new int[obnum];
	    int dataState = 0;
	    boolean frame = false;
	    boolean see = false;
	    int zero = 0;
	    
		//Get Pixy I2C Data 
		for (int i = 0; i < pobjectx.length; i++) {
			pobjectx[i] = 0;
		}
				while (track) {
			    	port.readOnly(buff, 1);
				
			    	switch (dataState) {
			    	case 0:
			    		if (buff[0] == (byte)0x55) {
			    			dataState++;
			    			object++;
			    			zero = 0;
			    		} else if (buff[0] == 0) {
			    			zero++;
			    			if (zero >= 100) {
			    				track = false;
			    				see = false;
			    			}
			    		}
			    		break;
			    	case 1:
			    		if (buff[0] == (byte)0xAA) {
			    			dataState++;
			    		} else {
			    			dataState = 0;
			    		}
			    		break;
			    	case 2:
			    		if (buff[0] == (byte)0x55) {
			    			dataState = 1;
			    			object = 0;
			    			if (frame) {
			    				track = false;
			    				frame = false;
			    			} else {
			    				frame = true;
			    				see = true;
			    			}
			    		} else {
			    		    //System.out.println("Object: " + object);
						    pchecksum[object] = buff[0] & 0xFF;
						    dataState++;
			    		}
						break;
			    	case 3:
			    		pchecksum[object] = pchecksum[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Checksum: " + pchecksum[object]);
						dataState++;
						break;
			    	case 4:
			    		psignature[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 5:
			    		psignature[object] = psignature[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Signature #: " + psignature[object]);
						dataState++;
						break;
			    	case 6:
			    		pobjectx[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 7:
			    		pobjectx[object] = pobjectx[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" X value: " + pobjectx[object]);
						dataState++;
						break;
			    	case 8:
			    		pobjecty[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 9:
			    		pobjecty[object] = pobjecty[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Y value: " + pobjecty[object]);
						dataState++;
						break;
			    	case 10:
			    		pobjectw[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 11:
			    		pobjectw[object] = pobjectw[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Width: " + pobjectw[object]);
						dataState++;
						break;
			    	case 12:
			    		pobjecth[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 13:
			    		pobjecth[object] = pobjecth[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Height: " + pobjecth[object]);
						dataState = 0;
						break;
			    	}
				}
				
				return see;
	}
	
	public int[] getPixyX() {
		boolean track = true;
		byte[] buff = new byte[1];
		int object = 0;
		int obnum = 20;
	    int[] pchecksum = new int[obnum];
	    int[] psignature = new int[obnum];
	    int[] pobjectx = new int[obnum];
	    int[] pobjecty = new int[obnum];
	    int[] pobjectw = new int[obnum];
	    int[] pobjecth = new int[obnum];
	    int dataState = 0;
	    boolean frame = false;
	    boolean see = false;
	    int zero = 0;
	    
		//Get Pixy I2C Data 
		for (int i = 0; i < pobjectx.length; i++) {
			pobjectx[i] = 0;
		}
				while (track) {
			    	port.readOnly(buff, 1);
				
			    	switch (dataState) {
			    	case 0:
			    		if (buff[0] == (byte)0x55) {
			    			dataState++;
			    			object++;
			    			zero = 0;
			    		} else if (buff[0] == 0) {
			    			zero++;
			    			if (zero >= 100) {
			    				track = false;
			    				see = false;
			    			}
			    		}
			    		break;
			    	case 1:
			    		if (buff[0] == (byte)0xAA) {
			    			dataState++;
			    		} else {
			    			dataState = 0;
			    		}
			    		break;
			    	case 2:
			    		if (buff[0] == (byte)0x55) {
			    			dataState = 1;
			    			object = 0;
			    			if (frame) {
			    				track = false;
			    				frame = false;
			    			} else {
			    				frame = true;
			    				see = true;
			    			}
			    		} else {
			    		    System.out.println("Object: " + object);
						    pchecksum[object] = buff[0] & 0xFF;
						    dataState++;
			    		}
						break;
			    	case 3:
			    		pchecksum[object] = pchecksum[object] | ((buff[0] & 0xFF)*256);
						System.out.println(" Checksum: " + pchecksum[object]);
						dataState++;
						break;
			    	case 4:
			    		psignature[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 5:
			    		psignature[object] = psignature[object] | ((buff[0] & 0xFF)*256);
						System.out.println(" Signature #: " + psignature[object]);
						dataState++;
						break;
			    	case 6:
			    		pobjectx[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 7:
			    		pobjectx[object] = pobjectx[object] | ((buff[0] & 0xFF)*256);
						System.out.println(" X value: " + pobjectx[object]);
						dataState++;
						break;
			    	case 8:
			    		pobjecty[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 9:
			    		pobjecty[object] = pobjecty[object] | ((buff[0] & 0xFF)*256);
						System.out.println(" Y value: " + pobjecty[object]);
						dataState++;
						break;
			    	case 10:
			    		pobjectw[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 11:
			    		pobjectw[object] = pobjectw[object] | ((buff[0] & 0xFF)*256);
						System.out.println(" Width: " + pobjectw[object]);
						dataState++;
						break;
			    	case 12:
			    		pobjecth[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 13:
			    		pobjecth[object] = pobjecth[object] | ((buff[0] & 0xFF)*256);
						System.out.println(" Height: " + pobjecth[object]);
						dataState = 0;
						break;
			    	}
				}
				
				return pobjectx;
	}
	public int[] getPixyY() {
		boolean track = true;
		byte[] buff = new byte[1];
		int object = 0;
		int obnum = 20;
	    int[] pchecksum = new int[obnum];
	    int[] psignature = new int[obnum];
	    int[] pobjectx = new int[obnum];
	    int[] pobjecty = new int[obnum];
	    int[] pobjectw = new int[obnum];
	    int[] pobjecth = new int[obnum];
	    int dataState = 0;
	    boolean frame = false;
	    boolean see = false;
	    int zero = 0;
	    
		//Get Pixy I2C Data 
		for (int i = 0; i < pobjectx.length; i++) {
			pobjectx[i] = 0;
		}
				while (track) {
			    	port.readOnly(buff, 1);
				
			    	switch (dataState) {
			    	case 0:
			    		if (buff[0] == (byte)0x55) {
			    			dataState++;
			    			object++;
			    			zero = 0;
			    		} else if (buff[0] == 0) {
			    			zero++;
			    			if (zero >= 100) {
			    				track = false;
			    				see = false;
			    			}
			    		}
			    		break;
			    	case 1:
			    		if (buff[0] == (byte)0xAA) {
			    			dataState++;
			    		} else {
			    			dataState = 0;
			    		}
			    		break;
			    	case 2:
			    		if (buff[0] == (byte)0x55) {
			    			dataState = 1;
			    			object = 0;
			    			if (frame) {
			    				track = false;
			    				frame = false;
			    			} else {
			    				frame = true;
			    				see = true;
			    			}
			    		} else {
			    		    //System.out.println("Object: " + object);
						    pchecksum[object] = buff[0] & 0xFF;
						    dataState++;
			    		}
						break;
			    	case 3:
			    		pchecksum[object] = pchecksum[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Checksum: " + pchecksum[object]);
						dataState++;
						break;
			    	case 4:
			    		psignature[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 5:
			    		psignature[object] = psignature[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Signature #: " + psignature[object]);
						dataState++;
						break;
			    	case 6:
			    		pobjectx[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 7:
			    		pobjectx[object] = pobjectx[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" X value: " + pobjectx[object]);
						dataState++;
						break;
			    	case 8:
			    		pobjecty[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 9:
			    		pobjecty[object] = pobjecty[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Y value: " + pobjecty[object]);
						dataState++;
						break;
			    	case 10:
			    		pobjectw[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 11:
			    		pobjectw[object] = pobjectw[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Width: " + pobjectw[object]);
						dataState++;
						break;
			    	case 12:
			    		pobjecth[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 13:
			    		pobjecth[object] = pobjecth[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Height: " + pobjecth[object]);
						dataState = 0;
						break;
			    	}
				}
				
				return pobjecty;
	}
	
	public int[] getPixyWidth() {
		boolean track = true;
		byte[] buff = new byte[1];
		int object = 0;
		int obnum = 20;
	    int[] pchecksum = new int[obnum];
	    int[] psignature = new int[obnum];
	    int[] pobjectx = new int[obnum];
	    int[] pobjecty = new int[obnum];
	    int[] pobjectw = new int[obnum];
	    int[] pobjecth = new int[obnum];
	    int dataState = 0;
	    boolean frame = false;
	    boolean see = false;
	    int zero = 0;
	    
		//Get Pixy I2C Data 
		for (int i = 0; i < pobjectx.length; i++) {
			pobjectx[i] = 0;
		}
				while (track) {
			    	port.readOnly(buff, 1);
				
			    	switch (dataState) {
			    	case 0:
			    		if (buff[0] == (byte)0x55) {
			    			dataState++;
			    			object++;
			    			zero = 0;
			    		} else if (buff[0] == 0) {
			    			zero++;
			    			if (zero >= 100) {
			    				track = false;
			    				see = false;
			    			}
			    		}
			    		break;
			    	case 1:
			    		if (buff[0] == (byte)0xAA) {
			    			dataState++;
			    		} else {
			    			dataState = 0;
			    		}
			    		break;
			    	case 2:
			    		if (buff[0] == (byte)0x55) {
			    			dataState = 1;
			    			object = 0;
			    			if (frame) {
			    				track = false;
			    				frame = false;
			    			} else {
			    				frame = true;
			    				see = true;
			    			}
			    		} else {
			    		    //System.out.println("Object: " + object);
						    pchecksum[object] = buff[0] & 0xFF;
						    dataState++;
			    		}
						break;
			    	case 3:
			    		pchecksum[object] = pchecksum[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Checksum: " + pchecksum[object]);
						dataState++;
						break;
			    	case 4:
			    		psignature[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 5:
			    		psignature[object] = psignature[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Signature #: " + psignature[object]);
						dataState++;
						break;
			    	case 6:
			    		pobjectx[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 7:
			    		pobjectx[object] = pobjectx[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" X value: " + pobjectx[object]);
						dataState++;
						break;
			    	case 8:
			    		pobjecty[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 9:
			    		pobjecty[object] = pobjecty[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Y value: " + pobjecty[object]);
						dataState++;
						break;
			    	case 10:
			    		pobjectw[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 11:
			    		pobjectw[object] = pobjectw[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Width: " + pobjectw[object]);
						dataState++;
						break;
			    	case 12:
			    		pobjecth[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 13:
			    		pobjecth[object] = pobjecth[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Height: " + pobjecth[object]);
						dataState = 0;
						break;
			    	}
				}
				
				return pobjectw;
	}
	
	public int[] getPixyHeight() {
		boolean track = true;
		byte[] buff = new byte[1];
		int object = 0;
		int obnum = 20;
	    int[] pchecksum = new int[obnum];
	    int[] psignature = new int[obnum];
	    int[] pobjectx = new int[obnum];
	    int[] pobjecty = new int[obnum];
	    int[] pobjectw = new int[obnum];
	    int[] pobjecth = new int[obnum];
	    int dataState = 0;
	    boolean frame = false;
	    boolean see = false;
	    int zero = 0;
	    
		//Get Pixy I2C Data 
		for (int i = 0; i < pobjectx.length; i++) {
			pobjectx[i] = 0;
		}
				while (track) {
			    	port.readOnly(buff, 1);
				
			    	switch (dataState) {
			    	case 0:
			    		if (buff[0] == (byte)0x55) {
			    			dataState++;
			    			object++;
			    			zero = 0;
			    		} else if (buff[0] == 0) {
			    			zero++;
			    			if (zero >= 100) {
			    				track = false;
			    				see = false;
			    			}
			    		}
			    		break;
			    	case 1:
			    		if (buff[0] == (byte)0xAA) {
			    			dataState++;
			    		} else {
			    			dataState = 0;
			    		}
			    		break;
			    	case 2:
			    		if (buff[0] == (byte)0x55) {
			    			dataState = 1;
			    			object = 0;
			    			if (frame) {
			    				track = false;
			    				frame = false;
			    			} else {
			    				frame = true;
			    				see = true;
			    			}
			    		} else {
			    		    //System.out.println("Object: " + object);
						    pchecksum[object] = buff[0] & 0xFF;
						    dataState++;
			    		}
						break;
			    	case 3:
			    		pchecksum[object] = pchecksum[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Checksum: " + pchecksum[object]);
						dataState++;
						break;
			    	case 4:
			    		psignature[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 5:
			    		psignature[object] = psignature[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Signature #: " + psignature[object]);
						dataState++;
						break;
			    	case 6:
			    		pobjectx[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 7:
			    		pobjectx[object] = pobjectx[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" X value: " + pobjectx[object]);
						dataState++;
						break;
			    	case 8:
			    		pobjecty[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 9:
			    		pobjecty[object] = pobjecty[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Y value: " + pobjecty[object]);
						dataState++;
						break;
			    	case 10:
			    		pobjectw[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 11:
			    		pobjectw[object] = pobjectw[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Width: " + pobjectw[object]);
						dataState++;
						break;
			    	case 12:
			    		pobjecth[object] = buff[0] & 0xFF;
						dataState++;
						break;
			    	case 13:
			    		pobjecth[object] = pobjecth[object] | ((buff[0] & 0xFF)*256);
						//System.out.println(" Height: " + pobjecth[object]);
						dataState = 0;
						break;
			    	}
				}
				
				return pobjecth;
	}
}
