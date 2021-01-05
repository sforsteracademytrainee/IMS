package com.qa.ims.persistence.domain;

public class Item {
	
	private Long id;
	private String name;
	private float value;
	private Long categoryId;
	private Long quantity;
	
	public Item(String name, float value, Long categoryId, Long quantity) {
		this.name = name;
		this.value = value;
		this.categoryId = categoryId;
		this.quantity = quantity;
	}
	
	public Item(String name, float value, Long categoryId) {
		// new item with initial quantity of 0
		this(name, value, categoryId, 0L);
	}
	
	public Item(Long id, String name, float value, Long categoryId, Long quantity) {
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "id: " + id + ", name: " + name + ", value: £" + value + ", category id: " + categoryId + ", quantity: " + quantity;
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
