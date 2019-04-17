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
    public static String SERVER_PORT;  //服务器端口号信息
    public static String SERVER_HOST;  //服务器地址信息
    public static int randomPort = generatePort();   //接收消息的端口号
    public static int randomPort2 = generatePort();  //接收用户列表的端口号
    
    /**
     *判断是否为空,为空返回true，否则返回false
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
    *判断字符串中是否包含@与/符号
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
     *判断服务器端口号是否在正确范围内
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
     *判断是否为数字，是数字返回true，否则返回false
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
     *判断服务器上的用户列表是否有重名，有返回true，没有返回false
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
     * 产生随机的端口号,该随机端口号大于等于1025
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
