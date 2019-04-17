package com.shengsiyuan.chat.util;

import java.util.Iterator;
import java.util.Map;

public class CharacterUtil
{
    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    public static final int PORT = generatePort();
    public static final int PORT2 = generatePort();
    public static String CLIENT_NAME;
    public static String SERVER_PORT;  //�������˿ں���Ϣ
    public static String SERVER_HOST;  //��������ַ��Ϣ
    public static int randomPort = generatePort();   //������Ϣ�Ķ˿ں�
    public static int randomPort2 = generatePort();  //�����û��б�Ķ˿ں�
    
    /**
     *�ж��Ƿ�Ϊ��,Ϊ�շ���true�����򷵻�false
     */
    public static boolean isEmpty(String str)
    {
        if("".equals(str.trim()))
        {
            return true;
        }
        return false;
    }
    
   /**
    *�ж��ַ������Ƿ����@��/����
    */
    
    public static boolean isCorrect(String str)
    {
        for(int i = 0 ; i < str.length() ; i++)
        {
            char ch = str.charAt(i);
            if('@' == ch || '/' == ch)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     *�жϷ������˿ں��Ƿ�����ȷ��Χ��
     *
     */
    
    public static boolean isPortCorrect(String port)
    {
        int temp = Integer.parseInt(port);
        
        if(temp <= 1024 || temp > 65535)
        {
            return false;
        }
        
        return true;
    }
    
    
    /**
     *�ж��Ƿ�Ϊ���֣������ַ���true�����򷵻�false
     */
    public static boolean isNumber(String str)
    {
        for(int i = 0 ; i < str.length() ; i++)
        {
            if(!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     *�жϷ������ϵ��û��б��Ƿ����������з���true��û�з���false
     *
     */
    
    public static boolean isUsernameDuplicated(Map map,String username)
    {
        for(Iterator i = map.keySet().iterator();i.hasNext();)
        {
            String temp = (String)i.next();
            
            if(username.equals(temp))
            {
                return true;
            }
        }
        return false;
    }
    
    
    
    /**
     * ��������Ķ˿ں�,������˿ںŴ��ڵ���1025
     */
    
    
    public static int generatePort()
    {
        int port = (int)(Math.random() * 50000 + 1025);
        return port;
    }
    
    public static int[] string2Array(String str)
    {
        int[] result = new int[2];
        int index =  str.indexOf("_");
        result[0] = Integer.parseInt(str.substring(0,index));
        result[1] = Integer.parseInt(str.substring(index + 1));
        
        return result;
        
    }
}
