package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:27 p.m.
 */
public class ValueSpecification extends NamedElement implements IPackagableElement, ITypedElement {

	private MultiplicityElement owningUpper;
	private MultiplicityElement owningLower;

	public ValueSpecification(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public boolean isComputable(){
		return false;
	}

	public int integerValue(){
		return 0;
	}

	public double realValue(){
		return 0;
	}

	public boolean booleanValue(){
		return false;
	}

	public String stringValue(){
		return "";
	}

	public boolean isNull(){
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

	public EVisibilityKind getVisibility(){
		return null;
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
}//end ValueSpecification