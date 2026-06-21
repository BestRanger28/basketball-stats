import java.util.ArrayList;
public class Network {
    public int numInputs;
    public ArrayList<Node[]> hiddenLayer;
    public Node[] outputNodes;
    public Node[] inputNodes;
    public double learningRate;
    public Network(int numInputs, int[] numHidden, int numOutputs, double learningRate) {
        this.numInputs = numInputs;
        this.learningRate = learningRate;
        hiddenLayer = new ArrayList<Node[]>();
        inputNodes = new Node[numInputs];
        for(int i = 0; i < numInputs; i++) {
            inputNodes[i] = new Node(0);
        }
        for(int i = 0; i < numHidden.length; i++) {
            hiddenLayer.add(new Node[numHidden[i]]);
            for(int j = 0; j < numHidden[i]; j++) {
                hiddenLayer.get(i)[j] = new Node(i == 0 ? numInputs : numHidden[i - 1], i == 0 ? inputNodes : hiddenLayer.get(i - 1),false);
            }
        }
        outputNodes = new Node[numOutputs];
        for(int i = 0; i < numOutputs; i++) {
            outputNodes[i] = new Node(numHidden[numHidden.length - 1], hiddenLayer.get(hiddenLayer.size() - 1),true);
        }
    }
    public double[] GetLayerOutputs(Node[] layer) {
        double[] outputs = new double[layer.length];
        for(int i = 0; i < layer.length; i++) {
            outputs[i] = layer[i].Activate();
        }
        return outputs;
    }
    public double[] GetOutputs(double[] inputValues) {
        SetInputValues(inputValues);
        for(int i = 0; i < hiddenLayer.size(); i++) {
            GetLayerOutputs(hiddenLayer.get(i));
        }
        return GetLayerOutputs(outputNodes);
    }
    void SetInputValues(double[] inputValues) {
        for(int i = 0; i < numInputs; i++) {
            inputNodes[i].sigOutput = inputValues[i];
        }
    }
    public void UpdateWeights(double[] expected) {
    double[][] hiddenErrors = new double[hiddenLayer.size()][];
    double[] outputErrors = new double[outputNodes.length];

    // Output layer errors
    for (int i = 0; i < outputNodes.length; i++) {
        outputErrors[i] = expected[i] - outputNodes[i].sigOutput;
    }

    // Hidden layer errors, working backwards
    double[] nextErrors = outputErrors;

    for (int i = hiddenLayer.size() - 1; i >= 0; i--) {
        Node[] layer = hiddenLayer.get(i);
        hiddenErrors[i] = new double[layer.length];

        for (int j = 0; j < layer.length; j++) {
            double error = 0;

            if (i == hiddenLayer.size() - 1) {
                for (int k = 0; k < outputNodes.length; k++) {
                    error += outputErrors[k] * outputNodes[k].weights[j];
                }
            } else {
                Node[] nextLayer = hiddenLayer.get(i + 1);

                for (int k = 0; k < nextLayer.length; k++) {
                    error += nextErrors[k] * nextLayer[k].weights[j];
                }
            }

            hiddenErrors[i][j] = error;
        }

        nextErrors = hiddenErrors[i];
    }

    // Update hidden layer weights
    for (int i = 0; i < hiddenLayer.size(); i++) {
        Node[] layer = hiddenLayer.get(i);

        for (int j = 0; j < layer.length; j++) {
            layer[j].UpdateWeights(learningRate, hiddenErrors[i][j]);
        }
    }

    // Update output layer weights
    for (int i = 0; i < outputNodes.length; i++) {
        outputNodes[i].UpdateWeights(learningRate, outputErrors[i]);
    }
}
}
