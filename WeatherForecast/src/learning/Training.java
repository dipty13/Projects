package learning;

import java.util.ArrayList;

import layers.InputLayer; 
import layers.NeuralNet; 
import layers.Neuron; 

public abstract class Training {

	private int epochs; //Integer number to store the training cycle, known as epoch
	private double error; //Real number to store the error between estimated output and real output
	private double mse; //Real number to store the mean square error(MSE)

	/*
	 * Enumeration to store types of training
		supported by project 
	 */
	public enum TrainingTypesENUM {
		PERCEPTRON, ADALINE, BACKPROPAGATION, KOHONEN;
	}

	/*
	 * Trains the neural network
	 */
	public NeuralNet train(NeuralNet n) {
		
		ArrayList<Double> inputWeightIn = new ArrayList<Double>();

		int rows = n.getTrainSet().length;
		int cols = n.getTrainSet()[0].length;

		while (this.getEpochs() < n.getMaxEpochs()) {

			double estimatedOutput = 0.0;
			double realOutput = 0.0;

			for (int i = 0; i < rows; i++) {

				double netValue = 0.0;

				for (int j = 0; j < cols; j++) {
					inputWeightIn = n.getInputLayer().getListOfNeurons().get(j).getListOfWeightIn();
					double inputWeight = inputWeightIn.get(0);
					netValue = netValue + inputWeight * n.getTrainSet()[i][j];
				}

				estimatedOutput = this.activationFnc(n.getActivationFnc(), netValue);
				realOutput = n.getRealOutputSet()[i];

				this.setError(realOutput - estimatedOutput);

				// System.out.println("Epoch: "+this.getEpochs()+" / Error: " + this.getError());

				if (Math.abs(this.getError()) > n.getTargetError()) {
					// fix weights
					InputLayer inputLayer = new InputLayer();
					inputLayer.setListOfNeurons(this.teachNeuronsOfLayer(cols, i, n, netValue));
					n.setInputLayer(inputLayer);
				}

			}

			this.setMse(Math.pow(realOutput - estimatedOutput, 2.0));
			n.getListOfMSE().add(this.getMse());

			this.setEpochs(this.getEpochs() + 1);

		}

		n.setTrainingError(this.getError());

		return n;
	}
	
	/*
	 * Teaches neurons of the layer, calculating and
		changing its weights and returns ArrayList of objects by the Neuron class
	 */
	private ArrayList<Neuron> teachNeuronsOfLayer(int numberOfInputNeurons,
			int line, NeuralNet n, double netValue) {
		ArrayList<Neuron> listOfNeurons = new ArrayList<Neuron>();
		ArrayList<Double> inputWeightsInNew = new ArrayList<Double>();
		ArrayList<Double> inputWeightsInOld = new ArrayList<Double>();

		for (int j = 0; j < numberOfInputNeurons; j++) {
			inputWeightsInOld = n.getInputLayer().getListOfNeurons().get(j)
					.getListOfWeightIn();
			double inputWeightOld = inputWeightsInOld.get(0);

			inputWeightsInNew.add( this.calcNewWeight(n.getTrainType(),
					inputWeightOld, n, this.getError(),
					n.getTrainSet()[line][j], netValue) );

			Neuron neuron = new Neuron();
			neuron.setListOfWeightIn(inputWeightsInNew);
			listOfNeurons.add(neuron);
			inputWeightsInNew = new ArrayList<Double>();
		}

		return listOfNeurons;

	}

	/*
	 * Calculates the new weight of a neuron
	 */
	private double calcNewWeight(TrainingTypesENUM trainType,
			double inputWeightOld, NeuralNet n, double error,
			double trainSample, double netValue) {
		switch (trainType) {
		case PERCEPTRON:
			return inputWeightOld + n.getLearningRate() * error * trainSample;
		case ADALINE:
			return inputWeightOld + n.getLearningRate() * error * trainSample
					* derivativeActivationFnc(n.getActivationFnc(), netValue);
		default:
			throw new IllegalArgumentException(trainType
					+ " does not exist in TrainingTypesENUM");
		}
	}

	/*
	 * Enumeration to store types of activation
functions supported by project
	 */
	public enum ActivationFncENUM {
		STEP, LINEAR, SIGLOG, HYPERTAN;
	}

