package nn;

public class OneLayerNetwork extends NeuralNetwork {

    private int hnodes;

    // Neural Network constructor function
    public OneLayerNetwork(int inputnodes, int hiddennodes, int outputnodes, double learning_rate, Activation Activation, Activation derivative) {
        super(Activation, derivative);
        // Number of nodes in layer (input, hidden, output)
        // This network is limited to 3 layers
        this.inodes = inputnodes;
        this.hnodes = hiddennodes;
        this.onodes = outputnodes;
        // These are the weight matrices
        // wih: weights from input to hidden
        // who: weights from hidden to output
        // weights inside the arrays are w_i_j
        // where link is from node i to node j in the next layer
        // Matrix is rows X columns
        this.wih = new Matrix(this.hnodes, this.inodes);
        this.who = new Matrix(this.onodes, this.hnodes);
        // Start with random values
        this.wih.randomize();
        this.who.randomize();
        this.learnRate = learning_rate;
        // Activation Function
    }


    public void mutate() {
        this.wih = Matrix.map(this.wih, Activation.MUTATE);
        this.who = Matrix.map(this.who, Activation.MUTATE);
    }

    // Train the network with inputs and targets
    public void train(double[] inputs_array, double[] targets_array) {
        // Turn input and target arrays into matrices
        Matrix inputs = Matrix.fromArray(inputs_array);
        Matrix targets = Matrix.fromArray(targets_array);

        // The input to the hidden layer is the weights (wih) multiplied by inputs
        Matrix hidden_inputs = Matrix.dot(this.wih, inputs);
        // The outputs of the hidden layer pass through sigmoid Activation function
        Matrix hidden_outputs = Matrix.map(hidden_inputs, this.Activation);
        // The input to the output layer is the weights (who) multiplied by hidden layer
        Matrix output_inputs = Matrix.dot(this.who, hidden_outputs);

        // The output of the network passes through sigmoid Activation function
        Matrix outputs = Matrix.map(output_inputs, this.Activation);

        // Error is TARGET - OUTPUT
        Matrix output_errors = Matrix.subtract(targets, outputs);

        // Now we are starting back propogation!
        // Transpose hidden <-> output weights
        Matrix whoT = this.who.transpose();
        // Hidden errors is output error multiplied by weights (who)
        Matrix hidden_errors = Matrix.dot(whoT, output_errors);
        // Calculate the gradient, this is much nicer in python!
        Matrix gradient_output = Matrix.map(outputs, this.derivative);
        // Weight by errors and learing rate
        gradient_output.multiply(output_errors);
        gradient_output.multiply(this.learnRate);

        // Gradients for next layer, more back propogation!
        Matrix gradient_hidden = Matrix.map(hidden_outputs, this.derivative);
        // Weight by errors and learning rate
        gradient_hidden.multiply(hidden_errors);
        gradient_hidden.multiply(this.learnRate);

        // Change in weights from HIDDEN --> OUTPUT
        Matrix hidden_outputs_T = hidden_outputs.transpose();
        Matrix deltaW_output = Matrix.dot(gradient_output, hidden_outputs_T);
        this.who.add(deltaW_output);

        // Change in weights from INPUT --> HIDDEN
        Matrix inputs_T = inputs.transpose();
        Matrix deltaW_hidden = Matrix.dot(gradient_hidden, inputs_T);
        this.wih.add(deltaW_hidden);
    }


    // Query the network!
    public double[] query(double[] inputs_array) {

        // Turn input array into a matrix
        Matrix inputs = Matrix.fromArray(inputs_array);
        // The input to the hidden layer is the weights (wih) multiplied by inputs
        Matrix hidden_inputs = Matrix.dot(this.wih, inputs);
        // The outputs of the hidden layer pass through sigmoid Activation function
        Matrix hidden_outputs = Matrix.map(hidden_inputs, this.Activation);
        // The input to the output layer is the weights (who) multiplied by hidden layer
        Matrix output_inputs = Matrix.dot(this.who, hidden_outputs);
        // The output of the network passes through sigmoid Activation function
        Matrix outputs = Matrix.map(output_inputs, this.Activation);
        // Return the result as an array
        return outputs.toArray();
    }

}
