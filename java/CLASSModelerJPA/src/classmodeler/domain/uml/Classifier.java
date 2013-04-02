package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:21 p.m.
 */
public class Classifier extends Namespace implements IType, IRedefinableElement {

	private boolean abstract;
	private boolean finalSpecification;
	private List<Property> attributes;
	private List<IFeature> features;
	private List<Classifier> generals;
	private List<Generalization> generalizations;
	private List<INamedElement> inheritMember;
	private List<Classifier> redefinedClassifiers;
	public Generalization m_Generalization;

	public Classifier(){

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

	public Package getOwningPackage(){
		return null;
	}

	/**
	 * 
	 * @param type
	 */
	public boolean conformsTo(IType type){
		return false;
	}
}//end Classifier