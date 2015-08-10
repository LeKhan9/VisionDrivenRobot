
int input[9];
String inString = ""; 
void setup() {

  Serial.begin(9600);
 
  Serial.println("\n\nString toInt():");
  Serial.println();
}

void loop() {
  // Read serial input:
  while (Serial.available() > 0) {
    int inChar = Serial.read();
    if (isDigit(inChar)) {
      // convert the incoming byte to a char
      inString += (char)inChar;
    }
        Serial.print(inString); 

   
      int a1= (inString.charAt(0))-'0';
      int a2= (inString.charAt(1))-'0';
      int a3= (inString.charAt(2))-'0';
      int a4= (inString.charAt(3))-'0';
      int a5= (inString.charAt(4))-'0';
      int a6= (inString.charAt(5))-'0';
      int a7= (inString.charAt(6))-'0';
      int a8= (inString.charAt(7))-'0';
      int a9= (inString.charAt(8))-'0';
      
      Serial.print(a1 +"," );
      Serial.println(a8);
   
      // clear the string
   
          inString = "";

  }
}
