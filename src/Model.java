
import java.util.Scanner;

public class Model {
	private Matrix weightsInput;
	private Matrix weightsHidden1;
	private Matrix weightsHidden2;

	private Vector biasInput;
	private Vector biasHidden1;
	private Vector biasHidden2;

	private Matrix inputLayer;
	private Matrix hiddenLayer1;
	private Matrix hiddenLayer2;
	private Matrix outputLayer;

	private Matrix inputWeightedSum;
	private Matrix hidden1WeightedSum;
	private Matrix hidden2WeightedSum;
	private Matrix outputWeightedSum;

	private Matrix labelMatrix;
	private Matrix lossMatrix;
	private Matrix diffMatrix;

	public Model(int inputSize, int h1Size, int h2Size) {
		// 레이어 초기화
		inputLayer = new Matrix(inputSize, 784);
		hiddenLayer1 = new Matrix(inputSize, h1Size);
		hiddenLayer2 = new Matrix(inputSize, h2Size);
		outputLayer = new Matrix(inputSize, 10);

		inputWeightedSum = new Matrix(inputSize, 784);
		hidden1WeightedSum = new Matrix(inputSize, h1Size);
		hidden2WeightedSum = new Matrix(inputSize, h2Size);
		outputWeightedSum = new Matrix(inputSize, 10);

		// 가중치 편향 초기화
		weightsInput = new Matrix(784, h1Size);
		weightsHidden1 = new Matrix(h1Size, h2Size);
		weightsHidden2 = new Matrix(h2Size, 10);
		biasInput = new Vector(h1Size);
		biasHidden1 = new Vector(h2Size);
		biasHidden2 = new Vector(10);
	}

	public void initiate() {
		weightsInput.setRandomValue(-0.05, 0.05);
		weightsHidden1.setRandomValue(-0.4, 0.4);
		weightsHidden2.setRandomValue(-1, 1);
		biasInput.setRandomValue(-0.1, 0.1);
		biasHidden1.setRandomValue(-0.15, 0.15);
		biasHidden2.setRandomValue(-0.3, 0.3);
	}

	public void forwardPass(Matrix inputLayer, Matrix labelLayer) throws Exception {
		this.inputLayer = inputLayer.clone();
		this.labelMatrix = labelLayer.clone();

		// 아웃풋 계산
		inputWeightedSum = inputLayer.clone().product(weightsInput).add(biasInput);

		hiddenLayer1 = inputWeightedSum.clone().sigmoid();

		hidden1WeightedSum = hiddenLayer1.clone().product(weightsHidden1).add(biasHidden1);
		hiddenLayer2 = hidden1WeightedSum.clone().sigmoid();

		hidden2WeightedSum = hiddenLayer2.clone().product(weightsHidden2).add(biasHidden2);
		outputLayer = hidden2WeightedSum.clone().sigmoid();
		// loss 계산
		diffMatrix = outputLayer.clone(); // 1/10*(L-O)
		diffMatrix = diffMatrix.sub(labelLayer);

		lossMatrix = diffMatrix.clone().muliply(diffMatrix).muliply(5); // 1/20*(L-O)^2

	}

	public double getLoss() {
		return lossMatrix.colSum().sum();
	}

	public void train(double learningRate) throws Exception {
		Matrix dHiddenLayer2 = hiddenLayer2.clone().muliply(-1).add(1).muliply(hiddenLayer2);
		Matrix dLoss_dH2WS = diffMatrix.clone();// .muliply(dHiddenLayer2);

		Vector delta_biasHidden2 = dLoss_dH2WS.colSum(); //
		Matrix delta_weightsHidden2 = hiddenLayer2.clone().transpose().product(dLoss_dH2WS);

		Matrix dHiddenLayer1 = hiddenLayer1.clone().muliply(-1).add(1).muliply(hiddenLayer1);
		Matrix dLoss_dH1WS = dLoss_dH2WS.clone().product(weightsHidden2.clone().transpose()).muliply(dHiddenLayer2);

		Vector delta_biasHidden1 = dLoss_dH1WS.colSum();
		Matrix delta_weightsHidden1 = hiddenLayer1.clone().transpose().product(dLoss_dH1WS);

		Matrix dInputLayer = inputLayer.clone().muliply(-1).add(1).muliply(inputLayer);
		Matrix dLoss_dIpWS = dLoss_dH1WS.clone().product(weightsHidden1.clone().transpose()).muliply(dHiddenLayer1);

		Vector delta_biasInput = dLoss_dIpWS.colSum();
		Matrix delta_weightsInput = inputLayer.clone().transpose().product(dLoss_dIpWS);

		biasHidden2.sub(delta_biasHidden2.multiply(learningRate));
		weightsHidden2.sub(delta_weightsHidden2.muliply(learningRate));
		biasHidden1.sub(delta_biasHidden1.multiply(learningRate));
		weightsHidden1.sub(delta_weightsHidden1.muliply(learningRate));
		biasInput.sub(delta_biasInput.multiply(learningRate));
		weightsInput.sub(delta_weightsInput.muliply(learningRate));
	}

	public double getAccuracy() throws Exception {
		int correct = 0;
		for (int i = 0; i < inputLayer.len0; i++) {
			int indxOutput = 0;
			double maxOutput = 0;
			int indxLabel = 0;
			double maxLabel = 0;
			for (int j = 0; j < 10; j++) {
				if (maxOutput < outputLayer.value[i][j]) {
					maxOutput = outputLayer.value[i][j];
					indxOutput = j;
				}
				if (maxLabel < labelMatrix.value[i][j]) {
					maxLabel = labelMatrix.value[i][j];
					indxLabel = j;
				}
			}
			if (indxLabel == indxOutput) {
				correct += 1;
			}
		}
		return 100 * (correct + 0.0) / inputLayer.len0;
	}

}
