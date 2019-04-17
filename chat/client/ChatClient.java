/*
 * ChatClient.java
 *
 *
 */

package com.shengsiyuan.chat.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.shengsiyuan.chat.util.CharacterUtil;

/**
 * 
 * @author zl
 */
public class ChatClient extends javax.swing.JFrame
{
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextArea jTextArea2;
	private javax.swing.JTextField jTextField1;

	private Thread thread;

	/** Creates new form ChatClient */
	public ChatClient(Thread parentThread)
	{
		initComponents();
		this.thread = parentThread;

		this.addWindowListener(new MyEvent(parentThread));

		this.jTextArea1.setForeground(new java.awt.Color(0, 0, 255));
		this.jTextArea1.setEditable(false);
		this.jTextArea2.setEditable(false);
	}

	public Thread getThread()
	{
		return thread;
	}

	public JTextArea getJTextArea2()
	{
		return jTextArea2;
	}

	public JTextArea getJTextArea1()
	{
		return jTextArea1;
	}

	public void setJTextArea1(JTextArea textArea1)
	{
		jTextArea1 = textArea1;
	}

	private void initComponents()
	{
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jTextField1 = new javax.swing.JTextField(20);
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea2 = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		jPanel3 = new JPanel();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("聊天室");
		setResizable(false);
		jPanel1.setBorder(BorderFactory.createTitledBorder("聊天室信息"));
		jPanel2.setBorder(BorderFactory.createTitledBorder("在线用户列表"));
		jTextArea1.setColumns(30);
		jTextArea1.setRows(25);

		jTextArea2.setColumns(20);
		jTextArea2.setRows(25);

		jPanel3.add(jTextField1);
		jPanel3.add(jButton1);
		jPanel3.add(jButton2);

		jPanel1.setLayout(new BorderLayout());
		jPanel1.add(jScrollPane1, BorderLayout.NORTH);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);

		jPanel2.add(jScrollPane2);

		jScrollPane1.setViewportView(jTextArea1);
		jScrollPane2.setViewportView(jTextArea2);

		jTextField1.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent evt)
			{
				jTextField1KeyPressed(evt);
			}
		});

		jButton1.setText("发送");
		jButton2.setText("清屏");

		this.setLayout(new FlowLayout());
		this.getContentPane().add(jPanel1);
		this.getContentPane().add(jPanel2);

		jButton1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jButton2ActionPerformed(evt);
			}
		});

		this.pack();
		this.setVisible(true);
	}

	private void jTextField1KeyPressed(java.awt.event.KeyEvent evt)// GEN-FIRST:event_jTextField1KeyPressed
	{
		if (KeyEvent.VK_ENTER == evt.getKeyCode())
		{
			String text = this.jTextField1.getText();
			String message = CharacterUtil.CLIENT_NAME + ":" + text;
			if ("".equals(text.trim()))
			{
				JOptionPane.showMessageDialog(this, "内容不能为空！", "警告",
						JOptionPane.WARNING_MESSAGE);
				this.jTextField1.requestFocus();
				return;
			}
			this.jTextField1.setText("");
			thread = new SendMessageThread(message);
			thread.start();
		}
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)
	{
		String text = this.jTextField1.getText();
		String message = CharacterUtil.CLIENT_NAME + ":" + text;

		if ("".equals(text.trim()))
		{
			JOptionPane.showMessageDialog(this, "内容不能为空！", "警告",
					JOptionPane.WARNING_MESSAGE);
			this.jTextField1.requestFocus();
			return;
		}

		this.jTextField1.setText("");
		thread = new SendMessageThread(message);
		thread.start();
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)
	{
		this.jTextArea1.setText("");
	}
}

class MyEvent extends WindowAdapter
{
	private Thread parentThread;

	public MyEvent(Thread parentThread)
	{
		this.parentThread = parentThread;
	}

	public void windowClosing(WindowEvent event)
	{
		int[] ports = CharacterUtil.string2Array(CharacterUtil.SERVER_PORT);
		int port = ports[0];
		try
		{
			InetAddress address = InetAddress
					.getByName(CharacterUtil.SERVER_HOST);
			Socket socket = new Socket(address, port);
			OutputStream outputStream = socket.getOutputStream();
			
			outputStream.write(CharacterUtil.CLIENT_NAME.getBytes());
			
			outputStream.close();
			socket.close();
			((ClientConnectThread) parentThread).setFlag(false);
			
			System.exit(0);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}