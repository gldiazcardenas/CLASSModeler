package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:21 p.m.
 */
public class BehavioralFeature extends Namespace implements IFeature {

	private List<IType> raisedExceptions;
	private List<Parameter> ownedParameters;

	public BehavioralFeature(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param namespace
	 * @param namedElement
	 */
	public boolean isDistinguishableFrom(Namespace namespace, INamedElement namedElement){
		return false;
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
}//end BehavioralFeature