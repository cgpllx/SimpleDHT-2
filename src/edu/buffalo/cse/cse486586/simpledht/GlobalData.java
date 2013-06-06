package edu.buffalo.cse.cse486586.simpledht;

public class GlobalData {
public static String MyID;
public static String Predecessor;
public static String Successor;
public static String Hash_AVD0;
public static String Hash_AVD1;
public static String Hash_AVD2;

public static Integer get_port(String port)
{
	if(port.equals("AVD0"))
		return 5554;
	else if(port.equals("AVD1"))
		return 5556;
	else return 5558;
}
public static Integer send_port(String to)
{
	if(to.equals("AVD0"))
		return 11108;
	else if(to.equals("AVD1"))
		return 11112;
	else return 11116;
}
}
