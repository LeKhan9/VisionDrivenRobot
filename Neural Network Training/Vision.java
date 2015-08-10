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
 	Vision.java
    Trained Neural Network MLP 
    Computational Intelligence Final Project
    Due Date:  12/11/2014 

    This program takes input from a text file with non-training
    sets for the purpose of testing the performance of the neural 
    network. The neural network in this program has been trained 
    in another class - MLP3D.java - and the weights have been 
    input into this class. The output determines the "Decision"
    for the robot during its runs through a colony space with 
    blocks to avoid hitting and to help dictate movement.
 ---------------------------------------------------------------*/

public class Vision
{
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		    String filename = "/home/owais/Desktop/Project/new_weights.txt"; 
		 
		    Scanner read= new Scanner(System.in);
		    System.out.println("How many test sets?");
		    int a= read.nextInt();
		    System.out.println("What is the length of the inputs?");
		    int b= read.nextInt();
		    read.close();
		    
		    double[][] inp=null;

		    // reads input from file into a 2D array
		    try
		    {
		    FileReader readConnectionToFile = new FileReader(filename);
		    BufferedReader reads = new BufferedReader(readConnectionToFile);
		    Scanner scan = new Scanner(reads);

		    inp = new double[a][(b)];
		    int counter = 0;
		    try
		    {
		        while(scan.hasNext() && counter < a)
		        {
		            for(int i = 0; i < a; i++)
		            {
		                counter = counter + 1;
		                for(int m = 0; m < (b); m++)
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
			       while(z<(b))
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
		    Perceptron[] per= new Perceptron[1];
			for(int i=0; i<1;i++)
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
			
			// sets how many inputs can go into Perceptr
			hid1.sizeofStrings(b);
			hid2.sizeofStrings(b);
			hid3.sizeofStrings(2);
			hid4.sizeofStrings(2);
			
			
			// Weights from training have been input here
			hid1.setWeightNew(0,-1.038644185967649);
			hid1.setWeightNew(1,3.844302425706347);
			hid1.setWeightNew(2,-2.325799271495259);
			hid1.setWeightNew(3, 3.8048399811267313);
			hid1.setWeightNew(4,-2.2316886871011237);
			hid1.setWeightNew(5,-3.7799188948762903);
			hid1.setWeightNew(6,-1.081538669917152);
			hid1.setWeightNew(7,0.7491565656754553);
			hid1.setWeightNew(8,-1.5931767331568287);
			hid1.setWeightNew(9,-0.20292637832352764);
			
			hid2.setWeightNew(0,-1.7237550803567183);
			hid2.setWeightNew(1,-1.6264232027414525);
			hid2.setWeightNew(2,-3.16861317000721);
			hid2.setWeightNew(3,2.6130140728519815);
			hid2.setWeightNew(4,-5.476491311398075);
			hid2.setWeightNew(5,-1.8801435156806459);
			hid2.setWeightNew(6,2.6200283092459014);
			hid2.setWeightNew(7,0.6582823386552139);
			hid2.setWeightNew(8,0.4456747781648002);
			hid2.setWeightNew(9,4.118930788084232);			
			
			hid3.setWeightNew(0,2.7159775041029826);
			hid3.setWeightNew(1,-3.2531657042217486);
			hid3.setWeightNew(2,6.4282859951243445);
				
			
			hid4.setWeightNew(0,2.089195177532419);
			hid4.setWeightNew(1,-3.503891496423567);
			hid4.setWeightNew(2,5.1561781667273205);
			
			for(int g=0;g<1;g++)
			{
				per[g].sizeofStrings(2);
				per[g].setWeightNew(0,7.161487129399436);
				per[g].setWeightNew(1,7.663917310213811);
				per[g].setWeightNew(2,6.316482792234619);
			}
			
				int e=0; 
				while(e<a)//goes line by line from user inputs
				{
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
					System.out.println("inputs");
					hid1.printIn();
					
					hid1.calcOutput();
					hid2.calcOutput();
					
					hid3.input(hid1.getOutput());
					hid3.input(hid2.getOutput());
					hid3.calcOutput();
					
					hid4.input(hid1.getOutput());
					hid4.input(hid2.getOutput());
					hid4.calcOutput();
							
					System.out.println();
					
					for(int g=0;g<1;g++)
					{
							per[g].input(-1.00);
							per[g].input(hid3.getOutput());
							per[g].input(hid4.getOutput());
							per[g].calcOutput();
							System.out.println("output :"	+ per[g].getOutput());
					}
					double out =per[0].getOutput();

					// Determines linguistic definitions based on output
					// value produced from Neural Network. In the future
					// to be actual methods for movements of a motor.
					if(out>=0 && out<=.37)
					{
						System.out.println("Turn Left");
					}
					if(out>.37 && out<=.65)
					{
						System.out.println("Go Forward");
					}
					if(out>.65 && out<=1)
					{
						System.out.println("Turn Right");
					}
					e++;
				}
		}    			
	}
