package dto;

public class UserDTO {
	private int userId;
	private String firstname;
	private String lastname;
	private String initials;
	private String password;
    private boolean isActive;

    public UserDTO(int userId, String firstname, String lastname, String initials, String password, boolean isActive) {
		this.userId = userId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.initials = initials;
		this.password = password;
        this.isActive = isActive;
    }
	
    public UserDTO(UserDTO user) {
    	this.userId = user.getUserId();
    	this.firstname = user.getFirstname();
    	this.lastname = user.getLastname();
    	this.initials = user.getInitials();
    	this.password = user.getPassword();
        this.isActive = user.isActive();
    }
    
    public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	public String getFirstname() { return firstname; }
	public void setFirstname(String firstname) { this.firstname = firstname; }
	public String getLastname() { return this.lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }
	public String getInitials() { return initials; }
	public void setInitials(String initials) { this.initials = initials; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
    public boolean isActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
    	this.isActive = isActive;
    }


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDTO that = (UserDTO) o;

		if (userId != that.userId) return false;
		if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
		if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
		if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;
		if (password != null ? password.equals(that.password) : that.password == null) return false;
		return isActive != that.isActive;
	}

	public String toString() {
        return userId + "\t" + firstname + "\t" + lastname + "\t" + initials + "\t" + password + "\t" + isActive;
    }
}
