package com.qa.ims.persistence.domain;

public class Item {
	
	private Long id;
	private String name;
	private float value;
	private int categoryId; // TODO make sure we are using the right data types for the job?
	private int quantity;
	
	public Item(String name, float value, int categoryId, int quantity) {
		this.name = name;
		this.value = value;
		this.categoryId = categoryId;
		this.quantity = quantity;
	}
	
	public Item(String name, float value, int categoryId) {
		// new item with initial quantity of 0
		this(name, value, categoryId, 0);
	}
	
	public Item(Long id, String name, float value, int categoryId, int quantity) {
		this(name, value, categoryId, quantity);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "id: " + id + ", name: " + name + ", value: " + value + ", category id: " + categoryId + ", quantity: " + quantity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (value != other.value) 
			return false;
		if (categoryId != other.categoryId)
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}
	
}
