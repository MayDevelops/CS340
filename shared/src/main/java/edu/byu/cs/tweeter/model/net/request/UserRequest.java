package edu.byu.cs.tweeter.model.net.request;

public class UserRequest extends Request {

  private String firstName;
  private String lastName;
  private String alias;
  private String imageUrl;
  private byte[] imageBytes;

  private UserRequest() {
  }

  public UserRequest(String firstName, String lastName, String imageURL) {
    this(firstName, lastName, String.format("@%s%s", firstName, lastName), imageURL);
  }

  public UserRequest(String firstName, String lastName, String alias, String imageURL) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.alias = alias;
    this.imageUrl = imageURL;
  }

  public UserRequest(String user_handle) {
    this.alias = user_handle;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public byte[] getImageBytes() {
    return imageBytes;
  }

  public void setImageBytes(byte[] imageBytes) {
    this.imageBytes = imageBytes;
  }
}
