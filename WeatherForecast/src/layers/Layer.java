package layers;

import java.util.ArrayList;

import layers.Neuron;

public abstract class Layer {

	private ArrayList<Neuron> listOfNeurons; //An ArrayList variable of objects of the Neuron class
	protected int numberOfNeuronsInLayer; //Integer number to store the quantity of neurons that are part of the layer
	
	public void printLayer(){
	}

	/*
	 * Returns An ArrayList variable of objects by
        the Neuron class
	 */
	public ArrayList<Neuron> getListOfNeurons() {
		return listOfNeurons;
	}

	/*
	 * Sets the listOfNeurons function with
		an ArrayList variable of objects of the
		Neuron class
	 */
	public void setListOfNeurons(ArrayList<Neuron> listOfNeurons) {
		this.listOfNeurons = listOfNeurons;
	}
	
	
	//Returns the number of neurons by layer
	public int getNumberOfNeuronsInLayer() {
		return numberOfNeuronsInLayer;
	}

	//Sets the number of neurons in a layer
	public void setNumberOfNeuronsInLayer(int numberOfNeuronsInLayer) {
		this.numberOfNeuronsInLayer = numberOfNeuronsInLayer;
	}
	
	
	
	
}
