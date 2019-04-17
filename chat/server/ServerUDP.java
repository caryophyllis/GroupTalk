package com.shengsiyuan.chat.server;

import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import com.shengsiyuan.chat.util.CharacterUtil;

public class ServerUDP extends Thread
{
    private JFrame frame;
    
    public ServerUDP(JFrame frame)
    {
        this.frame = frame;
    }
    
    public void run()
    {
        while(true)
        {
            try
            {
                DatagramSocket datagramSocket = new DatagramSocket(CharacterUtil.PORT2);
                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf,1024);
                datagramSocket.receive(datagramPacket);
                String message = new String(buf,0,datagramPacket.getLength());//某个用户发来的信息
                
                Server server = (Server)frame;
                Map map = server.getMap();
                Set set = map.entrySet();
                Iterator iterator = set.iterator();
                
                while(iterator.hasNext()) //向每个客户端发送信息
                {
                    
                    Map.Entry me = (Map.Entry)iterator.next();
                    String username = (String)me.getKey();
                    String ports = (String)me.getValue();
                    int index = ports.indexOf("_");
                    int port = Integer.parseInt(ports.substring(0,index));
                    int lastIndex = ports.lastIndexOf("_");
                    
                    String address = ports.substring(lastIndex + 1);
                    
                    InetAddress clientAddress = InetAddress.getByName(address);
                    
                    Socket socket = new Socket(clientAddress,port);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((message).getBytes());
                    outputStream.close();
                    socket.close();
                }
  
                datagramSocket.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
}
