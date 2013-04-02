package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:23 p.m.
 */
public class Expression extends ValueSpecification {

	private String symbol;
	private List<ValueSpecification> operand;

	public Expression(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Expression