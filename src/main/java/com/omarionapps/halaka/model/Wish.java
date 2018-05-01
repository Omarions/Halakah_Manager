package com.omarionapps.halaka.model;

/**
 * Created by Omar on 23-Sep-17.
 */
public class Wish extends StudentTrack {
	private boolean      selected;
	private int          activityId;
	private House        house;
	private StudentTrack track; 

	public Wish() {
	}

	public Wish(boolean selected, int activityId) {
		this.selected = selected;
		this.activityId = activityId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public StudentTrack getTrack() {
		return track;
	}

	public void setTrack(StudentTrack track) {
		this.track = track;
	}

	@Override
	public String toString() {

		return "Wish{" +
				"selected=" + selected +
				", activityId=" + activityId +
				", houseId= " + ((house == null) ? "" : house.getId()) +
				", track=" + track +
				'}';
	}
}