	/*
	 * Decides which activation function to use and calls the method of computing it
	 * and returns Calculated value of the activation function
	 */
	protected double activationFnc(ActivationFncENUM fnc, double value) {
		switch (fnc) {
		case STEP:
			return fncStep(value);
		case LINEAR:
			return fncLinear(value);
		case SIGLOG:
			return fncSigLog(value);
		case HYPERTAN:
			return fncHyperTan(value);
		default:
			throw new IllegalArgumentException(fnc
					+ " does not exist in ActivationFncENUM");
		}
	}

	/*
	 * Decides which activation function to use and
		calls the method of computing the derivative value
	 */
	public double derivativeActivationFnc(ActivationFncENUM fnc, double value) {
		switch (fnc) {
		case LINEAR:
			return derivativeFncLinear(value);
		case SIGLOG:
			return derivativeFncSigLog(value);
		case HYPERTAN:
			return derivativeFncHyperTan(value);
		default:
			throw new IllegalArgumentException(fnc
					+ " does not exist in ActivationFncENUM");
		}
	}
	
	/*
	 * Computes step function
	 */
	private double fncStep(double v) {
		if (v >= 0) {
			return 1.0;
		} else {
			return 0.0;
		}
	}
	
	/*
	 * Computes linear function
	 */
	private double fncLinear(double v) {
		return v;
	}
	
	
	/*
	 * Computes sigmoid logistics function
	 */
	private double fncSigLog(double v) {
		return (1.0 / (1.0 + Math.exp(-v)));
	}
	
	/*
	 * Computes hyperbolic tangent function
	 */
	private double fncHyperTan(double v) {
		return Math.tanh(v);
	}

	/*
	 * Computes the derivative of the linear function
	 */
	private double derivativeFncLinear(double v) {
		return 1.0;
	}
	
	/*
	 * Computes the derivative of the sigmoid
		logistics function
	 */
	private double derivativeFncSigLog(double v) {
		return (v * (1.0 - v));
	}
	
	/*
	 * Computes the derivative of the hyperbolic
		tangent function
	 */
	private double derivativeFncHyperTan(double v) {
		return (1.0 / Math.pow(Math.cosh(v), 2.0));
	}

	/*
	 * Prints trained neural net and shows its results
	 */
	public void printTrainedNetResult(NeuralNet trainedNet) {

		int rows = trainedNet.getTrainSet().length;
		int cols = trainedNet.getTrainSet()[0].length;

		ArrayList<Double> inputWeightIn = new ArrayList<Double>();

		for (int i = 0; i < rows; i++) {
			double netValue = 0.0;
			for (int j = 0; j < cols; j++) {
				inputWeightIn = trainedNet.getInputLayer().getListOfNeurons().get(j).getListOfWeightIn();
				double inputWeight = inputWeightIn.get(0);
				netValue = netValue + inputWeight * trainedNet.getTrainSet()[i][j];

				System.out.print(trainedNet.getTrainSet()[i][j] + "\t");
			}

			double estimatedOutput = this.activationFnc( trainedNet.getActivationFnc(), netValue );
			
			int colsOutput = trainedNet.getRealMatrixOutputSet()[0].length;
			
			double realOutput = 0.0;
			for (int k = 0; k < colsOutput; k++) {
				realOutput = realOutput + trainedNet.getRealMatrixOutputSet()[i][k];
			}

			System.out.print(" NET OUTPUT: "  + estimatedOutput + "\t");
			System.out.print(" REAL OUTPUT: " + realOutput + "\t");
			double error = estimatedOutput - realOutput;
			System.out.print(" ERROR: " + error + "\n");

		}

	}

	/*
	 * Returns the number of epochs of the training
	 */
	public int getEpochs() {
		return epochs;
	}

	/*
	 * Sets the number of epochs of the training
	 */
	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}
	
	/*
	 * Returns the training error (comparison
		between estimated and real values)
	 */
	public double getError() {
		return error;
	}

	/*
	 * Sets the training error
	 */
	public void setError(double error) {
		this.error = error;
	}

	/*
	 * Returns the MSE
	 */
	public double getMse() {
		return mse;
	}

	/*
	 * Sets the MSE
	 */
	public void setMse(double mse) {
		this.mse = mse;
	}

}
