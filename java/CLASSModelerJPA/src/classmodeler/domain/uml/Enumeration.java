package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:23 p.m.
 */
public class Enumeration extends DataType {

	private List<EnumerationLiteral> ownedLiterals;

	public Enumeration(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Enumeration