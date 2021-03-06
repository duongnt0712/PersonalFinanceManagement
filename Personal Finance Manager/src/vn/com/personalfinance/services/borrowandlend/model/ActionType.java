package vn.com.personalfinance.services.borrowandlend.model;

import java.util.ArrayList;  
import java.util.Collection;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.model.meta.Select;

@DClass (schema = "personalfinancemanager")
public class ActionType {
	public static final String A_name = "name";
	
	@DAttr (name = "id", type = Type.Integer, length = 8, id = true, auto = true, mutable = false, optional = false)
	private int id;
	
	private static int idCounter;
	
	@DAttr (name = A_name, type = Type.String, length = 30, optional = false, cid = true)
	private String name;
	
	@DAttr (name = "borrowAndLend", type = Type.Collection, serialisable = false, optional = false, filter = @Select(clazz = BorrowAndLend.class))
	@DAssoc (ascName = "borrowAndLend-has-actionType", role = "actionType", ascType = AssocType.One2Many, endType = AssocEndType.One, 
			associate = @Associate(type = BorrowAndLend.class, cardMin = 0, cardMax = MetaConstants.CARD_MORE))
	private Collection<BorrowAndLend> borrowAndLend;
	// derived attributes
	private int borrowAndLendCount;
	
//	Constructor method
	@DOpt (type = DOpt.Type.ObjectFormConstructor)
	@DOpt (type = DOpt.Type.RequiredConstructor)
	public ActionType (@AttrRef("name") String name) {
		this(null, name);
	}
	
	@DOpt (type = DOpt.Type.DataSourceConstructor) 
	public ActionType (@AttrRef("id") Integer id, @AttrRef("name") String name) {
		this.id = nextId(id);
		this.name = name;
		borrowAndLend = new ArrayList<>();
		borrowAndLendCount = 0;
	}
	
//	Getter method
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@DOpt(type=DOpt.Type.Getter)
	public Collection<BorrowAndLend> getBorrowAndLend() {
		return borrowAndLend;
	}
	
	@DOpt(type=DOpt.Type.LinkCountGetter)
	public int getBorrowAndLendCount() {
		return borrowAndLendCount;
	}
	
//	Setter method
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DOpt(type=DOpt.Type.Setter)
	public void setBorrowAndLend(Collection<BorrowAndLend> borrowAndLend) {
		this.borrowAndLend = borrowAndLend;
		borrowAndLendCount = borrowAndLend.size();
	}

	@DOpt(type=DOpt.Type.LinkCountSetter)
	public void setBorrowAndLendCount(int borrowAndLendCount) {
		this.borrowAndLendCount = borrowAndLendCount;
	}
	
	
//	Operations for sub-form
	@DOpt (type = DOpt.Type.LinkAdder)
	private boolean addBorrowAndLend (BorrowAndLend a) {
		if (!this.borrowAndLend.contains(a)) {
			this.borrowAndLend.add(a);
		}
		return false;
	}
	
	@DOpt (type = DOpt.Type.LinkAdder)
	private boolean addBorrowAndLend (Collection<BorrowAndLend> borrowAndLend) {
		for (BorrowAndLend a : borrowAndLend) {
			if (!this.borrowAndLend.contains(a)) {
				this.borrowAndLend.add(a);
			}
		}
		return false;
	}
	
	@DOpt(type = DOpt.Type.LinkAdderNew)
	private boolean addNewBorrowAndLend (BorrowAndLend a) {
		borrowAndLend.add(a);
		borrowAndLendCount++;
		return false;
	}
	
	@DOpt(type = DOpt.Type.LinkAdderNew)
	private boolean addNewBorrowAndLend (Collection<BorrowAndLend> borrowAndLend) {
		this.borrowAndLend.addAll(borrowAndLend);
		borrowAndLendCount += borrowAndLend.size();
		return false;
	}
	
	@DOpt (type = DOpt.Type.LinkRemover)
	private boolean removeBorrowAndLend (BorrowAndLend a) {
		boolean removed = borrowAndLend.remove(a);
		if (removed) {
			borrowAndLendCount--;
		}
		return false;
	}
//	End operations for sub-form
	
	private static int nextId(Integer currId) {
		if (currId == null) {
			idCounter++;
			return idCounter;
		} else {
			int num = currId.intValue();
			if (num > idCounter) {
				idCounter = num;
			}
			return currId;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionType other = (ActionType) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActionType [id=" + id + ", name=" + name + "]";
	}
	
	/**
	   * @requires 
	   *  minVal != null /\ maxVal != null
	   * @effects 
	   *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
	   */
	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal) 
			throws ConstraintViolationException {
		if (minVal != null && maxVal != null) {
			// TODO: update this for the correct attribute if there are more than one auto
			// attributes of this class
			if (attrib.name().equals("id")) {
				int maxIdVal = (Integer) maxVal;
				if (maxIdVal > idCounter)
					idCounter = maxIdVal;
			}
		}
	}	
}
