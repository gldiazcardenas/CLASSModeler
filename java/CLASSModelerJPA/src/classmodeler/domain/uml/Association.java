package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:21 p.m.
 */
public class Association extends Classifier implements IRelationship {

	private boolean derived;
	private List<Property> membersEnd;
	private List<Property> ownedEnd;
	private List<Property> navigableOwnedEnd;
	private List<IType> endTypes;

	public Association(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public IElement getOwner(){
		return null;
	}

	public List<Comment> getOwnedComments(){
		return null;
	}

	public List<IElement> getRelatedElements(){
		return null;
	}
}//end Association