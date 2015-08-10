package com.example.arduinosensors;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
  
public class MainActivity extends Activity {
    
  Button  btnOff;
  TextView txtArduino, txtString, txtStringLength;
  Handler bluetoothIn;

    Button button;
    int REQUEST_CODE =1;
    ImageView IMG;

  final int handlerState = 0;
  private BluetoothAdapter btAdapter = null;
  private BluetoothSocket btSocket = null;
  private StringBuilder recDataString = new StringBuilder();
   
  private ConnectedThread mConnectedThread;
    
  private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  private static String address;

@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.activity_main);
  
    btnOff = (Button) findViewById(R.id.buttonOff);
    txtString = (TextView) findViewById(R.id.txtString); 
    txtStringLength = (TextView) findViewById(R.id.testView1);   


    bluetoothIn = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {
            	String readMessage = (String) msg.obj;
                recDataString.append(readMessage);
                int endOfLineIndex = recDataString.indexOf("~");
                if (endOfLineIndex > 0) {
                    String dataInPrint = recDataString.substring(0, endOfLineIndex);
                    txtString.setText("Data Received = " + dataInPrint);
                    int dataLength = dataInPrint.length();
                    txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                    if (recDataString.charAt(0) == '#')
                    {
                    	String sensor0 = recDataString.substring(1, 5);
                    	String sensor1 = recDataString.substring(6, 10);
                    	String sensor2 = recDataString.substring(11, 15);
                    	String sensor3 = recDataString.substring(16, 20);
                    }
                    recDataString.delete(0, recDataString.length());
                    dataInPrint = " ";
                }
            }
        }
    };
      
    btAdapter = BluetoothAdapter.getDefaultAdapter();
    checkBTState();

    button =(Button) findViewById(R.id.button);
    IMG= (ImageView) findViewById(R.id.imageView);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(getPackageManager())!=null)
            {
                startActivityForResult(i,REQUEST_CODE);
            }

        }
    });

    // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
    btnOff.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        mConnectedThread.write(text);    // Send "0" via Bluetooth
         Toast.makeText(getApplicationContext(),  "SENT!", Toast.LENGTH_LONG).show();

      }
    });


  }

  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
      
      return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
      //creates secure outgoing connecetion with BT device using UUID
  }
    
  @Override
  public void onResume() {
    super.onResume();

    Intent intent = getIntent();
    
    address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

    BluetoothDevice device = btAdapter.getRemoteDevice(address);
     
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e) {
    	Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
    }  

    try
    {
      btSocket.connect();
    } catch (IOException e) {
      try 
      {
        btSocket.close();
      } catch (IOException e2) 
      {
      }
    } 
    mConnectedThread = new ConnectedThread(btSocket);
    mConnectedThread.start();

    mConnectedThread.write("x");
  }
  
  @Override
  public void onPause() 
  {
    super.onPause();
    try
    {
      btSocket.close();
    } catch (IOException e2) {
    }
  }

  private void checkBTState() {
 
    if(btAdapter==null) { 
    	Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
    } else {
      if (btAdapter.isEnabled()) {
      } else {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
      }
    }
  }
  
  private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
      
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
      
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        
      
        public void run() {
            byte[] buffer = new byte[256];  
            int bytes; 
 
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {  
            	Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            	finish();
              }
        	}
    	}

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }


    private static int[] convert(String string) {
        int number[] = new int[string.length()];

        for (int i = 0; i < string.length(); i++) {
            number[i] = Integer.parseInt(String.valueOf(string.charAt(i)));
        }
        return number;
    }

    public double summation(double[] array1, double[] array2, int index)
    {
        double k=0;
        for(int i=0; i<=index;i++)
        {
            k+= array1[i]*array2[i];
        }
        return k;
    }

    public double calcOutput(double x)
    {
        return 1 / (1 + Math.exp(-x));
    }

    String text = "";

    public void onActivityResult(int requestcode, int resultcode, Intent data)
    {
        if(requestcode == REQUEST_CODE)
        {
            if(resultcode == RESULT_OK)
            {
                Bundle bundle = new Bundle();
                bundle = data.getExtras();
                Bitmap BMP;
                BMP = (Bitmap) bundle.get("data");
                BMP = Bitmap.createScaledBitmap(BMP, 9, 9, false);
                BMP = toGrayscale(BMP);

                text = "";
                for(int i=0; i<BMP.getWidth(); i++) {

                    int pix = BMP.getPixel( i, BMP.getHeight() / 2);
                    int red = Color.red(pix);
                    int blue = Color.blue(pix);
                    int green = Color.green(pix);


                    double sum = Math.sqrt(.241*red*red + .068*blue*blue + .691*green*green);

                    if(sum>=113)
                    {
                        sum =0;
                    }
                    else
                    {
                        sum=1;
                    }

                    text += String.valueOf((int)sum);
                }

                //text += "\n";

                int array[] = convert(text);

                double input1[]= new double[10];//hid1
                double input2[]= new double[10];//hid2
                double input3[]= new double[3];//hid3
                double input4[]= new double[3];//hid4
                double input5[]= new double[3];//out

                input1[0]=-1.0;
                input2[0]=-1.0;
                input3[0]=-1.0;
                input4[0]=-1.0;
                input5[0]=-1.0;


                input1[1]=array[0];
                input2[1]=array[0];

                input1[2]=array[1];
                input2[2]=array[1];

                input1[3]=array[2];
                input2[3]=array[2];

                input1[4]=array[3];
                input2[4]=array[3];

                input1[5]=array[4];
                input2[5]=array[4];

                input1[6]=array[5];
                input2[6]=array[5];

                input1[7]=array[6];
                input2[7]=array[6];

                input1[8]=array[7];
                input2[8]=array[7];

                input1[9]=array[8];
                input2[9]=array[8];


                double weight1[]= new double[10];//hid1
                double weight2[]= new double[10];//hid2
                double weight3[]= new double[3];//hid3
                double weight4[]= new double[3];//hid4
                double weight5[]= new double[3];//out


                weight1[0]= -4.448516863433532;//threshold
                weight1[1]= -5.1661299270385435;//input1
                weight1[2]= -1.904595583206199;//input2
                weight1[3]= -1.6158938186059753;//input3
                weight1[4]= -1.6662391370401297;//input4
                weight1[5]= -1.2188780925207225;//input5
                weight1[6]= 1.4980301732599104;//input6
                weight1[7]=  -0.32128015714137975;//input7
                weight1[8]= 1.8941399951030629;//input8
                weight1[9]= 5.578394305343655;//input9

                weight2[0]= 3.472705508716659;
                weight2[1]= 0.37313136819524206;
                weight2[2]= -1.9546299214367517;
                weight2[3]= 1.1018341762321813;
                weight2[4]= -1.78647593741318;
                weight2[5]= 3.0391438161604847;
                weight2[6]= 2.858602917074742;
                weight2[7]= -0.021276560927022427;
                weight2[8]= 1.962077850860825;
                weight2[9]= -0.5282283344557247;


                weight3[0]=  4.910090022205001;
                weight3[1]= 3.8768393408219293;
                weight3[2]= 5.155142991903531;

                weight4[0]= 4.082464299175923;
                weight4[1]= 4.939599838788214;
                weight4[2]=  2.702864036082112;


                weight5[0]= -7.390501976450356;
                weight5[1]= -8.647376859657003;
                weight5[2]= -6.973499042402019;

                double sigmoid1= summation(input1,weight1, 9);
                double sigmoid2= summation(input2,weight2,9);

                double out1 = calcOutput(sigmoid1);
                double out2 = calcOutput(sigmoid2);

                input3[1]=out1;
                input4[1]=out1;

                input3[2]=out2;
                input4[2]=out2;

                double sigmoid3= summation(input3,weight3,2);
                double sigmoid4= summation(input4,weight4,2);

                double out3= calcOutput(sigmoid3);
                double out4= calcOutput(sigmoid4);

                input5[1]=out3;
                input5[2]=out4;

                double sigmoid5= summation(input5,weight5,2);
                double out5= calcOutput(sigmoid5);

                String outer = "";
                if(out5>=0 && out5<=.37)
                {
                    text = "00";
                    outer = "left";

                }
                if(out5>.37 && out5<=.65)
                {
                    text = "10";
                    outer= "forward";
                }
                if(out5>.65 && out5<=1)
                {
                    text = "11";
                    outer= "right";
                }

                text += "\n";


                Toast.makeText(getApplicationContext(),  outer, Toast.LENGTH_LONG).show();

                //IMG.setImageBitmap(BMP);
            }
        }
    }

}
    
