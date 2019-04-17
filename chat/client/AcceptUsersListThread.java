package com.shengsiyuan.chat.client;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import com.shengsiyuan.chat.util.CharacterUtil;

public class AcceptUsersListThread extends Thread
{
    private JFrame frame;
    private ServerSocket serverSocket;
    
    public AcceptUsersListThread(JFrame frame)
    {
        this.frame = frame;
        try
        {
            serverSocket = new ServerSocket(CharacterUtil.randomPort2);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    public void run()
    {
        while(true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                byte[] buf = new byte[4096];
                int length = inputStream.read(buf);
                String usersList = new String(buf,0,length);
                
                ChatClient chatClient = (ChatClient)frame;
                chatClient.getJTextArea2().setText(usersList);
                
                inputStream.close();
                socket.close();
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
    }
}
