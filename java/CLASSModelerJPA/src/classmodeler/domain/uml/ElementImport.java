package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class ElementImport extends DirectedRelationship {

	private EVisibilityKind visibility;
	private String alias;
	private IPackagableElement importedElement;
	private Namespace importingNamespace;

	public ElementImport(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end ElementImport