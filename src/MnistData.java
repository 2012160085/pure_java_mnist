
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class MnistData {
	public MnistData(String dir) {
		gzipFileDir = dir;
	}

	private String gzipFileDir;
	private int[][] trainImageData;
	private int[] trainLabelData;
	private int[][] validImageData;
	private int[] validLabelData;

	private double[][] trainImageInput;
	private double[][] trainLabelInput;
	private double[][] validImageInput;
	private double[][] validLabelInput;

	public void loadData() {
		loadImageData("t10k-images-idx3-ubyte", "valid");
		loadImageData("train-images-idx3-ubyte", "train");
		loadLabelData("t10k-labels-idx1-ubyte", "valid");
		loadLabelData("train-labels-idx1-ubyte", "train");
	}

	// 데이터 파일 읽어오는 메소드
	public void loadImageData(String fileName, String type) {
		byte[] header = new byte[16];
		byte[] buffer = new byte[784];
		try {
			FileInputStream file = new FileInputStream(gzipFileDir + "\\" + fileName);
			file.read(header);
			int bytes_read;
			ArrayList<int[]> images = new ArrayList<>();
			while ((bytes_read = file.read(buffer)) == 784) {
				int[] arr = new int[784];
				for (int i = 0; i < 784; i++)
					arr[i] = (int) (buffer[i] & 0xff);
				images.add(arr);
			}
			if (type == "train") {
				trainImageData = images.toArray(new int[][] {});
				double[][] trainInputValue = new double[trainImageData.length][trainImageData[0].length];
				for (int i = 0; i < trainImageData.length; i++) {
					for (int j = 0; j < trainImageData[0].length; j++) {
						trainInputValue[i][j] = (trainImageData[i][j] - 128.0) / 128.0;
					}
				}
				trainImageInput = trainInputValue;
			}
			if (type == "valid") {
				validImageData = images.toArray(new int[][] {});
				double[][] validInputValue = new double[validImageData.length][validImageData[0].length];
				for (int i = 0; i < validImageData.length; i++) {
					for (int j = 0; j < validImageData[0].length; j++) {
						validInputValue[i][j] = (validImageData[i][j] - 128.0) / 128.0;
					}
				}
				validImageInput = validInputValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadLabelData(String fileName, String type) {
		byte[] header = new byte[8];
		byte[] buffer = new byte[1];
		try {
			FileInputStream file = new FileInputStream(gzipFileDir + "\\" + fileName);
			file.read(header);
			int bytes_read;
			ArrayList<Integer> arr = new ArrayList<>();
			while ((bytes_read = file.read(buffer)) > 0) {
				arr.add((int) (buffer[0] & 0xff));
			}
			if (type == "train") {
				trainLabelData = arr.stream().mapToInt(Integer::intValue).toArray();
				double[][] trainLabelValue = new double[trainImageData.length][10];
				for(int i = 0; i < trainLabelValue.length; i++) {
					for(int j = 0; j < 10; j++) {
						if(trainLabelData[i] == j) {
							trainLabelValue[i][j] = 1;
						}
					}
				}
				trainLabelInput = trainLabelValue;
			}
			if (type == "valid") {
				validLabelData = arr.stream().mapToInt(Integer::intValue).toArray();
				double[][] validLabelValue = new double[validLabelData.length][10];
				for(int i = 0; i < validLabelValue.length; i++) {
					for(int j = 0; j < 10; j++) {
						if(validLabelData[i] == j) {
							validLabelValue[i][j] = 1;
						}
					}
				}
				validLabelInput = validLabelValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ShowImage(int[] image) {
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				if (image[i * 28 + j] < 128) {
					System.out.print("--");
				} else {
					System.out.print("##");
				}
			}
			System.out.println();
		}
	}
	public int getDataLength(String type) {
		if(type == "train") {
			return trainImageData.length;
		}else {
			return validImageData.length;
		}
	}
	public Matrix getImageInput(String type, int idxFrom, int idxTo) {
		if(type == "train") {
			return new Matrix(Arrays.copyOfRange(trainImageInput, idxFrom, idxTo));
		}
		if(type == "valid") {
			return new Matrix(Arrays.copyOfRange(validImageInput, idxFrom, idxTo));
		}
		return null;
	}
	public Matrix getLabelInput(String type, int idxFrom, int idxTo) {
		if(type == "train") {
			return new Matrix(Arrays.copyOfRange(trainLabelInput, idxFrom, idxTo));
		}
		if(type == "valid") {
			return new Matrix(Arrays.copyOfRange(validLabelInput, idxFrom, idxTo));
		}
		return null;
	}
}
