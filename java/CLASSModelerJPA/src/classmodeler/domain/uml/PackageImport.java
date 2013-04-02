package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class PackageImport extends DirectedRelationship {

	private EVisibilityKind visibility;
	private Package importedPackage;
	private Namespace importingNamespace;

	public PackageImport(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end PackageImport