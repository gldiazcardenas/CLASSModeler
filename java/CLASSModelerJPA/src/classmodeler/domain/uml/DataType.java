package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class DataType extends Classifier {

	private List<Property> ownedAttribute;
	private List<Operation> ownedOperation;

	public DataType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end DataType