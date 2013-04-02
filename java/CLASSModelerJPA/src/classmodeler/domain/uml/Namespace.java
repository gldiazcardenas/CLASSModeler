package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class Namespace extends NamedElement {

	private List<ElementImport> elementImports;
	private List<PackageImport> packageImports;
	private List<INamedElement> members;
	private List<NamedElement> ownedMembers;
	private List<Constraint> ownedRules;

	public Namespace(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Namespace