package cn.crtlprototypestudios.litc.experimental.neural;

public class SimpleNeuralNetwork {
    private final int inputSize;
    private final int hiddenSize;
    private final int outputSize;
    private final double[][] weightsInputHidden;
    private final double[][] weightsHiddenOutput;
    private final double[] biasHidden;
    private final double[] biasOutput;

    public SimpleNeuralNetwork(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;

        weightsInputHidden = new double[inputSize][hiddenSize];
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        biasHidden = new double[hiddenSize];
        biasOutput = new double[outputSize];

        initializeWeightsAndBiases();
    }

    private void initializeWeightsAndBiases() {
        // Initialize weights and biases with small random values
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] = Math.random() - 0.5;
            }
        }
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHiddenOutput[i][j] = Math.random() - 0.5;
            }
            biasHidden[i] = Math.random() - 0.5;
        }
        for (int i = 0; i < outputSize; i++) {
            biasOutput[i] = Math.random() - 0.5;
        }
    }

    public double[] forward(double[] input) {
        double[] hidden = new double[hiddenSize];
        double[] output = new double[outputSize];

        // Hidden layer
        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;
            for (int j = 0; j < inputSize; j++) {
                sum += input[j] * weightsInputHidden[j][i];
            }
            hidden[i] = activate(sum + biasHidden[i]);
        }

        // Output layer
        for (int i = 0; i < outputSize; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hidden[j] * weightsHiddenOutput[j][i];
            }
            output[i] = activate(sum + biasOutput[i]);
        }

        return output;
    }

    private double activate(double x) {
        // ReLU activation function
        return Math.max(0, x);
    }
}
