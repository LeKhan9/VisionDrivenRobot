 /*-------------------------------------------------------------
 	Mohammad Khan
 	Perceptron.java 
    Neural Network Perceptron
    Computational Intelligence Final Project
    Due Date:  12/11/2014 

    Implementation of Perceptron that will can take in as many
    inputs as desired. The inputs are read in from a file into a 
    2D array. This class will be applied to form a larger network 
    with interconnections, and multiple layers. Each Perceptron 
    in the network will be a node.

    Sigmoid activations functions are used to squash the values 
    between 0 and 1. This program also has function to get desired
    output and calculate error. Weights are updated using the 
    calculated errors. Error gradients are calculated and used 
    for the same purposes. 
 ---------------------------------------------------------------*/

public class Perceptron 
{	
	// creates arrays for inputs, weights, anddelta values
	// the global variables here dictate the use of deltas and
	// outputs in more than one function
	int i=-1;
	double add;
	double desiredOutput;
	double input[];
	double weight[];
	double delta1[];
	double output;
	
	
	int limiter;
	int numOuts;
	int ins=-1;
	
	double outj3;
	double outj4;
	double outi1;
	double outi2;
	
	double deltaHid1;
	double deltaHid2;
	
	double deltaLast1;
	double deltaLast2;
	
	// define how many inputs can go into each node
	public void sizeofStrings(int x)
	{
		input= new double[x+1];
		weight= new double[x+1];
		limiter=x;
	}
	
	// actually inputs the value into the Perceptron via array
	public void input(double x)
	{
		i++;
		if(i==limiter+1)
		{
			i=0;
		}
		input[i]=x;
	}
	
	// prints the inputs
	public void printIn()
	{
		for(int y=1; y<=limiter; y++)
		{
			System.out.print("["+ input[y]+"] " );
		}
	}

	// calculates the output using the two arrays of weights 
	// and inputs. The multiplication between the two is summed 
	// over a range.
	public void calcOutput()
	{
		add=0.0;
		for(int x=0;x<=i;x++)
		{
			double a=input[x];
			double b=weight[x];
			
			double k= (a*b);
			add+=k;
		}
	}
	
	// the output from the sums is squashed with 
	// a sigmoid activation function between 0 and 1
	public double getOutput()
	{
		output= 1 / (1 + Math.exp(-add));
		return output;
	}
	
	// the user can enter the desired output of the node
	public void setDesiredOutput(double x)
	{
		desiredOutput= x;
	}
	
	public double getDesiredOutput()
	{
		return desiredOutput;
	}

	// initializes random weights for the nodes. Here the weight 
	// at position 1 is always for the bias unit input
	public void setWeight()
	{
		System.out.println("Set initial  random weight for threshold:    ");
		weight[0]=(double)(Math.random()*1);
		
		System.out.println(weight[0]);
		
		for(int u=1;u<=limiter;u++)
		{
			System.out.println("Set initial  random weight for input:    ");
			weight[u]=(double)(Math.random()*1);
			System.out.println(weight[u]);
		}
	} 

	// Can set the weights if the NN is already trained
	public void setWeightNew(int x, double y)
	{
		weight[x]=y;
	}

	public double error()
	{
		return (desiredOutput-output);
	}
	
	// updates weights with use of error and the learning rate
	// the learning rate can be adjusted to fine tune the network
	public void updateWeight()
	{
			double rate=.01;
			
			for(int v=0;v<=limiter;v++)
			{
				weight[v]= weight[v]+ (rate*input[v]*error());
			}
	}
	
	// the outer layer weights are updated after an iteration
	public void updateWeightOut()
	{
		double rate=.01;
		
		for(int v=0;v<=limiter;v++)
		{
			weight[v]= weight[v]+ (rate*input[v]*deltaOut());
		}
	}
	
	// the first of the two hidden layer's weights are updated
	// and the parameter determines which delta (error gradient is used)
	public void updateWeightHid(int x)
	{
		double rate=.01;

		if(x==1)
		{
			for(int v=0;v<=limiter;v++)
			{
				weight[v]= weight[v]+ (rate*input[v]*deltaHid1);
			}
		}
		if(x==2)
		{
			for(int v=0;v<=limiter;v++)
			{
				weight[v]= weight[v]+ (rate*input[v]*deltaHid2);
			}
		}
	}
	
	// the second of the two hidden layer's weights are updated
	public void updateWeightHid2(int x)
	{
		double rate=.01;
		
		if(x==1)
		{
			for(int v=0;v<=limiter;v++)
			{
				weight[v]= weight[v]+ (rate*input[v]*deltaLast1);
			}
		}
		if(x==2)
		{
			for(int v=0;v<=limiter;v++)
			{
				weight[v]= weight[v]+ (rate*input[v]*deltaLast2);
			}
		}
	}
	
	public void printWeight()
	{
		System.out.println("T-weight:  " + weight[0]);
		for(int n=1;n<=limiter;n++)
		{
			System.out.println("weight" + n + ":  " + weight[n]);
		}
	}
	
	public double getWeight(int x)
	{
		return weight[x];
	}
	
	// calculates the error gradient for outer layer
	public double deltaOut()
	{
		double x= (getOutput() *( 1-getOutput())* error());
		return x;
	}

	// calculates the error gradient for the first of two hidden nodes in 
	// the last hidden layer
	public double deltaHid1(double x, double y )
	{
		outj3=x;
		double a=outj3*(1-outj3)*y;
		deltaHid1=a;
		return a;
	}
	
	// calculates error gradient for the second of two hidden nodes
	// in the last hidden layer
	public double deltaHid2(double x, double y )
	{
		outj4=x;
		double a=outj4*(1-outj4)*y;
		deltaHid2=a;
		return a;
	}
	
	// calculates error gradient for the first of two hidden nodes
	// in the first hidden layer
	public double deltaHidLast1(double x, double y)
	{
		outi1=x;
		double a= outi1*(1-outi1)*y;
		deltaLast1=a;
		return a;
	}
	
	// calculates error gradient for the second of two hidden nodes
	// in the first hidden layer
	public double deltaHidLast2(double x, double y)
	{
		outi2=x;
		double a= outi2*(1-outi2)*y;
		deltaLast2=a;
		return a;
	}
}