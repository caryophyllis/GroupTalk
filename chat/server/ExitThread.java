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

public class ExitThread extends Thread
{
    private JFrame frame;
    private ServerSocket serverSocket;
    
    public ExitThread(JFrame frame)
    {
        this.frame = frame;
        
        try
        {
            serverSocket = new ServerSocket(CharacterUtil.PORT);
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
                String username = new String(buf,0,length);
                Server server = (Server)frame;
                Map map = server.getMap();
                map.remove(username);
                server.setUsersList();
                
                /**
                 * ���´���Ϊ֪ͨ���пͻ��˸����û��б���ʵӦ�ý����ع��ˣ���Ϊ�����õ�ͬ���Ĵ���
                 */
                
                Set set = map.keySet();
                Iterator iterator = set.iterator();
                StringBuffer sb = new StringBuffer();
                while(iterator.hasNext())
                {
                    String name = (String)iterator.next();
                    sb.append(name + "\n");
                }
                String usersList = sb.toString();  //�õ��û��б�
                
                Set set2 = map.entrySet();
                Iterator iterator2 = set2.iterator();
                
                while(iterator2.hasNext())  //���µ��û��б���ÿ���ͻ���
                {
                    
                    Map.Entry me = (Map.Entry)iterator2.next();
                    String temp = (String)me.getValue();
                    int index_ = temp.indexOf("_");
                    int lastIndex_ = temp.lastIndexOf("_");
                    int port = Integer.parseInt(temp.substring(index_ + 1,lastIndex_));
                    String address = temp.substring(lastIndex_ + 1);
                    
                    InetAddress clientAddress = InetAddress.getByName(address);
                    
                    Socket s = new Socket(clientAddress,port);
                    OutputStream os = s.getOutputStream();
                    os.write(usersList.getBytes());
                    os.close();
                    s.close();
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
}
