#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

const char wifi_name[]     = "MERCURY_63EF";         // 当前的 wifi 名称
const char wifi_password[] = "123456";               //  当前的 wifi 密码
const char pc_ip[] = "192.168.3.255";

byte mac_1 = 0x43;
byte mac_2 = 0xE8;

byte mac_3 = 0x39;
byte mac_4 = 0x71;

byte mac_5 = 0x49;
byte mac_6 = 0x2A;


const char server_ip[] = "127.0.0.1";
const int  server_port = 45311;
String dev_id = "hlmio";

String rst_open = "201";
String rst_shutdown = "202";
String register_devId_info = "id:=" + dev_id;
String keep_alive_info = "keep alive :  " + dev_id;

WiFiClient client;
WiFiUDP Udp;

int state; 
String rst;
int loop_count = 0;

byte wake_msg[108];
//
// 构建pc唤醒魔术包
//
byte* build_wake_msg(byte mac_1=mac_1,byte mac_2=mac_2,byte mac_3=mac_3,
                     byte mac_4=mac_4,byte mac_5=mac_5,byte mac_6=mac_6) 
{
    for (int j = 0; j < 6; j++)
    {
        wake_msg[j] = 0xFF;
    }
    for (int j = 0; j < 16; j++)
    {
        wake_msg[6 + j * 6 + 0] = mac_1;
        wake_msg[6 + j * 6 + 1] = mac_2;
        wake_msg[6 + j * 6 + 2] = mac_3;
        wake_msg[6 + j * 6 + 3] = mac_4;
        wake_msg[6 + j * 6 + 4] = mac_5;
        wake_msg[6 + j * 6 + 5] = mac_6;
    }
    for (int j = 102; j < 108; j++)
    {
        wake_msg[j] = 0x00;
    }
    return wake_msg;
}

//
// 发送udp包，唤醒电脑
//
void open_pc(byte* wake_msg) 
{
    Udp.beginPacket(pc_ip,45313);
    byte mypacket[108];
    for(int j=0;j<108;j++){
        Udp.write(wake_msg[j]);
    }
    Udp.endPacket();
    delay(100);
}

//
// 连接wifi
//
void check_and_link_wifi() 
{
    if(WiFi.status() != WL_CONNECTED){
        WiFi.begin(wifi_name, wifi_password);
        while (WiFi.status() != WL_CONNECTED) 
        {
          delay(1000);
        }
    }
}

//
// 连接服务器的tcp
//
int check_and_link_tcp() 
{
    if(!client.connected()){
        if(client.connect(server_ip, server_port)){
            client.write(register_devId_info.c_str());
            return 1;
        }else{
            return 0;
        }
    }
}

void setup() {
    // put your setup code here, to run once:
    Serial.println("0");
    
    Serial.begin(115200);
    Serial.println("hello world");
    WiFi.mode(WIFI_AP_STA);               //设置工作模式 
    WiFi.setAutoConnect(false);
    delay(100);
    Serial.println("1");
    
    // 连接到wifi
    check_and_link_wifi();
    Serial.println("2");
    
    //建立tcp连接并判断
    check_and_link_tcp();
    Serial.println("3");

    build_wake_msg();
    open_pc(wake_msg);
    Serial.println("4");

    delay(1000);
}

void loop() {
    // put your main code here, to run repeatedly:
    ++ loop_count;
    check_and_link_wifi();
    state = check_and_link_tcp();
    if (state == 1){
        while (client.available())//available()表示是否可以获取到数据
        {
            rst = client.readString();
			      Serial.println(rst);
            if(rst == rst_open or rst.indexOf(rst_open)>-1){
               open_pc(wake_msg);
            }
    //        if(rst == rst_shutdown or rst.indexOf(rst_shutdown)>-1){
    //        }
        }
        if(loop_count>20){
          loop_count = 1;
          client.write(keep_alive_info.c_str());
        }
    }
    delay(3000);
}
