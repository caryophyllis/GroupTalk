package com.shengsiyuan.chat.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.shengsiyuan.chat.util.CharacterUtil;


public class SendMessageThread extends Thread
{
    private String text;
    public SendMessageThread(String text)
    {
        this.text = text;       
    }
    
    public void run()
    {
        try
        {
            DatagramSocket datagramSocket = new DatagramSocket();
            int[] ports = CharacterUtil.string2Array(CharacterUtil.SERVER_PORT);
            int port = ports[1];   
            InetAddress address = InetAddress.getByName(CharacterUtil.SERVER_HOST);    
            DatagramPacket datagramPacket = new DatagramPacket(text.getBytes(),text.length(),address,port);
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
