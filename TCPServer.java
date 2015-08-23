import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;
import java.nio.charset.Charset;

class TCPServer
{
	
	
	public static String[] portOfAllProxies=new String[5];
	public static String[] portNameOfAllProxies=new String[5];
	public static int channelLimit=0;
	public static int reservedChannel[] = new int[5];
	public static void FunctionToSaveChannels(int index,SocketChannel c)
	{
		if(index==0)
		{
			TCPChannel=c;
			reservedChannel[0]=1;
		}
		if(index==1)
		{
			TCPChannel2=c;
			reservedChannel[1]=1;
		}
		if(index==2)
		{
			TCPChannel3=c;
			reservedChannel[2]=1;
		}
		if(index==3)
		{
			TCPChannel4=c;
			reservedChannel[3]=1;
		}
		if(index==4)
		{
			TCPChannel5=c;
			reservedChannel[4]=1;
		}

	}//END FUNCTION

	public static SocketChannel retrieveChannel(int index)
	{
		if(index==0)
			return TCPChannel;
		else if(index==1)
			return TCPChannel2;
		else if(index==2)
			return TCPChannel3;
		else if(index==3)
			return TCPChannel4;
		else 
			return TCPChannel5;
	}

	public static SocketChannel TCPChannel;
	public static SocketChannel TCPChannel2;
	public static SocketChannel TCPChannel3;
	public static SocketChannel TCPChannel4;
	public static SocketChannel TCPChannel5;	
	
	public static char color[][]=new char[5][5];

