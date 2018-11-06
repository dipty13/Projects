package layers;

import java.util.ArrayList;
import java.util.Random;

public class Neuron {

	private ArrayList<Double> listOfWeightIn; //An ArrayList variable of real numbers that represents the list of input weights
	private ArrayList<Double> listOfWeightOut;//An ArrayList variable of real numbers that represents the list of output weights
	private double outputValue;
	private double error;
	private double sensibility;
	
	/*Initializes listOfWeightIn and listOfWeightOut function with a 
	 * pseudo random real number and returns A pseudo random real number
	 */
	public double initNeuron(){
		Random r = new Random();
		return r.nextDouble();
	}
	
	

	/*Sets the listOfWeightIn function with a list of
		real numbers list
	 * 
	 */
	public void setListOfWeightIn(ArrayList<Double> listOfWeightIn) {
		this.listOfWeightIn = listOfWeightIn;
	}
	
	//Returns the input weights a list of neurons
		public ArrayList<Double> getListOfWeightIn() {
			return listOfWeightIn;
		}

	

	/*
	 * Sets the listOfWeightOut function with a list
		of real numbers list
	 */
	public void setListOfWeightOut(ArrayList<Double> listOfWeightOut) {
		this.listOfWeightOut = listOfWeightOut;
	}

	//Returns the output weights a list of neurons
		public ArrayList<Double> getListOfWeightOut() {
			return listOfWeightOut;
		}
		
	public double getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(double outputValue) {
		this.outputValue = outputValue;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getSensibility() {
		return sensibility;
	}

	public void setSensibility(double sensibility) {
		this.sensibility = sensibility;
	}
	
}
