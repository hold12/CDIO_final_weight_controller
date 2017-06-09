package dto;

public class UserDTO {
	private int userId;
	private String firstname;
	private String lastname;

    public UserDTO(int userId, String firstname, String lastname) {
		this.userId = userId;
		this.firstname = firstname;
		this.lastname = lastname;
    }
	
    public UserDTO(UserDTO user) {
    	this.userId = user.getUserId();
    	this.firstname = user.getFirstname();
    	this.lastname = user.getLastname();
    }
    
    public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	public String getFirstname() { return firstname; }
	public void setFirstname(String firstname) { this.firstname = firstname; }
	public String getLastname() { return this.lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDTO that = (UserDTO) o;

		if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
		if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
		return userId == that.userId;
	}

	public String toString() {
        return userId + "\t" + firstname + "\t" + lastname;
    }
}
