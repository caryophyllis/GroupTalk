package com.shengsiyuan.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.shengsiyuan.chat.util.CharacterUtil;

public class Client extends JFrame
{
	private JTextField hostAddress;

	private JButton jButton1;

	private JButton jButton2;

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JPanel jPanel1;

	private JTextField port;

	private JTextField username;

	private Thread thread;

	public Client()
	{
		initComponents();
	}

	private void initComponents()
	{
		jPanel1 = new JPanel();
		jLabel1 = new JLabel();
		username = new JTextField(15);
		jLabel2 = new JLabel();
		hostAddress = new JTextField(15);
		jLabel3 = new JLabel();
		port = new JTextField(15);
		jButton1 = new JButton();
		jButton2 = new JButton();

		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("�û���¼");
		this.setAlwaysOnTop(true);
		this.setResizable(false);

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("�û���¼"));

		jLabel1.setText("�û���");
		jLabel2.setText("������");
		jLabel3.setText("�˿ں�");

		jButton1.setText("��¼");
		jButton2.setText("����");

		jButton1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("\u91cd\u7f6e");
		jButton2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				jButton2ActionPerformed(evt);
			}
		});

		jPanel1.add(jLabel1);
		jPanel1.add(username);
		jPanel1.add(jLabel2);
		jPanel1.add(hostAddress);
		jPanel1.add(jLabel3);
		jPanel1.add(port);

		jPanel1.add(jButton1);
		jPanel1.add(jButton2);

		this.getContentPane().add(jPanel1);
		this.setSize(250, 300);
		this.setVisible(true);

	}

	private void jButton1ActionPerformed(ActionEvent evt)
	{
		String username = this.username.getText();
		String hostAddress = this.hostAddress.getText();
		String hostPort = this.port.getText();

		if (CharacterUtil.isEmpty(username))
		{
			JOptionPane.showMessageDialog(this, "�û�������Ϊ�գ�", "����",
					JOptionPane.WARNING_MESSAGE);
			this.username.requestFocus();
			return;
		}

		if (!CharacterUtil.isCorrect(username))
		{
			JOptionPane.showMessageDialog(this, "�û������ܰ��� @ �� / �ַ���", "����",
					JOptionPane.WARNING_MESSAGE);
			this.username.requestFocus();
			return;
		}

		if (CharacterUtil.isEmpty(hostAddress))
		{
			JOptionPane.showMessageDialog(this, "��������ַ����Ϊ�գ�", "����",
					JOptionPane.WARNING_MESSAGE);
			this.hostAddress.requestFocus();
			return;
		}
		if (CharacterUtil.isEmpty(hostPort))
		{
			JOptionPane.showMessageDialog(this, "�˿ںŲ���Ϊ�գ�", "����",
					JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return;
		}
		if (!CharacterUtil.isNumber(hostPort))
		{
			JOptionPane.showMessageDialog(this, "�˿ںű���Ϊ���֣�", "����",
					JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return;
		}

		if (!CharacterUtil.isPortCorrect(hostPort))
		{
			JOptionPane.showMessageDialog(this, "�˿ںű����� 1024 �� 65535 ֮�䣡",
					"����", JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return;
		}

		int port = Integer.parseInt(hostPort);

		CharacterUtil.SERVER_HOST = hostAddress; // ��������ַ
		CharacterUtil.CLIENT_NAME = username;

		thread = new ClientConnectThread(this, hostAddress, port, username);
		thread.start();
	}

	private void jButton2ActionPerformed(ActionEvent evt)
	{
		this.username.setText("");
		this.hostAddress.setText("");
		this.port.setText("");
	}

	public static void main(String[] args)
	{
		new Client();
	}

}
