import java.util.Random;
public class Node {
    public double output;
    public double sigOutput;
    public Node[] inputs;
    public double[] weights;
    private static final Random Random = new Random();
    public double bias;
    boolean isOutputNode;
    Node(int numInputs, Node[] inputs, boolean isOutputNode) {
        this.inputs = inputs;
        this.isOutputNode = isOutputNode;
        weights = new double[numInputs];
        for(int i = 0; i < numInputs; i++) {
            weights[i] = Random.nextGaussian() * Math.sqrt(2.0 / numInputs);
        }
        bias = Math.random() * 2 - 1;
    }
    Node(int numInputs) {
        inputs = null;
        weights = new double[numInputs];
        for(int i = 0; i < numInputs; i++) {
            weights[i] = Math.random() * 2 - 1;
        }
        bias = Math.random() * 2 - 1;
    }
    public double Activate(){
        output = bias;
        for(int i = 0; i < weights.length; i++) {
            output += weights[i] * inputs[i].sigOutput;
        }
        if(isOutputNode) {
            sigOutput =Sigmoid(output);
        } else {
            sigOutput = ReLU(output);
        }
        return sigOutput;
    }
    double SigmoidDerivative(double sigOutput) {
        return sigOutput * (1 - sigOutput);
    }
    double Sigmoid(double output) {
        return 1 / (1 + Math.exp(-output));
    }
    double ReLU(double output) {
        return Math.max(0, output);
    }

    double ReLUDerivative(double output) {
        return output > 0 ? 1 : 0;
    }
    public void UpdateWeights(double learningRate, double error) {
        if(isOutputNode) {
            error *= SigmoidDerivative(sigOutput);
        } else {
            error *= ReLUDerivative(sigOutput);
        }
        double delta = error ;
        for(int i = 0; i < weights.length; i++) {
            weights[i] += delta * learningRate * inputs[i].sigOutput;
        }
        bias += delta * learningRate;
    }
}
