import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;
import java.nio.charset.Charset;

/*This is the SEND Thread of the UDP. It can read input from user anytime and send data anytime*/

public class UDPSend implements Runnable
{
	public UDPSend()
	{
	}

	public void run()
	{

		//Reading buffered stream from user 
		BufferedReader inFromUser =new BufferedReader(new InputStreamReader(System.in));

		while(UDPClient.loop==1)
		{
			try
			{
				byte[] sendData = new byte[1024];
				System.out.println("Enter the group letter (in CAPS) you want to send to : ");
				String sentence = inFromUser.readLine();
				System.out.println("Enter the message you want to send : ");
				sentence =sentence + inFromUser.readLine();
				sendData = sentence.getBytes();
				ByteBuffer buffer = ByteBuffer.wrap(sendData);
				if(UDPClient.loop==1)
					UDPClient.UDPChannel.send(buffer,UDPClient.IPAddress);
				buffer.clear();
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}//END WHILE
		System.out.println("Client SEND has closed.");
	}//END RUN
}//END
