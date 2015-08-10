import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

 /*-------------------------------------------------------------
    Mohammad Khan
    Pixel.java
    Image pixel extraction program
    Computational Intelligence Final Project
    Due Date:  12/11/2014 

    This program takes pixel input from an image. The size of the
    image is 2592 as Height - initially labeled as "Width" in the
    image definition - and 1936 as Width - initially labled as 
    "Height" in the image definition. 

    The car will be taking input with one row of pixels at this 
    point. This was done to mitigate surrounding "distraction"
    pixels. The images have been normalized to black and white
    and the row of pixels taken has been cut into 9 equal blocks.

    The pixel values themselves refer to the RGB values of each
    pixel and the values are averaged over a range.

    In the future this class will have a GUI to be able to see 
    each image as the program runs and have a box where the user 
    can enter input 
 ---------------------------------------------------------------*/

class Pixel extends JFrame
{
   BufferedImage  image;
   int width;
   int height;
   int rows = 20;   // the number of images to be processed 
   int columns = 9; // the number of blocks in the pixel row
   int pictureCount=21;
   double[][] inp= new double[rows][columns+1];
   int y=1;

   // iterates through the pixels in each image and takes 
   // values into a 2D array partitioned into the number of blocks
   public void process() 
   {
      try 
      {
         while (y<pictureCount)
         {
               // the files are read automatically from a file
               // with incrementation as they are all labeled with 
               // the same name. 
               File input = new File("Test" + y + ".jpg");
               System.out.println("Picture "+ y);
               image = ImageIO.read(input);
               width = image.getWidth();
               height = image.getHeight();
               int sum=0;
               int index=0;

               // each k-th block increment the averaged sum 
               // value is written into the array
               for(int j=0; j<height; j++)
               {
                  if(j%215!=0 && j!=1935)
                  {
                     Color c = new Color(image.getRGB(width/2, j));
                     int r = c.getRed();
                     int g = c.getGreen();
                     int b = c.getBlue();
                     int average = (r+g+b)/(3);
                     sum+=average;
                  }
                  else if(j==0)
                  {
                     Color c = new Color(image.getRGB(width/2, j));
                     int r = c.getRed();
                     int g = c.getGreen();
                     int b = c.getBlue();
                     int average = (r+g+b)/(3);
                     sum+=average;
                  }
                  else if(j==1935)
                  {
                     Color c = new Color(image.getRGB(width/2, j));
                     int r = c.getRed();
                     int g = c.getGreen();
                     int b = c.getBlue();
                     int average = (r+g+b)/(3);
                     sum+=average;
                     sum=sum/215;
                     if(sum >=0 && sum <=140)
                     {
                        sum= 0;
                     }
                     if(sum>140 && sum<=205)
                     {
                        sum=1;
                     }
                     if(sum>205)
                     {
                        sum=2;
                     } 
                     inp[y-1][index] = sum;
                     sum=0;
                     index++;
                  }
                  else 
                  {
                     Color c = new Color(image.getRGB(width/2, j));
                     int r = c.getRed();
                     int g = c.getGreen();
                     int b = c.getBlue();
                     int average = (r+g+b)/(3);
                     sum+=average;
                     sum=sum/215;
                      if(sum >=0 && sum <=140)
                     {
                        sum= 0;
                     }
                     if(sum>140 && sum<=205)
                     {
                        sum=1;
                     }
                     if(sum>205)
                     {
                        sum=2;
                     }
                     inp[y-1][index] = sum;
                     sum=0;
                     index++;
                  }
               }
               y++;
            }  
         } catch (Exception e) {}
   }

   public void printArray()
   {
      int z=0;
      for(int i = 0; i < rows; i++)
      {
          System.out.print("Set " + (i + 1) + " is: " );
          z=0;
          while(z<(columns))
          {
             System.out.print(inp[i][z] + " ");
             z++;
          }
             System.out.println();
      }
   }

   public void decision() throws Exception  
   {
      // writes array values and decision values into a text
      // file that will be fed into the neural network for training
      PrintStream output = new PrintStream("input.txt");
      inp[0][9]=0;
      inp[1][9]=1;
      inp[2][9]=1;
      inp[3][9]=0;
      inp[4][9]=0;
      inp[5][9]=.5;
      inp[6][9]=0;
      inp[7][9]=0;
      inp[8][9]=0;
      inp[9][9]=0;
      inp[10][9]=.5;
      inp[11][9]=.5;
      inp[12][9]=.5;
      inp[13][9]=.5;
      inp[14][9]=0;
      inp[15][9]=0;
      inp[16][9]=1;
      inp[17][9]=.5;
      inp[18][9]=.5;
      inp[19][9]=0;
      
      for(int u=0;u<rows;u++)
      {
         for(int i=0; i<columns+1;i++)
         {
            if(i==9)
            {
               output.println("  "+ inp[u][i]);
            }
            else 
            {
               output.print(" "+ inp[u][i]);
            }
         }
      }
   }

   public static void main(String args[]) throws Exception  
   {
      Pixel obj = new Pixel();
      obj.process();
      obj.printArray();
      obj.decision();
   }
}