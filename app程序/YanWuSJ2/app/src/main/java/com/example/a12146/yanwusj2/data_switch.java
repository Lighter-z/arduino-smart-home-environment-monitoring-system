package com.example.a12146.yanwusj2;

public class data_switch {

    //********************  这个是十进制的数字的  字节与数之间的转换


    //字节转换为十进制的数

    public static int byte2Int (byte b ){

        int r = (int) b ;
        return  r;

    }


    //十进制的数字转字节
    public static byte int2Byte (int i){

        byte r= (byte) i;

        return r;


    }


    //***************************************下面都是   十进制与十六进制   字符  之间的转换**********************************//


    //******************  十进制字符串转化为十六进制字符串

    /*
     * 字符串转为字节数组
     *
     */

    public static byte[] string2Bytes(String s ){

        byte[] r = s.getBytes();
        return r;

    }

    /*
     *   字节数组转换为16进制字符串
     *
     *   对每一个字节，先和0XFF做与运算，然后使用integer.toString（）函数，如果只有一位，需要在前面加0
     */

    public static String bytes2HexString(byte[] b){

        String r ="";

        for(int i = 0 ; i<b.length ;i++){
            String hex = Integer.toHexString(b[i] & 0xFF);
            if(hex.length()  == 1){

                hex = '0'+hex ;
            }
            r += hex.toUpperCase();

        }

        return r;
    }


    //*******************  十六进制字符串转化为手十进制字符串

    /*
     * 十六进制字符串转化为字符串
     *
     * 先转化为byte[]，在转化为字符串
     */


    /*
     * 十六进制字符串转为字节数组
     *
     * 这个比较复杂，每一个16进制字符是4bit，一个字节是8bit，所以两个十六进制字符转换成一个字节，
     * 对于第一个字符，转换成byte以后后移4位，然后和第二个字符的byte做或运算 ，这样就吧两个字符转换为一个字节
     *
     */

    //字符转换为字节
    private static byte charToByte (char c ){

        return (byte) "0123456789ABCDEF".indexOf(c);

    }


    //十六进制字符串转换为字节数组
    public static byte[] hexString2Bytes(String hex){

        if((hex == null) || (hex.equals("")) ){
            return  null ;

        }
        else if(hex.length()%2 != 0){
            return null ;
        }

        else{

            hex = hex.toUpperCase();
            int len = hex.length()/2 ;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for(int i = 0 ; i<len;i++){

                int p = 2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4  | charToByte(hc[p+1]));
            }

            return b ;

        }


    }


    //16进制字符串转字符串

    public static String hex2String (String hex) throws Exception {

        String r = bytes2String(hexString2Bytes(hex));     //这里的hexString2Bytes(hex)返回值是一个字节数组  我们要把
        //字节数组转化为字符串
        return r ;

    }


    /*
     * 字节数组转换为字符串 的方法
     *
     */


    public static String bytes2String(byte[] b) throws Exception {

        String r = new String(b,"UTF-8");
        return r;

    }






}
