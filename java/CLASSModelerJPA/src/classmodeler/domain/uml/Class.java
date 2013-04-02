package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:06 p.m.
 */
public class Class extends Classifier {

	private List<Classifier> nestedClassifiers;
	private List<Property> ownedAttributes;
	private List<Operation> ownedOperations;
	private List<Class> superClasses;

	public Class(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Class