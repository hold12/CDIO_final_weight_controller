package dto;

public class UserDTO {
	private int userId;
	private String userFirstname;
	private String userLastname;
	private String initials;
	private String password;
    private boolean isActive;

    public UserDTO(int userId, String userFirstname, String userLastname, String initials, String password, boolean isActive) {
		this.userId = userId;
		this.userFirstname = userFirstname;
		this.userLastname = userLastname;
		this.initials = initials;
		this.password = password;
        this.isActive = isActive;
    }
	
    public UserDTO(UserDTO opr) {
    	this.userId = opr.getUserId();
    	this.userFirstname = opr.getUserFirstname();
    	this.userLastname = opr.getUserLastname();
    	this.initials = opr.getInitials();
    	this.password = opr.getPassword();
        this.isActive = opr.isActive();
    }
    
    public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	public String getUserFirstname() { return userFirstname; }
	public void setUserFirstname(String userFirstname) { this.userFirstname = userFirstname; }
	public String getUserLastname() { return this.userLastname; }
	public void setUserLastname(String userLastname) { this.userLastname = userLastname; }
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
		if (userFirstname != null ? !userFirstname.equals(that.userFirstname) : that.userFirstname != null) return false;
		if (userLastname != null ? !userLastname.equals(that.userLastname) : that.userLastname != null) return false;
		if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;
		if (password != null ? password.equals(that.password) : that.password == null) return false;
		return isActive != that.isActive;
	}

	public String toString() {
        return userId + "\t" + userFirstname + "\t" + userLastname + "\t" + initials + "\t" + password + "\t" + isActive;
    }
}
