package com.ways.framework.model;

import java.io.Serializable;

import javax.persistence.Transient;


/**
 * Base class for Model objects. Child objects should implement toString(),
 * equals() and hashCode().
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseBean implements Serializable {    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1507654004868736766L;

	private int rowCount;
	
	private boolean leaf;
	
	private boolean expanded;
	
	@Transient
    public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	@Transient
	public boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
