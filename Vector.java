
import java.text.DecimalFormat;
import java.util.Random;

public class Vector {
	public double[] value;
	public int len;
	public Vector(double[] v) {
		value = v;
		len = value.length;
	}
	public Vector(int len) {
		this.len = len;
		value = new double[len];
	}
	public Vector add(Vector v) {
		for(int i = 0; i < value.length;i++) {
			value[i] += v.value[i];
		}
		return this;
	}
	public Vector sub(Vector v) {
		for(int i = 0; i < value.length;i++) {
			value[i] -= v.value[i];
		}
		return this;
	}
	public Vector multiply(Vector v) {
		for(int i = 0; i < value.length;i++) {
			value[i] *= v.value[i];
		}
		return this;
	}
	public Vector multiply(double v) {
		for(int i = 0; i < value.length;i++) {
			value[i] *= v;
		}
		return this;
	}
	public double sum() {
		double result = 0;
		for(int i = 0; i < value.length;i++) {
			result += value[i];
		}
		return result;
	}
	public void setRandomValue(double min, double max) {
		Random rd = new Random();
		for(int i = 0; i < value.length;i++) {
			value[i] = rd.nextDouble() * (max - min) + min;
		}
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		DecimalFormat form = new DecimalFormat("#0.00000");
		for(int i = 0; i < len; i++) {
			sb.append("[");
			if (value[i] >= 0) {
				sb.append("+" + form.format(value[i]).substring(0, 4));
			} else {
				sb.append(form.format(value[i]).substring(0, 5));
			}
			sb.append("]");
		}
		return sb.toString();
	}
}
