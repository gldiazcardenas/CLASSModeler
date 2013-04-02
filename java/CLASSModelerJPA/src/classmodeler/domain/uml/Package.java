package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class Package extends Namespace implements IPackagableElement {

	private EVisibilityKind visibility;
	private String URI;
	private List<PackageMerge> packagesMerged;
	private List<IPackageElement> packagedElement;
	private List<IType> ownedTypes;
	private List<Package> nestedPackages;

	public Package(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public void mustBeOwned(){

	}

	public void visibleMembers(){

	}

	public void makesVisible(){

	}

	public IElement getOwner(){
		return null;
	}

	public List<Namespace> getAllNamespaces(){
		return null;
	}

	public String getSeparator(){
		return "";
	}

	/**
	 * 
	 * @param namedElement
	 * @param namespace
	 */
	public boolean isDistinguishableFrom(INamedElement namedElement, Namespace namespace){
		return false;
	}

	public EVisibilityKind getVisibility(){
		return null;
	}

	public List<Comment> getOwnedComments(){
		return null;
	}

	public String getName(){
		return "";
	}

	public EVisibilityKind getVisibility(){
		return null;
	}

	public String getQualifiedName(){
		return "";
	}
}//end Package