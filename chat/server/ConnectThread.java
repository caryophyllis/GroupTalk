package com.shengsiyuan.chat.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import com.shengsiyuan.chat.util.CharacterUtil;

public class ConnectThread extends Thread
{
	private ServerSocket serverSocket;
	
	private Socket socket;
	
	private InputStream inputStream;
	
	private OutputStream outputStream;
	
	private JFrame frame; // 对主线程的引用

	public ConnectThread(JFrame frame, String threadName, int port)
	{
		this.frame = frame;

		this.setName(threadName);
		
		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void run()
	{
		while (true)
		{
			try
			{
				socket = serverSocket.accept();
				
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				
				byte[] buf = new byte[1024];
				int length = inputStream.read(buf);
				String info = new String(buf, 0, length); // 客户端发来的连接信息

				int index = info.lastIndexOf("@@@");
				String username = info.substring(0, index);
				int lastIndex = info.lastIndexOf("@");

				String clientPort = info.substring(lastIndex + 1);

				Server server = (Server) frame;
				
				Map map = server.getMap();

				if (CharacterUtil.isUsernameDuplicated(map, username))
				{
					String error = CharacterUtil.ERROR;
					outputStream.write(error.getBytes());
					inputStream.close();
					outputStream.close();
					socket.close();
				}
				else
				{
					String success = CharacterUtil.SUCCESS;
					
					String info2 = success + "@@@" + CharacterUtil.PORT + "_"
							+ CharacterUtil.PORT2;
					
					map.put(username, clientPort); // 将新用户的名字和端口号加入map中
					
					server.setUsersList();
					
					outputStream.write(info2.getBytes());
					inputStream.close();
					outputStream.close();
					socket.close();

					Set set = map.keySet();
					Iterator iterator = set.iterator();
					StringBuffer sb = new StringBuffer();
					
					while (iterator.hasNext())
					{
						String name = (String) iterator.next();
						sb.append(name + "\n");
					}
					
					String usersList = sb.toString(); // 得到用户列表

					Set set2 = map.entrySet();
					Iterator iterator2 = set2.iterator();

					while (iterator2.hasNext()) // 将新的用户列表发到每个客户端
					{

						Map.Entry me = (Map.Entry) iterator2.next();
						
						String temp = (String) me.getValue();
						int index_ = temp.indexOf("_");
						int lastIndex_ = temp.lastIndexOf("_");
						int port = Integer.parseInt(temp.substring(index_ + 1,
								lastIndex_));
						String address = temp.substring(lastIndex_ + 1);

						InetAddress clientAddress = InetAddress
								.getByName(address);

						Socket s = new Socket(clientAddress, port);
						OutputStream os = s.getOutputStream();
						os.write(usersList.getBytes());
						os.close();
						s.close();
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
