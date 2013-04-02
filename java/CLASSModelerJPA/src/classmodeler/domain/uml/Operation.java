package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class Operation extends BehavioralFeature {

	private boolean ordered;
	private boolean query;
	private boolean unique;
	private int upper;
	private int lower;
	private List<Constraint> preConditions;
	private List<Constraint> bodyConditions;
	private List<Constraint> postCondition;
	private List<Operation> redefinedOperations;
	private Parameter ownedParameters;

	public Operation(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Operation