import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;

 /*-------------------------------------------------------------
 	Mohammad Khan
 	MLP3D.java 
    Neural Network MLP with Vision
    Computational Intelligence Final Project
    Due Date:  12/11/2014 

    Implementation of Neural Network that can take in as many
    inputs as desired with as many desired outputs per training 
    set. The inputs are read in from a file into a 2D array. 
    There are 4 hidden layer nodes - 2 in each layer. 

    The input is normalized to work with the neural network,
    meaning the numbers are chosen such that computation time
    is not excessive. In another class - Pixel.java - the 
    pixel values are read in from an image and values are written
    onto a text file. Those values are then the inputs and desired
    outputs for this program. 

    The final training was undertaken with 9 inputs. 
    This class uses methods of the Perceptron class which is 
    implemented elsewhere.
 ---------------------------------------------------------------*/

public class MLP3D
{

	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		    String filename = "/home/owais/Desktop/Project/input.txt"; 
		 
		    Scanner read= new Scanner(System.in);
		    System.out.println("How many test sets?");
		    int a= read.nextInt();
		    System.out.println("What is the length of the inputs?");
		    int b= read.nextInt();
		    System.out.println("What is the length of the outputs?");
		    int c= read.nextInt();
		    System.out.println("How many cycles do you want to run? Enter Number X: ");
			int r= read.nextInt();
		    read.close();
		    
		    double[][] inp=null;

		    try
		    {
		    FileReader readConnectionToFile = new FileReader(filename);
		    BufferedReader reads = new BufferedReader(readConnectionToFile);
		    Scanner scan = new Scanner(reads);

		    inp = new double[a][(b+c)];
		    int counter = 0;
		    

		    // reads input from file into a 2D array
		    try
		    {
		        while(scan.hasNext() && counter < a)
		        {
		            for(int i = 0; i < a; i++)
		            {
		                counter = counter + 1;
		                for(int m = 0; m < (b+c); m++)
		                {
		                    inp[i][m] = scan.nextDouble();
		                }
		            }
		        }

			    int z=0;
			    for(int i = 0; i < a; i++)
			    {
			       System.out.print("Set " + (i + 1) + " is: " );
			       z=0;
			       while(z<(b+c))
			       {
			          System.out.print(inp[i][z] + " ");
			          z++;
			       }
			       System.out.println();
			    }

		    } catch(InputMismatchException e)
		    {
		        System.out.println("Error converting number");
		    }
		    scan.close();
		    reads.close();
		    } catch (FileNotFoundException e)
		    {
		        System.out.println("File not found" + filename);
		    } catch (IOException e)
		    {
		        System.out.println("IO-Error open/close of file" + filename);
		    }
		    
		    //creates the output layer Perceptron
		    Perceptron[] per= new Perceptron[c];
			for(int i=0; i<c;i++)
			{
				per[i]=new Perceptron();
				per[i].sizeofStrings(2);
			}
			
			// creates the hidden layer Perceptron, where 
			// two in first hidden layer and two in second
			Perceptron hid1= new Perceptron();
			Perceptron hid2= new Perceptron();
			Perceptron hid3= new Perceptron();
			Perceptron hid4= new Perceptron();
			
			// sets how many inputs can go into Perceptron
			hid1.sizeofStrings(b);
			hid2.sizeofStrings(b);
			hid3.sizeofStrings(2);
			hid4.sizeofStrings(2);
			
			// sets random initial weights
			hid1.setWeight();
			hid2.setWeight();
			hid3.setWeight();
			hid4.setWeight();
			for(int g=0;g<c;g++)
			{
				per[g].sizeofStrings(2);
				per[g].setWeight();
			}
			
			
			int s=0;//iterations to r
			int y=0;//track cycles

			// loop that runs through cycles - updating weights and producing outputs
			// based on error and error gradients. Input is given from the 2D array
			while(s<r)
			{
				y++;
				System.out.println();
				System.out.println();
				System.out.println(" Cycle: " + y);

				int e=0; 
				while(e<a)//goes line by line from user inputs
				{
					// puts bias input of -1.0 into each node
					hid1.input(-1.0);
					hid2.input(-1.0);
					hid3.input(-1.0);
					hid4.input(-1.0);
					for(int i=0;i<b;i++)
					{
						hid1.input((double)inp[e][i]);
						hid2.input((double)inp[e][i]);
					}
					System.out.println();
					//System.out.println("inputs");
					//hid1.printIn();
					

					hid1.calcOutput();
					hid2.calcOutput();
					
					// inputs output of hidden nodes in first layer 
					// hidden nodes in second layer
					hid3.input(hid1.getOutput());
					hid3.input(hid2.getOutput());
					hid3.calcOutput();
					
					hid4.input(hid1.getOutput());
					hid4.input(hid2.getOutput());
					hid4.calcOutput();
					
					int x=0;
					//System.out.print("							Desired Output: " );

					// sets the desired output for the output layer
					for(int g=b;g<(b+c);g++)
					{
						per[x].setDesiredOutput((double)inp[e][g]);
						//System.out.print("[" +per[x].getDesiredOutput()+"]");
						x++;
					}
					
					// inputs the outputs of the second hidden layer into output layer
					// and it also prints and calculates the error 
					for(int g=0;g<c;g++)
					{
							per[g].input(-1.00);
							per[g].input(hid3.getOutput());
							per[g].input(hid4.getOutput());
							per[g].calcOutput();
							System.out.println("output "+ (g+1) + ":	"+ per[g].getOutput());
							System.out.println("error: 			             " + Math.abs(per[g].error()));
					}
					
					// these variables will keep track of the error gradient summations by being summed
					// and in turn will be used as parameters in determining the different error gradients
					double v=0.0;
					double g=0.0;
					double f=0.0;
					double j=0.0;
					
					for(int g6=0;g6<c;g6++)
					{
						v+=per[g6].deltaOut() *per[g6].getWeight(1);
					}	
					for(int g6=0;g6<c;g6++)
					{
						g+=per[g6].deltaOut() *per[g6].getWeight(2);
					}	
					
					for(int g6=0;g6<c;g6++)
					{
						per[g6].updateWeightOut();
					}	
					
				    f= (hid3.deltaHid1(hid3.getOutput(), v))*(hid3.getWeight(1)) + (hid4.deltaHid2(hid4.getOutput(),g))*(hid4.getWeight(1));
				    j= (hid3.deltaHid1(hid3.getOutput(), v))*(hid3.getWeight(2)) + (hid4.deltaHid2(hid4.getOutput(),g))*(hid4.getWeight(2));
				    
					hid3.deltaHid1(hid3.getOutput(), v);
					hid3.updateWeightHid(1);
					hid4.deltaHid2(hid4.getOutput(),g);
					hid4.updateWeightHid(2);
					
					hid1.deltaHidLast1(hid1.getOutput(),f);
					hid1.updateWeightHid2(1);
					
					hid2.deltaHidLast2(hid2.getOutput(),j);
					hid2.updateWeightHid2(2);
					e++;
				}
				s++;
			}

			// after the learning is done with the desired cycles - the weights are printed for each node
			// I personally then copy and pasted them into a text file to be input into the trained NN
			hid1.printWeight();
			hid2.printWeight();
			hid3.printWeight();
			hid4.printWeight();
			per[0].printWeight();
		}    			
	}