	public static void main(String args[]) throws Exception
	{	
	
		SocketChannel tempChannel;
		int ServerPortNo,UDPServerPortNo;
		if(args.length !=2)
		{
			System.out.println("You goofed !");
			System.exit(0);
		}
		ServerPortNo=Integer.parseInt(args[0]);	
		UDPServerPortNo=Integer.parseInt(args[1]);
		ServerSocketChannel TCPServerChannel = ServerSocketChannel.open();
		DatagramChannel UDPChannel = DatagramChannel.open();
		
		UDPChannel.configureBlocking(false);
		TCPServerChannel.configureBlocking(false);
		
		InetSocketAddress UDPServerSocket = new InetSocketAddress(UDPServerPortNo);
		UDPChannel.socket().bind(UDPServerSocket);

		Selector selector = Selector.open();

		SelectionKey UDPServerKey = UDPChannel.register(selector,SelectionKey.OP_READ,SelectionKey.OP_WRITE);

		SelectionKey socketServerSelectionKey = TCPServerChannel.register(selector,SelectionKey.OP_ACCEPT);

		String UDPServerProperties="UDPServerChannel";
		UDPServerKey.attach(UDPServerProperties);

		String serverProperties;
		serverProperties="TCPChannel";
		socketServerSelectionKey.attach(serverProperties);

		TCPServerChannel.socket().bind(new InetSocketAddress(ServerPortNo));

		// mark the serversocketchannel as non blocking
		//TCPChannel.configureBlocking(false);

		for(;;) 
		{
			System.out.println("It is waiting here...");
			
			if (selector.select() == 0)
                	continue;

			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();

			while (iterator.hasNext()) 
			{
				SelectionKey ky = iterator.next();
				
//------------------------------------------------------------------------------------------------------------------------------------------
				if (((String)ky.attachment()).equals("TCPChannel"))
				{
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel) ky.channel();

					SocketChannel clientSocketChannel = TCPServerChannel.accept();
					
					if(clientSocketChannel !=null)
					{
if((reservedChannel[0]==0||reservedChannel[1]==0||reservedChannel[2]==0||reservedChannel[3]==0||reservedChannel[4]==0)&&(channelLimit<5)) 
						{
							clientSocketChannel.configureBlocking(false);
				SelectionKey clientKey = clientSocketChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
						
							System.out.println("A new server-proxy channel is registered");
					
// IMPORTANT SAVES ALL THE CHANNELS HERE !!
							boolean enterFunction=true;
							for(int i=0;i<5;i++)
							{
								if((0==reservedChannel[i])&&(true==enterFunction))
								{
									FunctionToSaveChannels(i,clientSocketChannel);
									enterFunction=false;
									clientKey.attach(Integer.toString(i));
									tempChannel=clientSocketChannel;
								}
							}
							
														
							System.out.println("Number of streams registered so far : "+channelLimit);
							System.out.println("IP address of host is : "+ clientSocketChannel.getRemoteAddress()); 														
						}//END IF
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
								for(int i=0;i<5;i++)
									{
										for(int j=0;j<5;j++)
										{
											if(message.charAt(0)==color[i][j])
											{
											port=portOfAllProxies[i];
											portName=portNameOfAllProxies[i];
											}
										}
									}
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
							System.out.println("A new channel tried to connect but I was full, so din't connect"); 							}//END ELSE 
					}//END IF
				}//END IF 
//----------------------------------------------------------------------------------------------------------------------------------------


				else if(((String)ky.attachment()).equals("UDPServerChannel"))
                                {

                                        ByteBuffer buffer = ByteBuffer.allocate(255);

                                        if (ky.isReadable())
                                        {
                                                DatagramChannel clientChannelUDP = (DatagramChannel) ky.channel();
                                                SocketChannel clientChannel;

                                                buffer.clear();
                                                clientChannelUDP.receive(buffer);
                                                String output = new String(buffer.array()).trim();
						System.out.println("Source says : "+ output);
                                                        if(output.equals(""))
                                                                {
                                                                }
                                                                else
								 {
                                                          try
                                                             	{
                                                                        for(int i=0;i<5;i++)
                                                                        {
                                                                                for(int j=0;j<5;j++)
                                                                                {
                                                                                        if(output.charAt(0)==color[i][j])
                                                                                        {

                                                                                                CharBuffer buffer3 = CharBuffer.wrap(output);
                                                                                                clientChannel=retrieveChannel(i);
                                                                                                while (buffer3.hasRemaining())
                                                                                                {
                                                                                clientChannel.write(Charset.defaultCharset().encode(buffer3));
                                                                                                }
                                                                                                buffer3.clear();
                                                                                        }
                                                                                }
                                                                        }
                                                                }
                                                                catch(Exception e)
                                                                {
                                                                        e.printStackTrace();
                                                                }
                                                        }
	                                        }//END IF KEY IS READABLE


                           }//END UDP PART








//-----------------------------------------------------------------------------------------------------------------------------------------
				else			
				{
						ByteBuffer buffer = ByteBuffer.allocate(20);
						SocketChannel clientChannel = (SocketChannel) ky.channel();
						int bytesRead = 0;
						String message = "";
						if (ky.isReadable()) 
						{
							if ((bytesRead = clientChannel.read(buffer)) > 0) 
							{
								buffer.flip();
								message += Charset.defaultCharset().decode(buffer);
							}

							
							if(message.equals("leave"))
							{
								String left;

								left=(String)ky.attachment();
								int x=Integer.parseInt(left);
								reservedChannel[x]=0;
								for(int i=0;i<5;i++)
								{
									if(color[x][i]!='\0') 										{
										System.out.println("Color "+ color[x][i]+" is leaving!");
										color[x][i]='\0';
										channelLimit--;
										
									}
								}	
								portOfAllProxies[x]=null;
								portNameOfAllProxies[x]=null;
								ky.cancel();
								System.out.println("Channel"+ x + "has quit");
								System.out.println("The proxy has left");
	
							}
				
if(message.startsWith("Rreg")||message.startsWith("Greg")||message.startsWith("Breg")||message.startsWith("Yreg")||message.startsWith("Oreg"))
							{
								String left;
								left=(String)ky.attachment();
								int x=Integer.parseInt(left);
								boolean stop=false;
								for(int i=0;i<5;i++)
								{
									if(('\0'==color[x][i])&&(false==stop))
									{
										color[x][i]=message.charAt(0);
										stop=true;
									}
								}
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

							System.out.println("The new proxy tcp port is : "+portOfAllProxies[channelLimit]);  	System.out.println("New color proxy is registered at : "+ x + " at position : " + channelLimit + "color stored : " + color[x][0]);
								
								channelLimit++;

							}		
				else if(message.equals("R")||message.equals("G")||message.equals("B")||message.equals("Y")||message.equals("O"))
							{
								if(channelLimit>4)
								{
										CharBuffer buffer2 = CharBuffer.wrap("no new spawn");
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
						System.out.println("A new stream of color " + message + "was requested, but I was full");
								}
								else
								{				
										String left;
										left=(String)ky.attachment();
										int x=Integer.parseInt(left);
										boolean stop=false;
										for(int i=0;i<5;i++)
										{
											if(('\0'==color[x][i])&&(false==stop))
											{
												color[x][i]=message.charAt(0);
												stop=true;
											}
										}					

										CharBuffer buffer2 = CharBuffer.wrap("a new spawn");
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
										channelLimit++;
								System.out.println("A new stream with color "+ message + " is created."); 
								}
							}
							
							else
							{
								System.out.println(message);
								if(message.equals(""))
								{
								}
								else 
								{
								try
								{
									for(int i=0;i<5;i++)
									{
										for(int j=0;j<5;j++)
										{
											if(message.charAt(0)==color[i][j])
											{
												
												CharBuffer buffer3 = CharBuffer.wrap(message);
												clientChannel=retrieveChannel(i);
												while (buffer3.hasRemaining()) 
												{	
										clientChannel.write(Charset.defaultCharset().encode(buffer3));
												}
												buffer3.clear();
											}
										}
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
	                      					}
								if (bytesRead < 0) 
								{
									clientChannel.close();
								}
								
							}
						}// END IF KEY IS READABLE ! 
					
				}// END PART 2 

//--------------------------------------------------------------------------------------------------------------------------

		 	

				iterator.remove();
			}//END WHILE
		}//END FOR


		
		
		
		
		
		
		
	}//END MAIN
}//END 
