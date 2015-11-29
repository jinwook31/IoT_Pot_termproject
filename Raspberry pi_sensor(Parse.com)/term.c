#include <stdio.h>
#include <parse.h>
#include <wiringPi.h>

#define MAX_COUNT 85
#define DHT_PIN 1

int dhtVal[5] = {0,0,0,0,0};

void readData(void){
  unsigned short state = HIGH;
  unsigned short counter = 0;
  unsigned short j = 0, i;
  float farenheit;

  for(i=0; i<5; i++) dhtVal[i]=0;

  pinMode(DHT_PIN, OUTPUT);
  digitalWrite(DHT_PIN, LOW);
  delay(18);
  digitalWrite(DHT_PIN, HIGH);
  delayMicroseconds(40);
  pinMode(DHT_PIN, INPUT);

  for(i=0; i < MAX_COUNT; i++){
	counter = 0;

	while(digitalRead(DHT_PIN) == state){
		counter++;
		delayMicroseconds(1);
		if(counter == 255) break;
	};

	state = digitalRead(DHT_PIN);
	if(counter == 255) break;

	if((i>=4) && (i%2 == 0)){
		dhtVal[j/8] <<= 1;
		if(counter > 16) dhtVal[j/8] |= 1;
		j++;
	}
  };
  if((j>=40) && (dhtVal[4] == ((dhtVal[0] + dhtVal[1] + dhtVal[2] + dhtVal[3]) & 0xFF))){
	farenheit = dhtVal[2]*9./5.+32;
	printf("Humidity = %d.%d %% Tempreture = %d.%d *C (%.1f *F)\n",dhtVal[0],dhtVal[1], dhtVal[2], dhtVal[3], farenheit);

	int humSens = dhtVal[0];
	int tempSens = dhtVal[2];

	ParseClient client = parseInitialize("u2Z03LI8SBDu7Rwv7irJtCMdZqaDyzvE07PQwKvU", "h6xEHnAdTkgwfTKY65LD5VkeCGWXgNRxEtCq6cPP");
	char temp[100];
	snprintf(temp, sizeof(temp), "{ \"temperature\":  %d}", tempSens, humSens);
	parseSendRequest(client, "POST", "/1/classes/Temperature", temp, NULL);
	char hum[100];
	snprintf(hum, sizeof(hum),  "{ \"Humidity\":  %d}", humSens);
	parseSendRequest(client, "POST", "/1/classes/Humidity", hum, NULL);

  }else printf("Invlid Data!!\n");
}

int main(int argc, char** argv){

    if(wiringPiSetup () == -1)
	return -1;

    //Setup()

    //loop()
    for(;;){
	readData();
	delay(500);
    }
    return 0;
}
