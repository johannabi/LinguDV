package data;

public class LabelEvalResult {
	
	private int tp, tn, fp, fn;
	private double precision;
	private double recall;
	private double accuracy;
	private double f1;
	
	private String label;
	
	public LabelEvalResult(String label, int tp, int tn, int fp, int fn) {
		this.label = label;
		
		this.tp = tp;
		this.tn = tn;
		this.fp = fp;
		this.fn = fn;
		
		this.precision = tp / (double) (fp + tp);
		this.recall = tp / (double) (fn + tp);
		this.f1 = 2 * precision * recall / (precision + recall);
		this.accuracy = (tp + tn) / (double) (tp + fp + fn + tn);
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getF1() {
		return f1;
	}

	public String getLabel() {
		return label;
	}

	public int getTp() {
		return tp;
	}

	public int getTn() {
		return tn;
	}

	public int getFp() {
		return fp;
	}

	public int getFn() {
		return fn;
	}

}
