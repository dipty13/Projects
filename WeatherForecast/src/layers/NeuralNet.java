package layers;

import java.util.ArrayList;

import learning.Adaline;
import learning.Backpropagation;
import learning.Perceptron;
import learning.Training.ActivationFncENUM;
import learning.Training.TrainingTypesENUM;
import som.Kohonen;

public class NeuralNet {

	private InputLayer inputLayer; //An object of the InputLayer class
	private HiddenLayer hiddenLayer; //An object of the HiddenLayer class
	private ArrayList<HiddenLayer> listOfHiddenLayer; //An ArrayList variable of objects of the HiddenLayer class.
	private OutputLayer outputLayer; //An object of the OutputLayer class
	private int numberOfHiddenLayers; //Integer number to store the quantity of layers that are part of the hidden layer
	
	private double[][] trainSet; //Matrix to store the training set of input data
	private double[][] validationSet; //Matrix to store the validation set of input data
	private double[] realOutputSet; //Vector to store the training set of output data
	
	/*
	 * Matrix to store the training set of the output
		data (matrix format)
	 */
	private double[][] realMatrixOutputSet;
	private int maxEpochs;	//Integer number to store the maximum number of epochs that neural net will train
	private double learningRate; //Real number to store the learning rate
	private double targetError; //Real number to store the target error
	private double trainingError; //Real number to store the training error
	
	/*
	 * Real number to store the mean of the error
		between two or more neurons
	 */
	private double errorMean;
	
	private ArrayList<Double> listOfMSE = new ArrayList<Double>();	//ArrayList of real numbers to store the MSE error of each epoch
	private ActivationFncENUM activationFnc; //Enum value of the activation function that will be used in training
	
	/*
	 * Enum value of the activation function that
		will be used in the output layer of the net
	 */
	private ActivationFncENUM activationFncOutputLayer;
	private TrainingTypesENUM trainType;	//Enum value of the training type that will be used to train the neural net
	
	/*
	 * Initializes the neural net as a whole. Layers are
	  built, and each list of the weights of neurons is
	  built randomly
	 */
	public NeuralNet initNet(int numberOfInputNeurons, 
			int numberOfHiddenLayers,
			int numberOfNeuronsInHiddenLayer,
			int numberOfOutputNeurons){
		inputLayer = new InputLayer();
		inputLayer.setNumberOfNeuronsInLayer( numberOfInputNeurons );
		
		listOfHiddenLayer = new ArrayList<HiddenLayer>();
		for (int i = 0; i < numberOfHiddenLayers; i++) {
			hiddenLayer = new HiddenLayer();
			hiddenLayer.setNumberOfNeuronsInLayer( numberOfNeuronsInHiddenLayer );
			listOfHiddenLayer.add( hiddenLayer );
		}
		
		outputLayer = new OutputLayer();
		outputLayer.setNumberOfNeuronsInLayer( numberOfOutputNeurons );
		
		inputLayer = inputLayer.initLayer(inputLayer);
		
		if(numberOfHiddenLayers > 0) {
			listOfHiddenLayer = hiddenLayer.initLayer(hiddenLayer, listOfHiddenLayer, inputLayer, outputLayer);
		}

		outputLayer = outputLayer.initLayer(outputLayer);
		
		NeuralNet newNet = new NeuralNet();
		newNet.setInputLayer(inputLayer);
		newNet.setHiddenLayer(hiddenLayer);
		newNet.setListOfHiddenLayer(listOfHiddenLayer);
		newNet.setNumberOfHiddenLayers(numberOfHiddenLayers);
		newNet.setOutputLayer(outputLayer);
	
		return newNet;
	}
	
	
	/*
	 * Prints the neural net as a whole. Each input and
       output weight of each layer is shown
	 */
	public void printNet(NeuralNet n){
		inputLayer.printLayer(n.getInputLayer());
		System.out.println();
		if(n.getHiddenLayer() != null){
			hiddenLayer.printLayer(n.getListOfHiddenLayer());
			System.out.println();
		}
		outputLayer.printLayer(n.getOutputLayer());
	}
	
	/*
	 * Trains the neural network
	 */
	public NeuralNet trainNet(NeuralNet n){

		NeuralNet trainedNet = new NeuralNet();
		
		switch (n.trainType) {
		case PERCEPTRON:
			Perceptron t = new Perceptron();
			trainedNet = t.train(n);
			return trainedNet;
		case ADALINE:
			Adaline a = new Adaline();
			trainedNet = a.train(n);
			return trainedNet;
		case BACKPROPAGATION:
			Backpropagation b = new Backpropagation();
			trainedNet = b.train(n);
			return trainedNet;
		case KOHONEN:
			Kohonen k = new Kohonen();
			trainedNet = k.train(n);
			return trainedNet;
		default:
			throw new IllegalArgumentException(n.trainType+" does not exist in TrainingTypesENUM");
		}
		
	}
	
	/*
	 * Prints the trained neural net and shows its
		results
	 */
	public void printTrainedNetResult(NeuralNet n) {
		switch (n.trainType) {
		case PERCEPTRON:
			Perceptron t = new Perceptron();
			t.printTrainedNetResult( n );
			break;
		case ADALINE:
			Adaline a = new Adaline();
			a.printTrainedNetResult( n );
			break;
		case BACKPROPAGATION:
			Backpropagation b = new Backpropagation();
			b.printTrainedNetResult( n );
			break;
		default:
			throw new IllegalArgumentException(n.trainType+" does not exist in TrainingTypesENUM");
		}
	}
	
