package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class Constraint extends NamedElement implements IPackagableElement {

	private List<IElement> constrainedElement;
	private Namespace context;
	private ValueSpecification specification;

	public Constraint(){

	}

	public void finalize() throws Throwable {
		super.finalize();
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
}//end Constraint