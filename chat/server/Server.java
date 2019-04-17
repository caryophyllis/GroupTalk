package com.shengsiyuan.chat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.shengsiyuan.chat.util.CharacterUtil;

public class Server extends JFrame
{
	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JButton jButton;

	private JPanel jPanel1;

	private JPanel jPanel2;

	private JScrollPane jScrollPane1;

	private JTextArea jTextArea1;

	private JTextField jTextField1;

	private static final String CONNECT_THREAD = "CONNECT";

	private static Thread thread;

	private static Thread thread2;

	private static Thread thread3;

	private Map map = new HashMap(); // 用户名与端口号的映射

	public Server(String name)
	{
		super(name);

		initComponents();
		this.jTextArea1.setEditable(false);
		this.jTextArea1.setForeground(new java.awt.Color(245, 0, 0));
		this.addWindowListener(new ServerClosed(this));
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public void setUsersList()
	{
		this.jTextArea1.setText(""); // 先清空之前的用户列表
		for (Iterator i = map.keySet().iterator(); i.hasNext();)
		{
			String username = (String) i.next();
			this.jTextArea1.append(username + "\n");
		}
	}

	private void initComponents()
	{
		jPanel2 = new JPanel();
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jTextField1 = new JTextField(10);
		jButton = new JButton();
		jPanel1 = new JPanel();
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();

		// ///////////////////////////////////////////////////////

		jPanel1.setBorder(BorderFactory.createTitledBorder("服务器信息"));
		jPanel2.setBorder(BorderFactory.createTitledBorder("在线用户列表"));

		jLabel1.setText("服务器状态");
		jLabel2.setText("停止");
		jLabel2.setForeground(new Color(204, 0, 51));
		jLabel3.setText("端口号");

		jButton.setText("启动服务器");

		jButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				Server.this.execute(evt);
			}
		});

		this.jTextArea1.setEditable(false);
		this.jTextArea1.setForeground(new java.awt.Color(245, 0, 0));

		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jTextField1);
		jPanel1.add(jButton);

		jTextArea1.setColumns(30);
		jTextArea1.setForeground(new java.awt.Color(0, 51, 204));
		jTextArea1.setRows(20);

		jScrollPane1.setViewportView(jTextArea1);

		jPanel2.add(jScrollPane1);

		this.getContentPane().add(jPanel1, BorderLayout.NORTH);
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	private void execute(ActionEvent evt)
	{
		String command = evt.getActionCommand();

		if ("启动服务器".equals(command))
		{
			String hostPort = this.jTextField1.getText();

			if (CharacterUtil.isEmpty(hostPort))
			{
				JOptionPane.showMessageDialog(this, "端口号不能为空！", "警告",
						JOptionPane.WARNING_MESSAGE);
				this.jTextField1.requestFocus();

				return;
			}

			if (!CharacterUtil.isNumber(hostPort))
			{
				JOptionPane.showMessageDialog(this, "端口号必须为数字！", "警告",
						JOptionPane.WARNING_MESSAGE);
				this.jTextField1.requestFocus();
				return;
			}

			if (!CharacterUtil.isPortCorrect(hostPort))
			{
				JOptionPane.showMessageDialog(this, "端口号必须在 1024 与 65535 之间！",
						"警告", JOptionPane.WARNING_MESSAGE);
				this.jTextField1.requestFocus();
				return;
			}

			int port = Integer.parseInt(hostPort);

			thread = new ConnectThread(this, CONNECT_THREAD, port);
			thread.start();

			thread2 = new ExitThread(this);
			thread2.start();

			thread3 = new ServerUDP(this);
			thread3.start();

			this.jButton.setText("运行中");
			this.jLabel3.setText("运行中");
			this.jTextField1.setEnabled(false);
			this.jButton.setEnabled(false);
		}
	}

	public static void main(String[] args)
	{
		new Server("服务器");
	}

}

class ServerClosed extends WindowAdapter
{
	private Server server;

	public ServerClosed(Server server)
	{
		this.server = server;
	}

	public void windowClosing(WindowEvent event)
	{
		try
		{
			Map map = server.getMap();
			Set set = map.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) // 向每个客户端发送信息
			{
				Map.Entry me = (Map.Entry) iterator.next();
				String username = (String) me.getKey();
				String ports = (String) me.getValue();
				int index = ports.indexOf("_");
				int port = Integer.parseInt(ports.substring(0, index));
				int lastIndex = ports.lastIndexOf("_");

				String address = ports.substring(lastIndex + 1);

				InetAddress clientAddress = InetAddress.getByName(address);

				Socket socket = new Socket(clientAddress, port);
				OutputStream outputStream = socket.getOutputStream();
				String message = "SERVER_CLOSED";
				outputStream.write((message).getBytes());
				outputStream.close();
				socket.close();

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		System.exit(0);
	}
}
