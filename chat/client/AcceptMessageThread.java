package com.shengsiyuan.chat.client;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.shengsiyuan.chat.util.CharacterUtil;

public class AcceptMessageThread extends Thread
{
    private JFrame frame;
    private ServerSocket serverSocket;
    
    public AcceptMessageThread(JFrame frame)
    {
        this.frame = frame;
        try
        {
            serverSocket = new ServerSocket(CharacterUtil.randomPort);
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
                byte[] buf = new byte[1024];
                int length = inputStream.read(buf);
                String message = new String(buf,0,length);
                
                if("SERVER_CLOSED".equals(message))
                {
                    JOptionPane.showMessageDialog(frame,"服务器已关闭，程序将退出！","错误",JOptionPane.ERROR_MESSAGE);
                    System.exit(0);//程序退出
                }
                
                
                
                ChatClient chatClient = (ChatClient)frame;
                chatClient.getJTextArea1().append(message + "\n");
                
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
