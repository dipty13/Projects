package layers;

import java.util.ArrayList;
import java.util.Arrays;

public class OutputLayer extends Layer {

	/*
	 * Initializes the output layer with pseudo random
		real numbers
		parameter is-An object of the OutputLayer class
	 */
	public OutputLayer initLayer(OutputLayer outputLayer) {
		ArrayList<Double> listOfWeightOutTemp = new ArrayList<Double>();
		ArrayList<Neuron> listOfNeurons = new ArrayList<Neuron>();

		for (int i = 0; i < outputLayer.getNumberOfNeuronsInLayer(); i++) {
			Neuron neuron = new Neuron();
			
			listOfWeightOutTemp.add(neuron.initNeuron());

			neuron.setListOfWeightOut(listOfWeightOutTemp);
			listOfNeurons.add(neuron);

			listOfWeightOutTemp = new ArrayList<Double>();
		}

		outputLayer.setListOfNeurons(listOfNeurons);
		
		return outputLayer; 
		
	}
	
	
	/*
	 * Prints the weights of the layer
	 */
	public void printLayer(OutputLayer outputLayer){
		System.out.println("### OUTPUT LAYER ###");
		int n = 1;
		for (Neuron neuron : outputLayer.getListOfNeurons()) {
			System.out.println("Neuron #" + n + ":");
			System.out.println("Output Weights:");
			System.out.println(Arrays.deepToString( neuron.getListOfWeightOut().toArray() ));
			n++;
		}
	}

}

