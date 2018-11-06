package validation;

import layers.NeuralNet;

public interface Validation {
	
	/*
	 * Performs neural network validation,
		printing some results on the PC
		screen
	 */
	public void netValidation(NeuralNet n);
	
}