package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:26 p.m.
 */
public class StructuralFeature extends MultiplicityElement implements ITypedElement, IFeature {

	private boolean readOnly;

	public StructuralFeature(){

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

	public IType getType(){
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

	public boolean isLeaf(){
		return false;
	}

	/**
	 * 
	 * @param redefinableElement
	 */
	public void isConsistentWith(IRedefinableElement redefinableElement){

	}

	/**
	 * 
	 * @param redefinableElement
	 */
	public void isRedefinitionContextValid(IRedefinableElement redefinableElement){

	}

	public boolean isStatic(){
		return false;
	}

	public List<Classifier> getFeaturedClassifiers(){
		return null;
	}
}//end StructuralFeature