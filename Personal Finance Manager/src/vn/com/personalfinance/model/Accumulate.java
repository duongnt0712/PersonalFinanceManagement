package vn.com.personalfinance.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.cache.StateHistory;

/**
 * Represents an accumulation.
 * 
 * @author Nguyen Hai - Group 2
 * @version 1.0
 */
@DClass(schema="personalfinancemanagement")
public class Accumulate extends Savings {
	public static final String S_remainedAmount = "remainedAmount";
	
	@DAttr(name = S_remainedAmount, type = Type.Double, auto = true, length = 15, mutable = false, optional = true,
			serialisable=false, derivedFrom={S_amount})
	private Double remainedAmount;

	private StateHistory<String, Object> stateHist;
	
	// constructor methods
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	public Accumulate(@AttrRef("amount") Double amount, 
			@AttrRef("name") String name,
			@AttrRef("purpose") String purpose,
			@AttrRef("startDate") Date startDate) {
		this(null, null, amount, name, purpose, startDate);
	}

	// a shared constructor that is invoked by other constructors
	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Accumulate(Integer id, String code, Double amount, String name,
					String purpose, Date startDate) throws ConstraintViolationException {
		super(id, code, amount, name, purpose, startDate);
		
		Collection<ExpenditureSavings> expenditureSavings = getExpenditureSavings();
		setExpenditureSavings(expenditureSavings = new ArrayList<>());
		setExpenditureSavingsCount(0);
		
		stateHist = new StateHistory<>();
		computeRemainedAmount();
	}
	
	//getter
	//devired attribute
	public double getRemainedAmount() {
		return getRemainedAmount(false);
	}
	
	public double getRemainedAmount(boolean cached) throws IllegalStateException {
		if (cached) {
			Object val = stateHist.get(S_remainedAmount);

			if (val == null)
				throw new IllegalStateException("Accumulate.getRemainedAmount: cached value is null");
			return (Double) val;
		} else {
			if (remainedAmount != null)
				return remainedAmount;
			else
				return 0;
		}
	}

	// setter methods
	@Override
	public void setAmount(double amount) {
		setAmount(amount, false);
	}
	
	public void setAmount(double amount, boolean computeRemainedAmount) {
		amount = getAmount();
		if (computeRemainedAmount)
			computeRemainedAmount();
	}
	
	// calculate accumulate
	@DOpt(type=DOpt.Type.DerivedAttributeUpdater)
	@AttrRef(value=S_remainedAmount)
	private void computeRemainedAmount() {
		stateHist.put(S_remainedAmount, remainedAmount);
		
//		if(getExpenditureSavingsCount() > 0 && remainedAmount > getAmount()) { 
		if(getExpenditureSavings().size() > 0 && remainedAmount > 0) { 
			double accumAmount = 0d;
			for(ExpenditureSavings eS : getExpenditureSavings()) {
				accumAmount += eS.getAmount();
			}
			remainedAmount = getAmount() - accumAmount;
		} else if (getExpenditureSavings().size() == 0){
			remainedAmount = getAmount();
		} 
	}
	
	@DOpt(type = DOpt.Type.LinkAdder)
	// only need to do this for reflexive association: @MemberRef(name="enrolments")
	public boolean addExpenditureSavings(ExpenditureSavings e) {
		if (!getExpenditureSavings().contains(e))
			getExpenditureSavings().add(e);
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewExpenditureSavings(ExpenditureSavings e) {
		getExpenditureSavings().add(e);

		int count = getExpenditureSavingsCount();
		setExpenditureSavingsCount(count + 1);

		// v2.6.4.b
		computeRemainedAmount();

		// no other attributes changed (average mark is not serialisable!!!)
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	// @MemberRef(name="enrolments")
	public boolean addExpenditureSavings(Collection<ExpenditureSavings> expSavings) {
		boolean added = false;
		for (ExpenditureSavings e : expSavings) {
			if (!getExpenditureSavings().contains(e)) {
				if (!added)
					added = true;
				getExpenditureSavings().add(e);
			}
		}
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewExpenditureSavings(Collection<ExpenditureSavings> expSavings) {
		getExpenditureSavings().addAll(expSavings);
		int count = getExpenditureSavingsCount();
		count += expSavings.size();
		setExpenditureSavingsCount(count);

		// v2.6.4.b
		computeRemainedAmount();

		// no other attributes changed (average mark is not serialisable!!!)
		return false;
	}

	@DOpt(type = DOpt.Type.LinkRemover)
	// @MemberRef(name="enrolments")
	public boolean removeExpenditureSavings(ExpenditureSavings e) {
		boolean removed = getExpenditureSavings().remove(e);

		if (removed) {
			int count = getExpenditureSavingsCount();
			setExpenditureSavingsCount(count - 1);
			
			double currentAccountBalance = e.getAccount().getBalance();
			e.getAccount().setBalance(currentAccountBalance += e.getAmount());

			// v2.6.4.b
			computeRemainedAmount();
		}
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkUpdater)
	// @MemberRef(name="enrolments")
	public boolean updateExpenditureSavings(ExpenditureSavings e) throws IllegalStateException {
		// recompute using just the affected enrolment
		/*
		 * double totalMark = averageMark * enrolmentCount;
		 * 
		 * int oldFinalMark = e.getFinalMark(true);
		 * 
		 * int diff = e.getFinalMark() - oldFinalMark;
		 * 
		 * // TODO: cache totalMark if needed
		 * 
		 * totalMark += diff;
		 * 
		 * averageMark = totalMark / enrolmentCount;
		 */

		// no other attributes changed
		return true;
	}
	
}
