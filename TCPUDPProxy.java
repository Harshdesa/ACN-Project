import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;
import java.nio.charset.Charset;

/* Receives data from UDP Client or TCP Server and sends data to TCP Server or TCP Client using Selector which selects the UDP CHANNEL OR TCP CHANNEL	
*/

class TCPUDPProxy 
{
	
	//TO MAKE SURE IT'S CONNECTED OR NOT
	public static int connectedFlag=0;

	public static DatagramChannel tempChannel;
	
	public static SocketChannel tempChannel2;

	public static int criticalSection=0;

	public static int channelLimit=0;

	public static int channelChangeIteration=0;

	public static String[] portOfAllProxies=new String[3];
	public static String[] portNameOfAllProxies=new String[3];	

	public static int reservedTCPChannel[] = new int[3];	

	public static String initialMessageToServer;

	public static void FunctionToSaveTCPChannels(int a,SocketChannel c)
	{
		if(a==0)
		{
			TCPChannela=c;
			reservedTCPChannel[0]=1;
		}
		if(a==1)
		{
			TCPChannelb=c;
			reservedTCPChannel[1]=1;
		}
		if(a==2)
		{
			TCPChannelc=c;
			reservedTCPChannel[2]=1;
		}
	}

	public static SocketChannel retrieveTCPChannel(int index)
	{
		if(index==0)
			return TCPChannela;
		else if(index==1)
			return TCPChannelb;
		else
			return TCPChannelc;

	}

	public static void removeConnection(InetSocketAddress clientAddress)
	{
		if(clientAddress1.equals(clientAddress))
		{
			color[0]='\0';
			clientAddress1 =new InetSocketAddress("127.0.0.1",1111);
		}
		else if(clientAddress2.equals(clientAddress))
		{
			color[1]='\0';
			clientAddress2 =new InetSocketAddress("127.0.0.1",1111);
		}
		else if(clientAddress3.equals(clientAddress))
		{
			color[2]='\0';
			clientAddress3 =new InetSocketAddress("127.0.0.1",1111);
		}
		else if(clientAddress4.equals(clientAddress))
		{
			color[3]='\0';
			clientAddress4 =new InetSocketAddress("127.0.0.1",1111);
		}
		else if(clientAddress5.equals(clientAddress))
		{
			color[4]='\0';
			clientAddress5 =new InetSocketAddress("127.0.0.1",1111);
		}



	}

	public static void FunctionToSaveChannels(DatagramChannel c,char Color,InetSocketAddress clientAddress)
	{
		if(clientAddress1.equals(clientAddressComp))
		{
			UDPChannel1=c;
			color[0]=Color;
			clientAddress1 =clientAddress ;
		}
		else if(clientAddress2.equals(clientAddressComp))
		{
			UDPChannel2=c;
			color[1]=Color;
			clientAddress2 =clientAddress ;
		}
		else if(clientAddress3.equals(clientAddressComp))
		{
			UDPChannel3=c;
			color[2]=Color;
			clientAddress3 =clientAddress ;
		}
		else if(clientAddress4.equals(clientAddressComp))
		{
			UDPChannel4=c;
			color[3]=Color;
			clientAddress4 =clientAddress ;
		}
		else if(clientAddress5.equals(clientAddressComp))
		{
			UDPChannel5=c;
			color[4]=Color;
			clientAddress5 =clientAddress ;
		}

	}
	//Retrives the UDPChannel to which the color is associated with 
	public static DatagramChannel retrieveChannel(int index)
	{
		if(index==0)
			return UDPChannel1;
		else if(index==1)
			return UDPChannel2;
		else if(index==2)
			return UDPChannel3;

		else if(index==3)
			return UDPChannel4;
		else 
			return UDPChannel5;
		



	}
	//Retrives the UDPChannel to which the color is associated with 
	public static InetSocketAddress retrieveAddress(int index)
	{
		if(index==0)
			return clientAddress1;
		else if(index==1)
			return clientAddress2;
		else if(index==2)
			return clientAddress3;

		else if(index==3)
			return clientAddress4;
		else 
			return clientAddress5;
		



	}
	
	//This is just so that it can send messages coming from the TCPServer to the selected UDPClients 
	// Connection from the UDPClients can be done at any time. 
	
