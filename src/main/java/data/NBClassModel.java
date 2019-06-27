package data;

import java.util.HashMap;
import java.util.Map;

/**
 * enthält ein Naive Bayes Modell für ein bestimmtes Label
 * @author Johanna Binnewitt
 *
 */
public class NBClassModel {
	
	//Dokumentenfrequenz von Merkmalen in der "inClass"
	private Map<String, Integer> inClassDFs = new HashMap<String, Integer>();
	//Dokumentenfrequenz von Merkmalen in der "notInClass"
	private Map<String, Integer> notInClassDFs = new HashMap<String, Integer>();
	
	private int membersInClass = 0;
	private int membersNotInClass = 0;
	
	public NBClassModel(Map<String, Integer> inClassDF, Map<String, Integer> notInClassDF,
			int membersInClass, int membersNotInClass) {
		this.inClassDFs = inClassDF;
		this.notInClassDFs = notInClassDF;
		this.membersInClass = membersInClass;
		this.membersNotInClass = membersNotInClass;
	}

	public Map<String, Integer> getInClassDFs() {
		return inClassDFs;
	}

	public Map<String, Integer> getNotInClassDFs() {
		return notInClassDFs;
	}

	public int getMembersInClass() {
		return membersInClass;
	}

	public int getMembersNotInClass() {
		return membersNotInClass;
	}

}
