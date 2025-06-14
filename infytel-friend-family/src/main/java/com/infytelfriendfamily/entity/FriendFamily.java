package com.infytelfriendfamily.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friendfamily")
public class FriendFamily {

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(name = "phone_no")
	long phoneNo;

	@Column(name = "friend_and_family")
	long friendAndFamily;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}

	public long getFriendAndFamily() {
		return friendAndFamily;
	}

	public void setFriendAndFamily(long friendAndFamily) {
		this.friendAndFamily = friendAndFamily;
	}

	public FriendFamily() {
		super();
	}

}
