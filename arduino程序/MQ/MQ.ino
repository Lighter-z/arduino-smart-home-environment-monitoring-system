/* 
 *  QQ:3162786026
蓝色卡号 97 73 182 195
白色卡号 176 251 218 27
卡号根据自己所用的IC卡实际卡号进行修改
*/

#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Servo.h>

#define SS_PIN 10
#define RST_PIN 9

#define MQ A0       //烟雾模块模拟引脚
#define MQ_DO 2   //烟雾模块  数字引脚

#define buzzer 3    //蜂鸣器引脚
#define fengshan 4  //风扇引脚
#define buttonPin 6 //人体检测引脚


#define Pin_servo 5   //舵机控制引脚

#define blue 97   //蓝色卡号 第一位  正确的卡
#define white 176  //白色卡号 第一位  错误的卡

MFRC522 rfid(SS_PIN, RST_PIN); //实例化类

// 初始化数组用于存储读取到的NUID 
byte nuidPICC[4];


//报警标志位
char yanwu_flag = 0;
char ka_flag = 0;
char renti_flag = 0;
char buzzer_flag = 0;
char feng_flag = 0;

/*
控制模式  
0x01为自动控制 
0x02为app控制
*/
char app = 0x01;  

unsigned char sendData[2] = {};      //数据发送缓存区
unsigned  char DATA[5] = {};         //数据接收缓存区   
unsigned char dataLen = 0;           //数据长度基数
unsigned char dataLenPro = 5;       //定义数据串最大长度
unsigned char sendDataEnd = 0x1F;       //数据结尾为0x1F   arduino-app
unsigned char dataEnd = 0x0A;       //数据结尾为0x0A app-arduino
unsigned char servoNum = 0;         //舵机的角度值



//串口数据传值
unsigned char servoVal = 0;
//MQ-2
unsigned int MQ_Value = 0;
//程序运行计数器
unsigned char runNum = 0; 

Servo myservo;          //实例化舵机

void setup()
{
  SPI.begin(); // 初始化SPI总线
  rfid.PCD_Init(); // 初始化 MFRC522 

  pinMode(buttonPin, INPUT);    
  
  pinMode(MQ_DO,INPUT);
  pinMode(MQ,INPUT);
  
  pinMode(buzzer,OUTPUT);
  digitalWrite(buzzer,HIGH); 
  
  pinMode(fengshan,OUTPUT);
  digitalWrite(fengshan,LOW);

  myservo.attach(Pin_servo);

  Serial.begin(115200); //设置通讯的波特率为9600
  sendData[1] = sendDataEnd;            //初始化保存发送数据校验位
}

void buzzerInit(unsigned char n)
{
   if (n == 1) digitalWrite(buzzer,LOW);     //蜂鸣器开
  else digitalWrite(buzzer,HIGH);      //蜂鸣器关
}

void fengshanInit(unsigned char n)
{
   if (n == 1) digitalWrite(fengshan,HIGH);//风扇开
  else digitalWrite(fengshan,LOW);//风扇关
}

void cameraControl()      //舵机控制：0x01左转，0x02
{
 
  if(servoVal == 0x01)     //左转
  {
    servoNum = servoNum + 5;
    if(servoNum >= 180)servoNum = 180;
  }
   if(servoVal == 0x02)    //右转
   {
      if(servoNum != 0)
         servoNum = servoNum - 5;
   }
    myservo.write(servoNum);          //舵机数值传入 
}

void MQ_2()
{
  if( digitalRead(MQ_DO) == LOW )   //当DO引脚接收到低电平时候说明，模拟值超过比较器阀值
  {                   //通过调节传感器上的电位器可以改变阀值   
    yanwu_flag = 1;
  }
  else
  {  
    yanwu_flag = 0;
  }
}

void FindRfid()
{
 // 找卡
  if ( ! rfid.PICC_IsNewCardPresent())
    return;
 
  // 验证NUID是否可读
  if ( ! rfid.PICC_ReadCardSerial())
    return;
 
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
 
  // 检查是否MIFARE卡类型
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI &&  
    piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
    piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println("不支持读取此卡类型");
    return;
  }
  
  // 将NUID保存到nuidPICC数组
  for (byte i = 0; i < 4; i++)
  {
    nuidPICC[i] = rfid.uid.uidByte[i];
  }   

   if(nuidPICC[0] == blue)  //正确的卡
  {
    ka_flag = 0;
  }
  else
  {
    ka_flag = 1; 
  }
  
  // 使放置在读卡区的IC卡进入休眠状态，不再重复读卡
  rfid.PICC_HaltA();
 
  // 停止读卡模块编码
  rfid.PCD_StopCrypto1();  
}

void rentihongwai()
{
     if (digitalRead(buttonPin) == HIGH) 
     {     
       renti_flag = 1;    //检测到有人
       delay(50);
     } 
     else {
       renti_flag = 0;
       delay(50);
     }
}

void serialRead()
{
char count= 0;           //数据计数器 
dataLen = Serial.available();     //检测数据长度
  while( Serial.available() >0)
  {
     DATA[count++] =char(Serial.read()); 
   } 
   delay(5);
   if(dataLen == dataLenPro && DATA[dataLenPro-1] == dataEnd)       //检测数组长度是否符合，并且结尾为dataEnd
   {
       servoVal = DATA[0];        //舵机控制
       app = DATA[1];
       feng_flag = DATA[2];        //风扇控制
       buzzer_flag = DATA[3];       //蜂鸣器控制
       cameraControl();                 //舵机控制  
   } 
}

void sendAllData()
{
  Serial.write(sendData[0]); 
  Serial.write(sendData[1]);      
}


void jingbao()
{
  if(app == 0x01)               //自动控制
  {
    if(yanwu_flag == 1)
    {                 
      buzzerInit(1);              //打开蜂鸣器
      fengshanInit(1);            //打开风扇
    }
    else if((yanwu_flag == 0) && ((ka_flag == 1) || (renti_flag == 1)))
    {
      buzzerInit(1);         //打开蜂鸣器
      fengshanInit(0);       //关闭蜂鸣器
    }
    else if((yanwu_flag == 0) && (ka_flag == 0) && (renti_flag == 0))
    {
      buzzerInit(0);
      fengshanInit(0);
    }
  }
  
  else if(app == 0x02)           //手动控制
  {
    if((feng_flag == 0x01) && (buzzer_flag == 0x01))
    {
        buzzerInit(0);
      fengshanInit(0);
    }
    else if((feng_flag == 0x01) && (buzzer_flag == 0x02))
    {
      buzzerInit(1);
      fengshanInit(0);
    }
    else if((feng_flag == 0x02) && (buzzer_flag == 0x01))
    {
      buzzerInit(0);
      fengshanInit(1);
      
    }  
    else if((feng_flag == 0x02) && (buzzer_flag == 0x02))
    {
      buzzerInit(1);
      fengshanInit(1);
      
    }
  }
   
}

void loop()
{
  serialRead();   //初始化串口
  FindRfid();//初始化门禁卡
  rentihongwai();//初始化人体红外
  MQ_2();//初始化烟雾模块
  jingbao(); //初始化报警

  runNum ++;           
  if(runNum >= 20)          //程序循环10次    延时
  {
    MQ_Value = analogRead(MQ);   //读取MQ引脚的模拟值，该值大小0-1023
    sendData[0]  = 100  * MQ_Value / 1023;
    runNum = 0;   
    sendAllData();         //发送数据         
  }
}
