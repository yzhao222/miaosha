package org.example.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CodeUtil {
    private static int width=90;//图片宽度
    private static int height=20;//图片高度
    private static int codeCount=4;//图片上显示验证码个数
    private static int xx=15;//验证码起始地点
    private static int fontHeight=18;
    private static int codeY=16;//验证码起始地点
    private static char[] codeSequence={'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static Map<String,Object>generateCodeAndPic(){
        //定义图像buffer
        BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        Graphics gd=bufferedImage.getGraphics();
        //随机数生成器
        Random random=new Random();
        //将图像填充为白色画一个长方形
        gd.setColor(Color.WHITE);
        gd.fillRect(0,0,width,height);
        //创建字体
        Font font=new Font("Fixedsys",Font.BOLD,fontHeight);
        gd.setFont(font);
        //画边框
        gd.setColor(Color.BLACK);
        gd.drawRect(0,0,width-1,height-1);
        //生成干扰线
        gd.setColor(Color.BLACK);
        for(int i=0;i<30;i++){
            int x=random.nextInt(width);
            int y= random.nextInt(height);
            int xl= random.nextInt(12);
            int yl= random.nextInt(12);
            gd.drawLine(x,y,x+xl,y+yl);
        }
        //保存code
        StringBuffer randomCode=new StringBuffer();
        int red=0, green=0,blue=0;
        //产生验证码
        for(int i=0;i<codeCount;i++){
            String code=String.valueOf(codeSequence[random.nextInt(36)]);
            red=random.nextInt(255);
            green=random.nextInt(255);
            blue=random.nextInt(255);
            gd.setColor(new Color(red,green,blue));
            gd.drawString(code,(i+1)*xx,codeY);
            randomCode.append(code);
        }
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("code",randomCode);
        map.put("codePic",bufferedImage);
        return map;
    }
}
