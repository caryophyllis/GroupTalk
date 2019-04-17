package com.shengsiyuan.chat.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.shengsiyuan.chat.util.CharacterUtil;

public class ClientConnectThread extends Thread
{
	private JFrame frame;
	private String hostAddress;
	private int hostPort;
	private String username;
	private boolean flag = true;
	private Thread acceptMessageThread;
	private Thread acceptUsersListThread;

	public ClientConnectThread(JFrame frame, String hostAddress, int hostPort,
			String username)
	{
		this.frame = frame;
		this.hostAddress = hostAddress;
		this.hostPort = hostPort;
		this.username = username;
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	public void run()
	{
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		String error = CharacterUtil.ERROR;

		try
		{
			InetAddress host = InetAddress.getByName(hostAddress);
			socket = new Socket(host, hostPort);

			is = socket.getInputStream();
			os = socket.getOutputStream();

			int randomPort = CharacterUtil.randomPort;
			int randomPort2 = CharacterUtil.randomPort2;

			InetAddress address = InetAddress.getLocalHost();// 得到客户端地址信息
			String clientAddress = address.toString();
			int l = clientAddress.indexOf("/");
			clientAddress = clientAddress.substring(l + 1);// 客户端ip地址
			String info = username + "@@@" + randomPort + "_" + randomPort2
					+ "_" + clientAddress;

			os.write(info.getBytes());

			byte[] buf = new byte[100];

			int length = is.read(buf);

			String temp = new String(buf, 0, length);

			if (temp.equals(error))
			{
				JOptionPane.showMessageDialog(frame, "用户名与现有用户重复，请更换用户名！",
						"错误", JOptionPane.ERROR_MESSAGE);

				socket.close();

				is.close();
				os.close();

				return;
			}

			int index = temp.lastIndexOf("@@@");

			String temp2 = temp.substring(0, index);
			int lastIndex = temp.lastIndexOf("@");
			String portInfo = temp.substring(lastIndex + 1); // 服务器端端口号信息

			CharacterUtil.SERVER_PORT = portInfo;

			socket.close();

			is.close();
			os.close();

			// 进入聊天主窗口
			ChatClient chatClient = new ChatClient(this);
			chatClient.setVisible(true);
			frame.setVisible(false);

			acceptMessageThread = new AcceptMessageThread(chatClient);// 接收消息的线程
			acceptMessageThread.start();

			acceptUsersListThread = new AcceptUsersListThread(chatClient);
			acceptUsersListThread.start();

			while (flag) // 不断循环直到主屏幕退出
			{
				try
				{
					Thread.sleep(6000);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "未能建立与服务器的连接！", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

}
