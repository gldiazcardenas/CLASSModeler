package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:26 p.m.
 */
public class Property extends StructuralFeature {

	private EAggregationKind aggregationKind;
	private String default;
	private boolean derived;
	private boolean derivedUnion;
	private boolean readOnly;
	private boolean isID;
	private List<Property> redefinedProperties;
	private ValueSpecification defaultValue;

	public Property(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Property