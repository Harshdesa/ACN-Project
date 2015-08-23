import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;



// Sends "connect" message to TCPUDPProxy to go ahead and connect to the Server 
//Also sends color of itself
//Receives if it can connect or it cannot connect 
/*
If it can connect, them 
Receives input from user and receives data from same/other multicast group 
*/


class UDPClient
{ 
	public static InetSocketAddress IPAddress;
	public static DatagramChannel UDPChannel;
	public static int loop=1;
	public static String color;
		
	public static void main(String args[]) throws Exception
	{
		
		
		//Declaring the server port number
		int ServerPortNo;

		String ServerName;

		// Take the argument of Server Port number from the user 
		if(args.length !=3)
		{
			System.out.println("You goofed !");
			System.exit(0);
		}
		
		//Assiging the server port number
		ServerName=args[0]; 
		ServerPortNo=Integer.parseInt(args[1]);

		//Assigning the color 
		color=args[2];

		// IP Address of the server 
		IPAddress = new InetSocketAddress(ServerName,ServerPortNo);
	
		//Create client UDP channel 
		UDPChannel= DatagramChannel.open();		
		

		/*SEND THE CONNECT MESSAGE */	
		byte[] sendConnect = new byte[1024];
		String sentence = "connect";
		sendConnect = sentence.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(sendConnect);
		UDPChannel.send(buffer,IPAddress);
		buffer.clear();
		
		/* SEND THE COLOR VALUE */	 
		byte[] sendColor = new byte[1024];
		sendColor = color.getBytes(); 
		ByteBuffer buffer2 = ByteBuffer.wrap(sendColor);
		UDPChannel.send(buffer2,IPAddress);
		buffer2.clear();
		
		//Creating threads for sending and receive so that they don't block each other		
		new Thread(new UDPSend()).start();
		new Thread(new UDPReceive()).start();
	}//end MAIN
}//END
