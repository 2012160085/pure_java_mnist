
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public class Matrix {
	public double[][] value;
	public int len0;
	public int len1;

	public Matrix(int len0, int len1) {
		this.len0 = len0;
		this.len1 = len1;
		value = new double[len0][len1];
	}

	public Matrix(double[][] d) {
		this.len0 = d.length;
		this.len1 = d[0].length;
		value = arrayClone(d);	
	}

	public Matrix(Matrix m) {
		this.len0 = m.len0;
		this.len1 = m.len1;
		value = arrayClone(m.value);
	}

	public static Matrix one(int len0, int len1) {
		Matrix result = new Matrix(len0, len1);
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				result.value[i][j] = 1;

			}
		}
		return result;
	}

	public Matrix clone() {
		return new Matrix(value);
	}

	public Matrix add(Matrix m) throws Exception {
		if(this.len0 != m.len0 || this.len1 != m.len1) {
			throw new Exception("Unable to calculate");
		}
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] += m.value[i][j];
			}
		}
		return this;
	}

	public Matrix add(Vector m) throws Exception {
		if(this.len1 != m.len) {
			throw new Exception("Unable to calculate");
		}
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] += m.value[j];
			}
		}
		return this;
	}

	public Matrix add(double m) {
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] += m;
			}
		}
		return this;
	}

	public Matrix sub(Matrix m) throws Exception {
		if(this.len0 != m.len0 || this.len1 != m.len1) {
			throw new Exception("Unable to calculate");
		}
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				this.value[i][j] -= m.value[i][j];
			}
		}
		return this;
	}

	public Matrix sub(Vector m) throws Exception {
		if(this.len1 != m.len ) {
			throw new Exception("Unable to calculate");
		}
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] -= m.value[i];
			}
		}
		return this;
	}

	public Matrix sub(double m) {
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] -= m;
			}
		}
		return this;
	}

	public Matrix muliply(Matrix m) throws Exception {
		if(this.len0 != m.len0 || this.len1 != m.len1) {
			throw new Exception("Unable to calculate : [" +  len0 + "," + len1 +"]*[" + m.len0 + "," + m.len1 + "]");
		}
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] *= m.value[i][j];
			}
		}
		return this;
	}

	public Matrix muliply(double m) {
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] *= m;
			}
		}
		return this;
	}

	public Matrix product(Matrix m) throws Exception {
		if(this.len1 != m.len0) {
			throw new Exception("Unable to calculate : [" +  len0 + "," + len1 +"]*[" + m.len0 + "," + m.len1 + "]");
		}
		int lenF = len1;
		len1 = m.len1;
		double[][] newValue = new double[len0][len1];
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				for (int k = 0; k < lenF; k++) {
					newValue[i][j] += value[i][k] * m.value[k][j];
				}
			}
		}
		value = newValue;
		return this;
	}

	public Matrix sigmoid() {
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] = 1 / (1 + Math.exp(-value[i][j]));
			}
		}
		return this;
	}

	public Matrix softmax() {
		for (int i = 0; i < len0; i++) {
			double iSum = 0;
			for (int j = 0; j < len1; j++) {
				iSum += Math.exp(value[i][j]);
			}
			for (int j = 0; j < len1; j++) {
				value[i][j] = Math.exp(value[i][j]) / iSum;
			}
		}
		return this;
	}

	public Vector rowSum() {
		double[] result = new double[len0];
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				result[i] += value[i][j];
			}
		}
		return new Vector(result);
	}

	public Vector colSum() {
		double[] result = new double[len1];
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				result[j] += value[i][j];
			}
		}
		return new Vector(result);
	}

	public Matrix(Vector row, Vector col) {
		len0 = row.len;
		len1 = col.len;
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] += row.value[i] * col.value[j];
			}
		}
	}

	public Matrix transpose() {
		double[][] newValue = new double[len1][len0];
		for (int i = 0; i < len1; i++) {
			for (int j = 0; j < len0; j++) {
				newValue[i][j] = value[j][i];
			}
		}
		value = newValue;
		len0 = newValue.length;
		len1 = newValue[0].length;
		return this;
	}

	public void setRandomValue(double min, double max) {
		Random rd = new Random();
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				value[i][j] = rd.nextDouble() * (max - min) + min;
			}
		}
	}

	@Override
	public String toString() {
		DecimalFormat form = new DecimalFormat("#0.00000");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len0; i++) {
			for (int j = 0; j < len1; j++) {
				sb.append("[");
				if (value[i][j] >= 0) {
					sb.append("+" + form.format(value[i][j]).substring(0, 4));
				} else {
					sb.append(form.format(value[i][j]).substring(0, 5));
				}
				sb.append("]");
			}
			sb.append("\n");
		}
		return sb.toString();

	}
	private double[][] arrayClone(double[][] d){
		double[][] n = new double[d.length][d[0].length];
		for(int i = 0; i < len0; i++) {
			for(int j = 0; j < len1; j++) {
				n[i][j] = d[i][j];
			}
		}
		return n;
	}
}