	public static SocketChannel getANewTCPChannel(int index)
	{
		
		if(index==0)
		{
			try{
			TCPChannelTemp = SocketChannel.open();
			TCPChannelTemp.configureBlocking(false);
			return TCPChannelTemp;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return TCPChannelTemp;
		
		}
		else 
		{
			try{
			TCPChannelTemp2 = SocketChannel.open();
			TCPChannelTemp2.configureBlocking(false);
			return TCPChannelTemp2;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return TCPChannelTemp2;
		}		
		
		
			


	}

		public static DatagramChannel UDPChannel1;	
		public static DatagramChannel UDPChannel2;	
		public static DatagramChannel UDPChannel3;	
		public static DatagramChannel UDPChannel4;	
		public static DatagramChannel UDPChannel5;	
	
		public static SocketChannel TCPChannela;
		public static SocketChannel TCPChannelb;
		public static SocketChannel TCPChannelc;		

		public static SocketChannel TCPChannelTemp;
		public static SocketChannel TCPChannelTemp2;
		public static InetSocketAddress clientAddress1 = new InetSocketAddress("127.0.0.1",1111);
		public static InetSocketAddress clientAddress2 = new InetSocketAddress("127.0.0.1",1111);
		public static InetSocketAddress clientAddress3 = new InetSocketAddress("127.0.0.1",1111);
		public static InetSocketAddress clientAddress4 = new InetSocketAddress("127.0.0.1",1111);
		public static InetSocketAddress clientAddress5 = new InetSocketAddress("127.0.0.1",1111);	
		public static InetSocketAddress clientAddressComp = new InetSocketAddress("127.0.0.1",1111); 

	//Color the proxy contains 
	public static char color[]=new char[5];
	public static char tempColor;
	public static int tempIterator;

	public static int askForSeparateChannel=1;		
	
	public static void main(String [] args) throws Exception 
	{
		
		//This is the flag so that the Proxy connects to the Server( For the 1st client connection) 
		int flagToConnect=0;	

		//Numberof connected clients
		int connected[]=new int[6];
		int connectIterator=0;
		
		
		int sameColor=0;

		Selector selector = Selector.open();
		String UDPServerName,TCPServerName,ProxyTCPServerName;
		int TCPServerPortNo,UDPServerPortNo,ProxyTCPPort;

		if(args.length !=6)
		{
			System.out.println("You goofed !");
			System.exit(0);
		}
		
		UDPServerName=args[0];
		UDPServerPortNo=Integer.parseInt(args[1]);
		TCPServerName=args[2];		
		TCPServerPortNo=Integer.parseInt(args[3]);
		ProxyTCPServerName=args[4];
		ProxyTCPPort=Integer.parseInt(args[5]);
		
		ServerSocketChannel TCPServerChannel = ServerSocketChannel.open();	 
		DatagramChannel UDPChannel=DatagramChannel.open();	 		
		
		UDPChannel.configureBlocking(false);
		TCPServerChannel.configureBlocking(false);
		
		
		InetSocketAddress UDPserverSocket = new InetSocketAddress(UDPServerPortNo);
		UDPChannel.socket().bind(UDPserverSocket);
		
		TCPServerChannel.socket().bind(new InetSocketAddress(ProxyTCPServerName, ProxyTCPPort));
		
				
		SocketChannel TCPChannelRep;
	
		SelectionKey serverKey = UDPChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
		SelectionKey socketServerSelectionKey = TCPServerChannel.register(selector,SelectionKey.OP_ACCEPT);
						

		String serverPropertiesTCP;
		serverPropertiesTCP="TCPChannelServer";
		socketServerSelectionKey.attach(serverPropertiesTCP);		
		String serverProperties;
		serverProperties="UDPChannel";
		serverKey.attach(serverProperties);

		SocketChannel TCPChannel= SocketChannel.open();
		TCPChannel.configureBlocking(false);
			
		InetSocketAddress clientAddress = new InetSocketAddress("127.0.0.1",1111);
		DatagramChannel UDPChannelRep = DatagramChannel.open();

		UDPChannel1 = DatagramChannel.open();	
		UDPChannel2 = DatagramChannel.open();	
		UDPChannel3 = DatagramChannel.open();	
		UDPChannel4 = DatagramChannel.open();	
		UDPChannel5 = DatagramChannel.open();	
	
		
		for(;;)
		{
			System.out.println("Waiting for the select...");

			if (selector.select() == 0)
                	continue;
			

			//For iterating among the number of keys
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iter = selectedKeys.iterator();
			
			while(iter.hasNext())
			{
				SelectionKey ky= iter.next();
//-------------------------------------------------------------------------------------------------------------------------------------------			
				if (((String)ky.attachment()).equals("TCPChannelServer"))
				{
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel) ky.channel();
					SocketChannel clientSocketChannel = TCPServerChannel.accept();

					if(clientSocketChannel !=null)
					{
						if(reservedTCPChannel[0]==0||reservedTCPChannel[1]==0||reservedTCPChannel[2]==0)
						{
							clientSocketChannel.configureBlocking(false);
				SelectionKey clientKeyServ = clientSocketChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
							System.out.println("A new server-proxy channel is registered");
							
							/*SAVE THE PROXY CHANNELS CONNECTED */
							boolean enterFunction=true;
							for(int i=0;i<3;i++)
							{
								if((0==reservedTCPChannel[i])&&(true==enterFunction))
								{
									FunctionToSaveTCPChannels(i,clientSocketChannel);
									enterFunction=false;
									clientKeyServ.attach(Integer.toString(i));
									tempChannel2=clientSocketChannel;
								}
							}

							System.out.println("Number of streams registered so far : "+channelLimit);
							System.out.println("IP address of host is : "+ clientSocketChannel.getRemoteAddress()); 						}//END IF

						else 
						{
							String message="";
							String noNewSpawn="no new spawn";
							ByteBuffer buffer = ByteBuffer.allocate(200);
							int bytesRead;
							if ((bytesRead = clientSocketChannel.read(buffer)) > 0) 
							{
								buffer.flip();
								message += Charset.defaultCharset().decode(buffer);
							}
							String port=null;
							String portName=null;
if(message.startsWith("Rreg")||message.startsWith("Greg")||message.startsWith("Breg")||message.startsWith("Yreg")||message.startsWith("Oreg"))
							{	
								Random rand = new Random();
								int randomNum = rand.nextInt(3);
								portName=portNameOfAllProxies[randomNum];
								port=portOfAllProxies[randomNum];
								//Sometimes will connect, sometimes it won't coz random		
							}
							noNewSpawn=noNewSpawn+portName+port;
							CharBuffer buffer2 = CharBuffer.wrap(noNewSpawn);
							try
							{
								while (buffer2.hasRemaining()) 
								{	
									clientSocketChannel.write(Charset.defaultCharset().encode(buffer2));
								}
							}							
							catch(Exception e)
							{
								e.printStackTrace();
							}
                      					buffer2.clear();
							System.out.println("A new channel tried to connect but I was full, so din't connect"); 							

						}//END ELSE 
					}//END IF
				}//END IF TCPCHANNELSERVER 

//---------------------------------------------------------------------------------------------------------------------------------------------				
				else if (((String)ky.attachment()).equals("UDPChannel"))
				{
					ByteBuffer buffer = ByteBuffer.allocate(255);
				
					if (ky.isReadable()) 
					{
						DatagramChannel clientChannel = (DatagramChannel) ky.channel();
						
						UDPChannelRep = clientChannel;
								
						buffer.clear();
						clientAddress = (InetSocketAddress)clientChannel.receive(buffer);
						System.out.println("Client address : " + clientAddress); 				
						String output = new String(buffer.array()).trim();
						tempChannel=clientChannel;	// Just for high load i.e. case 2
						
						if(output.equals("connect"))
						{
							criticalSection=0;
							if(flagToConnect==0)
							{
				getANewTCPChannel(channelChangeIteration).connect(new InetSocketAddress(TCPServerName, TCPServerPortNo)); 
								
								if(channelChangeIteration==0)
								TCPChannel=TCPChannelTemp;						
								else
								TCPChannel=TCPChannelTemp2;
								channelChangeIteration++;
						
								while (!TCPChannel.finishConnect()) 
								{
									System.out.println("Still connecting");			
								}
								askForSeparateChannel=0;
					SelectionKey clientKey = TCPChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
					

						String clientProperties;
						clientProperties="TCPChannel";
						clientKey.attach(clientProperties);
							}
							else
							askForSeparateChannel=1;
						// SO now when a new client comes, it can ask for stream without duplication of channelLimit
							flagToConnect=1;
						}

					//IF CLIENT TELLS WHAT COLOR IT IS 
				else if(output.equals("R")||output.equals("G")||output.equals("B")||output.equals("Y")||output.equals("O"))
						{
							connectIterator++;
							tempColor=output.charAt(0);
							tempChannel=clientChannel;
							tempIterator=connectIterator;
						//IF 5 Client limit reached, then stop/ Send reply back to UDPClient that it cannot connect
							if(connectIterator>5)
							{ 
								connectIterator--;
								byte[] sendNack = new byte[1024];
								String nack = "Do not connect";
								sendNack = nack.getBytes();
								ByteBuffer buffer2 = ByteBuffer.wrap(sendNack);
						System.out.println("A new client tried to connect but since proxy is full, it cannot connect ");
								clientChannel.send(buffer2, clientAddress);	
							}
							else
							{
								//IF Same color connecting then allow i.e. store the udpchannel
								for(int i=0;i<=4;i++)
								{
									if(color[i]==output.charAt(0))
									{
										
										//Will store the UDPChannel! 
					FunctionToSaveChannels(clientChannel,output.charAt(0),clientAddress);
				SelectionKey serverKey2 = clientChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
								serverKey2.attach(serverProperties);
								System.out.println("A new client is connected to the "+ output + " group");
										sameColor=1;
										byte[] sendAck=new byte[1024];
										String ack="Yay I am connected";
										sendAck = ack.getBytes();
										ByteBuffer buffer2 = ByteBuffer.wrap(sendAck);
										clientChannel.send(buffer2, clientAddress);

										//To avoid duplication of connected messages 
										i=i+10;
									}
								}
								
							//If same color not connecting then,  FORWARD IT TO THE SERVER
							//Store the channel temporarily, If negative reply received, discard the channel
							//If positive reply received, save the channel  
								if((sameColor==0)&&(askForSeparateChannel==1))
								{
									if (output.length() > 0) 
									{
										CharBuffer bufferC = CharBuffer.wrap(output);
										while (bufferC.hasRemaining()) 
										{
										TCPChannel.write(Charset.defaultCharset().encode(bufferC));
										}
									}
									System.out.println("Asking the server if it can spawn a new stream...");
										
								}//END IF sameColor
				
							//For an entirely new proxy
								if((sameColor==0)&&(askForSeparateChannel==0))
								{
//SAVE UDP CHANNELS HERE !!!
					FunctionToSaveChannels(clientChannel,output.charAt(0),clientAddress);
				SelectionKey serverKey2 = clientChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
									serverKey2.attach(serverProperties);
									//SEND PROXY TCP PORT TO THE SERVER
									output=output+"reg"+ProxyTCPServerName+Integer.toString(ProxyTCPPort);
									initialMessageToServer=output;
									if (output.length() > 0) 
									{
										// write some data into the channel
										CharBuffer bufferC = CharBuffer.wrap(output);
										while (bufferC.hasRemaining()) 
										{
										TCPChannel.write(Charset.defaultCharset().encode(bufferC));
										}
										bufferC.clear();
									}
							System.out.println("Registeration of color of new stream at server...");
									
								}//END IF sameColor
								sameColor=0;
							}//END ELSE 
							flagToConnect=1;
						}//END ELSE IF 
						

						//IF THE CLIENT CONNECTED DECIDES TO LEAVE
						else if(output.equals("leave"))
						{
							connectIterator--;
							//ky.cancel();

							byte[] sendNack = new byte[1024];
							String nack = "Do not connect";
							sendNack = nack.getBytes();
							ByteBuffer buffer2 = ByteBuffer.wrap(sendNack);
							clientChannel.send(buffer2, clientAddress);

							//Restart using clientAddress
							removeConnection(clientAddress);
							if(0==connectIterator)
							{
								String leaveMessage="leave";
								if (leaveMessage.length() > 0) 
								{
									// write some data into the channel
									CharBuffer bufferC = CharBuffer.wrap(leaveMessage);
									while (bufferC.hasRemaining()) 
									{
										TCPChannel.write(Charset.defaultCharset().encode(bufferC));
									}
								}
								String pruneMessage="prune";
								for(int i=0;i<3;i++)
								{
									if(reservedTCPChannel[i]==1)
									{
										System.out.println("Selecting " + i + " channel");
										TCPChannelRep=retrieveTCPChannel(i);
										CharBuffer bufferC = CharBuffer.wrap(pruneMessage);
										try
										{
											while (bufferC.hasRemaining()) 
											{		
										TCPChannelRep.write(Charset.defaultCharset().encode(bufferC));
											}
										}							
										catch(Exception e)
										{
											e.printStackTrace();
										}
                      								bufferC.clear();
									}
								}
							}
							
						}

						else
						{
							System.out.println("Message read from client: " + output);
	
							// Close the client channel if "Bye." is received 
							if (output.equals("Bye.")) 
							{
			        	        	        clientChannel.close();
								System.out.println("Client messages are complete; close.");
							}

															
							
							// Forward it to the server 
							if (output.length() > 0) 
							{
								// write some data into the channel
								CharBuffer bufferC = CharBuffer.wrap(output);
								while (bufferC.hasRemaining()) 
								{
									TCPChannel.write(Charset.defaultCharset().encode(bufferC));
								}
							}
						}



						
					}//END IF 
				}//END THE UDP PART 
				
//---------------------------------------------------------------------------------------------------------------------------------------------
				else if ((((String)ky.attachment()).equals("TCPChannel"))&&(flagToConnect==1))
				{
					ByteBuffer bufferA = ByteBuffer.allocate(200);
					int count = 0;
					String message = "";
					while ((count = TCPChannel.read(bufferA)) > 0) 
					{
						bufferA.flip();
						message += Charset.defaultCharset().decode(bufferA);
						System.out.println("Received from the parent : "+message);
					}
					

					//If reply received that the proxy can create a new spawn, then create a new channel 
					//If for child proxy then just forward the message
					if(message.equals("a new spawn"))
					{
						if(criticalSection==0)
						{
						System.out.println("Yay, a new group is added");
						
				SelectionKey serverKey2 = tempChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
						serverKey2.attach(serverProperties);
						System.out.println("A new client is connected to the "+ tempColor + " group");
/*STORE THE UDP CHANNEL HERE */
						FunctionToSaveChannels(tempChannel,tempColor,clientAddress);
						byte[] sendAck=new byte[1024];
						String ack="Yay I am connected";
						sendAck = ack.getBytes();
						ByteBuffer buffer = ByteBuffer.wrap(sendAck);
						tempChannel.send(buffer, clientAddress);
						}
						else
						{
						CharBuffer buffer2 = CharBuffer.wrap(message);
							try
							{
								while (buffer2.hasRemaining()) 
								{	
									tempChannel2.write(Charset.defaultCharset().encode(buffer2));
								}
							}							
							catch(Exception e)
							{
								e.printStackTrace();
							}
                      					buffer2.clear();
						}

					}
					else if(message.startsWith("no new spawn"))
					{
						if(criticalSection==0)
						{
							if(connectedFlag==1)
							{
							byte[] sendNack = new byte[1024];
							String nack = "Do not connect";
							sendNack = nack.getBytes();
							ByteBuffer buffer = ByteBuffer.wrap(sendNack);
			System.out.println("A new client with new color tried to connect but since server is full, it cannot connect ");
							tempChannel.send(buffer, clientAddress);
							}
							else if(("n".equals(String.valueOf(message.charAt(12))))&&("u".equals(String.valueOf(message.charAt(13)))))
							{
								byte[] sendNack = new byte[1024];
								String nack = "Do not connect";
								sendNack = nack.getBytes();
								ByteBuffer buffer = ByteBuffer.wrap(sendNack);
								tempChannel.send(buffer, clientAddress);
								TCPChannel.close();
								ky.cancel();	//unregister the channel
							}
							else  
							{
							String portName=String.valueOf(message.charAt(12))+String.valueOf(message.charAt(13))+String.valueOf(message.charAt(14))+String.valueOf(message.charAt(15))+String.valueOf(message.charAt(16));
							String portServ=String.valueOf(message.charAt(17))+String.valueOf(message.charAt(18))+String.valueOf(message.charAt(19))+String.valueOf(message.charAt(20)); 

							int portServer;
							portServer=Integer.parseInt(portServ);
							System.out.println("The new port to connect to "+portServer);
							//RESET EVERYTHING HERE !!!!! AND TELL UDPCLIENT TO RESEND CONNECT 								
							TCPChannel.close();
							
							TCPChannel= SocketChannel.open();
							TCPChannel.configureBlocking(false);

							TCPChannel.connect(new InetSocketAddress(portName, portServer));
							while (!TCPChannel.finishConnect()) 
								{
									System.out.println("Still connecting");			
								}
					SelectionKey clientKey = TCPChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);    
					

						String clientProperties;
						clientProperties="TCPChannel";
						clientKey.attach(clientProperties);
							if (initialMessageToServer.length() > 0) 
							{
								// write some data into the channel
								CharBuffer bufferC = CharBuffer.wrap(initialMessageToServer);
								while (bufferC.hasRemaining()) 
								{
									TCPChannel.write(Charset.defaultCharset().encode(bufferC));
								}
							}
							System.out.println("Registeration of color of new stream at server...");
							/*for(int i=0;i<4;i++)
								color[i]='\0';
					
							flagToConnect = 0;
							askForSeparateChannel = 1;
							connectIterator =0;
							byte[] sendReconnect = new byte[1024];
							String Reconnect = "Reconnect";
							sendReconnect = Reconnect.getBytes();
							ByteBuffer buffer = ByteBuffer.wrap(sendReconnect);
							tempChannel.send(buffer, clientAddress);
							ky.cancel();	//unregister the channel*/
							}
						}
						else
						{
							CharBuffer buffer2 = CharBuffer.wrap(message);
							try
							{
								while (buffer2.hasRemaining()) 
								{	
									tempChannel2.write(Charset.defaultCharset().encode(buffer2));
								}
							}							
							catch(Exception e)
							{
								e.printStackTrace();
							}
                      					buffer2.clear();
						}

					}
					else if(message.equals("connected"))
					{
						connectedFlag=1;



					}
					else if(message.equals("prune"))
					{
						ky.cancel();
						TCPChannel.close();
							
							TCPChannel= SocketChannel.open();
							TCPChannel.configureBlocking(false);

							TCPChannel.connect(new InetSocketAddress(TCPServerName, TCPServerPortNo));
							while (!TCPChannel.finishConnect()) 
								{
									System.out.println("Still connecting");			
								}
					SelectionKey clientKey = TCPChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);    
					

						String clientProperties;
						clientProperties="TCPChannel";
						clientKey.attach(clientProperties);
							if (initialMessageToServer.length() > 0) 
							{
								// write some data into the channel
								CharBuffer bufferC = CharBuffer.wrap(initialMessageToServer);
								while (bufferC.hasRemaining()) 
								{
									TCPChannel.write(Charset.defaultCharset().encode(bufferC));
								}
							}
							System.out.println("Registeration of color of new stream at server...");
						
					}
					//Still to change to selective channels
					else
					{

						
						// Prepare buffer for sending
						bufferA.flip(); 
						System.out.println("Reached else");
						for(int i=0;i<5;i++)
						{
							System.out.println("Reached for " + i +" times");
							if(message.charAt(0)==color[i])
							{
								System.out.println("Now,what's the problem...");
								UDPChannelRep=retrieveChannel(i);
								clientAddress=retrieveAddress(i);
								byte[] send = new byte[1024];
								System.out.println("It is being sent to "+ color[i] + "which is at " + i);
								send = message.getBytes();
								ByteBuffer buffer = ByteBuffer.wrap(send);
								UDPChannelRep.send(buffer,clientAddress);
							
							}
						}
						for(int i=0;i<channelLimit;i++)
						{
							if(reservedTCPChannel[i]==1)
							{
							System.out.println("Selecting " + i + " channel");
							TCPChannelRep=retrieveTCPChannel(i);
							CharBuffer buffer2 = CharBuffer.wrap(message);
							try
							{
								while (buffer2.hasRemaining()) 
								{	
									TCPChannelRep.write(Charset.defaultCharset().encode(buffer2));
								}
							}							
							catch(Exception e)
							{
								e.printStackTrace();
							}
                      					buffer2.clear();
							}

						}
						
						
					}

	
				}//END THE TCP PART 
//---------------------------------------------------------------------------------------------------------------------------------------------				
			else if(!((String)ky.attachment()).equals("TCPChannel"))			
				{
					// This is just to see if it enters this section if more than 2 proxies are connected
					System.out.println("checkpoint for more than 2 proxies");
						criticalSection=1;
						// data is available for read
						// buffer for reading
						ByteBuffer buffer = ByteBuffer.allocate(20);
						SocketChannel clientChannel = (SocketChannel) ky.channel();
						int bytesRead = 0;
						String message = "";
						if (ky.isReadable()) 
						{
							// the channel is non blocking so keep it open till the
							// count is >=0
							if ((bytesRead = clientChannel.read(buffer)) > 0) 
							{
								//flip the buffer to start reading
								buffer.flip();
								message += Charset.defaultCharset().decode(buffer);
							}

							//If client wants to leave 
							if(message.equals("leave"))
							{
								String left;

								left=(String)ky.attachment();
								int x=Integer.parseInt(left);
								portOfAllProxies[x]=null;
								portNameOfAllProxies[x]=null;
								ky.cancel();
								System.out.println("Channel"+ x + "has quit");
								System.out.println("The proxy has left");
	
							}
					/*SAVE THE PORT NUMBER OF THE CONNECTED CHANNELS*/
if(message.startsWith("Rreg")||message.startsWith("Greg")||message.startsWith("Breg")||message.startsWith("Yreg")||message.startsWith("Oreg"))
							{
								String PORT;
								PORT=(String)ky.attachment();
								int x=Integer.parseInt(PORT);
								//Collect port numbers of child proxies
portNameOfAllProxies[x]=String.valueOf(message.charAt(4))+String.valueOf(message.charAt(5))+String.valueOf(message.charAt(6))+String.valueOf(message.charAt(7))+String.valueOf(message.charAt(8));
portOfAllProxies[x]=String.valueOf(message.charAt(9))+String.valueOf(message.charAt(10))+String.valueOf(message.charAt(11))+String.valueOf(message.charAt(12));  								

								CharBuffer buffer2 = CharBuffer.wrap("connected");
								try
								{
									while (buffer2.hasRemaining()) 
									{	
								clientChannel.write(Charset.defaultCharset().encode(buffer2));
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
	                      					buffer2.clear();

	System.out.println("The new proxy tcp port is : "+portOfAllProxies[x]);							
System.out.println("New color proxy is registered at : "+ x + " at position : " + channelLimit);
								//Number of channels +1
								channelLimit++;

							}
							
							else
							{
								System.out.println(message);
								if(message.equals(""))
								{
								}
								else 
								{
								//Forward it to the TCP Proxy acting as server 
								if (message.length() > 0) 
									{
										// write some data into the channel
										CharBuffer bufferC = CharBuffer.wrap(message);
										while (bufferC.hasRemaining()) 
										{
										TCPChannel.write(Charset.defaultCharset().encode(bufferC));
										}
									}
								
								//Forward to proxy's children
								for(int i=0;i<5;i++)
								{
									System.out.println("Reached for " + i +" times");
									if(message.charAt(0)==color[i])
									{
										System.out.println("Now,what's the problem...");
										UDPChannelRep=retrieveChannel(i);
										clientAddress=retrieveAddress(i);
										byte[] send = new byte[1024];
								System.out.println("It is being sent to "+ color[i] + "which is at " + i);
										send = message.getBytes();
										ByteBuffer buffer2 = ByteBuffer.wrap(send);
										UDPChannelRep.send(buffer2,clientAddress);
							
									}
								}
	                      					}
								if (bytesRead < 0) 
								{
									// the key is automatically invalidated once the
									// channel is closed
									clientChannel.close();
								}
								
							}
						}
					
				}// END ELSE FINAL 
				else if(((String)ky.attachment()).equals("TCPChannel"))
				{
					System.out.println("Repeatedly selecting TCPChannel");
				}
				else
				{
					System.out.println("THE REASON FOR PARENT PROXY CRASHING : " +(String)ky.attachment());
				
				}
				iter.remove();
				selectedKeys.clear();
			}// End while loop 			
			
		}// End for loop 
	}// End Main()
}//END
