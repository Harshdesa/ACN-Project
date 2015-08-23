import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;
import java.nio.charset.Charset;

/*This class can receive from the proxy anytime. The blank acceptance is so that it doesn't go in an infinite loop*/

public class UDPReceive implements Runnable
{
	public UDPReceive()
	{
	}
	public void run()
	{
		
		while(UDPClient.loop==1)
		{
			try
			{
				ByteBuffer buffer = ByteBuffer.allocate(1000);
				UDPClient.UDPChannel.receive(buffer);
				buffer.flip();
				String output = new String(buffer.array()).trim();
				if(output.equals(""))
				{
				}				
				else if(output.equals("Do not connect"))
				{
					UDPClient.loop=0;
					System.out.println("Sorry, you cannot connect.");
				}
				else if(output.equals("Reconnect"))
				{
					/*SEND THE CONNECT MESSAGE */	
					byte[] sendConnect = new byte[1024];
					String sentence = "connect";
					sendConnect = sentence.getBytes();
					ByteBuffer buffer2 = ByteBuffer.wrap(sendConnect);
					UDPClient.UDPChannel.send(buffer2,UDPClient.IPAddress);
					buffer2.clear();
		
					/* SEND THE COLOR VALUE */	 
					byte[] sendColor = new byte[1024];
					sendColor = UDPClient.color.getBytes(); 
					ByteBuffer buffer3 = ByteBuffer.wrap(sendColor);
					UDPClient.UDPChannel.send(buffer3,UDPClient.IPAddress);
					buffer3.clear();

				}
				else
				System.out.println("Message read from Proxy: " + output);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}//END WHILE
		System.out.println("Client RECEIVE has closed.");
	}//end RUN
}//END
