package edu.buffalo.cse.cse486586.simpledht;

import java.io.Serializable;

public class Messageformat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String MSGType;
	String from;
	String to;
	String Data;
	Integer Port;
	String Key;
	String Value;
	String Successor;
	String Predecessor;
	String [][] gdump;
	boolean is_node_fail;
}