	public double[][] getNetOutputValues(NeuralNet trainedNet){
		
		int rows = trainedNet.getTrainSet().length;
		
		int cols = trainedNet.getOutputLayer().getNumberOfNeuronsInLayer();
		
		double[][] matrixOutputValues = new double[rows][cols];
		
		switch (trainedNet.trainType) {
			case BACKPROPAGATION:
				Backpropagation b = new Backpropagation();
				
				for (int rows_i = 0; rows_i < rows; rows_i++) {
					for (int cols_i = 0; cols_i < cols; cols_i++) {
						
						matrixOutputValues[rows_i][cols_i] = b.forward(trainedNet, rows_i).getOutputLayer().getListOfNeurons().get(cols_i).getOutputValue();
						
					}
				}
				
				break;
			default:
				throw new IllegalArgumentException(trainedNet.trainType+" does not exist in TrainingTypesENUM");
		}
		
		return matrixOutputValues;
				
	}
	
	public void netValidation(NeuralNet n) {
		
		switch (n.trainType) {
		case KOHONEN:
			Kohonen k = new Kohonen();
			k.netValidation( n );
			break;
		default:
			throw new IllegalArgumentException(n.trainType+" does not exist in TrainingTypesENUM");
		}
		
	}
	
	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	public HiddenLayer getHiddenLayer() {
		return hiddenLayer;
	}

	public void setHiddenLayer(HiddenLayer hiddenLayer) {
		this.hiddenLayer = hiddenLayer;
	}

	public ArrayList<HiddenLayer> getListOfHiddenLayer() {
		return listOfHiddenLayer;
	}

	public void setListOfHiddenLayer(ArrayList<HiddenLayer> listOfHiddenLayer) {
		this.listOfHiddenLayer = listOfHiddenLayer;
	}

	public OutputLayer getOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(OutputLayer outputLayer) {
		this.outputLayer = outputLayer;
	}

	public int getNumberOfHiddenLayers() {
		return numberOfHiddenLayers;
	}

	public void setNumberOfHiddenLayers(int numberOfHiddenLayers) {
		this.numberOfHiddenLayers = numberOfHiddenLayers;
	}

	/*
	 *Returns the matrix of the training set of input
		data
	 */
	public double[][] getTrainSet() {
		return trainSet;
	}

	/*
	 * Sets the matrix of the training set of input data
	 */
	public void setTrainSet(double[][] trainSet) {
		this.trainSet = trainSet;
	}

	/*
	 * Returns the vector training set of output data
	 */
	public double[] getRealOutputSet() {
		return realOutputSet;
	}

	/*
	 * Sets the vector training set of output data
	 */
	public void setRealOutputSet(double[] realOutputSet) {
		this.realOutputSet = realOutputSet;
	}

	/*
	 * Returns the maximum number of epochs that
		the neural net will train
	 */
	public int getMaxEpochs() {
		return maxEpochs;
	}

	/*
	 * Sets the maximum number of epochs that the
		neural net will train
	 */
	public void setMaxEpochs(int maxEpochs) {
		this.maxEpochs = maxEpochs;
	}

	/*
	 * Returns the target error
	 */
	public double getTargetError() {
		return targetError;
	}

	/*
	 * Sets the target error
	 */
	public void setTargetError(double targetError) {
		this.targetError = targetError;
	}

	/*
	 * Returns the learning rate used in training
	 */
	public double getLearningRate() {
		return learningRate;
	}

	/*
	 * Sets the learning rate used in training
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/*
	 * Returns the training error
	 */
	public double getTrainingError() {
		return trainingError;
	}

	/*
	 * Sets the training error
	 */
	public void setTrainingError(double trainingError) {
		this.trainingError = trainingError;
	}

	/*Returns the enum value of the activation
		function that will be used in training
	*/
	public ActivationFncENUM getActivationFnc() {
		return activationFnc;
	}

	/*
	 * Sets the enum value of the activation function
		that will be used in training
	 */
	public void setActivationFnc(ActivationFncENUM activationFnc) {
		this.activationFnc = activationFnc;
	}

	/*
	 * Returns the enum value of the training type
		that will be used to train the neural net
	 */
	public TrainingTypesENUM getTrainType() {
		return trainType;
	}

	/*
	 * Sets the enum value of the training type that
		will be used to train the neural net
	 */
	public void setTrainType(TrainingTypesENUM trainType) {
		this.trainType = trainType;
	}

	/*
	 * Returns the list of real numbers that stores the
		MSE error of each epoch
	 */
	public ArrayList<Double> getListOfMSE() {
		return listOfMSE;
	}

	/*
	 * Sets the list of real numbers that stores the
		MSE error of each epoch
	 */
	public void setListOfMSE(ArrayList<Double> listOfMSE) {
		this.listOfMSE = listOfMSE;
	}

	
	public double[][] getRealMatrixOutputSet() {
		return realMatrixOutputSet;
	}

	/*
	 * 
	 */
	public void setRealMatrixOutputSet(double[][] realMatrixOutputSet) {
		this.realMatrixOutputSet = realMatrixOutputSet;
	}

	public double getErrorMean() {
		return errorMean;
	}

	public void setErrorMean(double errorMean) {
		this.errorMean = errorMean;
	}

	public ActivationFncENUM getActivationFncOutputLayer() {
		return activationFncOutputLayer;
	}

	public void setActivationFncOutputLayer(
			ActivationFncENUM activationFncOutputLayer) {
		this.activationFncOutputLayer = activationFncOutputLayer;
	}

	public double[][] getValidationSet() {
		return validationSet;
	}

	public void setValidationSet(double[][] validationSet) {
		this.validationSet = validationSet;
	}

	
	
}
