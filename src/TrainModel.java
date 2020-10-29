import java.util.Scanner;

public class TrainModel {
	public static void main(String[] args) throws Exception {
		System.out.println("unzipped mnist dataset directory : ");
		Scanner sc = new Scanner(System.in);
		MnistData mnistData = new MnistData(sc.nextLine());
		mnistData.loadData();
		int batchSize = 500;
		Model model = new Model(batchSize, 100, 50);
		model.initiate();
		
		
		int trainIter = mnistData.getDataLength("train") / batchSize;
		int validIter = mnistData.getDataLength("valid") / batchSize;
		for (int gen = 0; gen < 200; gen++) {
			double trainLoss = 0;
			
			for (int i = 0; i < trainIter; i++) {
				Matrix input = mnistData.getImageInput("train", i * batchSize, (i + 1) * batchSize);
				Matrix label = mnistData.getLabelInput("train", i * batchSize, (i + 1) * batchSize);
				model.forwardPass(input, label);
				trainLoss += model.getLoss();
				model.train(0.0005);
			}
			trainLoss = trainLoss / trainIter;
			
			double validLoss = 0;
			double accuracy = 0;
			for (int i = 0; i < mnistData.getDataLength("valid") / batchSize; i++) {
				Matrix input = mnistData.getImageInput("valid", i * batchSize, (i + 1) * batchSize);
				Matrix label = mnistData.getLabelInput("valid", i * batchSize, (i + 1) * batchSize);
				model.forwardPass(input, label);
				accuracy += model.getAccuracy();
				validLoss += model.getLoss();
			}
			validLoss = validLoss / validIter;
			accuracy = accuracy / validIter;
			System.out.printf("gen%d= Accuracy %.2f%c ValidLoss %.4f TrainLoss %.4f \n", gen + 1,accuracy,
					'%', validLoss, trainLoss);
		}
	}
}
