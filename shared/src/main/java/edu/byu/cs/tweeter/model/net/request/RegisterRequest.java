package edu.byu.cs.tweeter.model.net.request;

/**
 * Contains all the information needed to make a login request.
 */
public class RegisterRequest {

  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String imageBytes;
  private String imageURL;

  public RegisterRequest() {
  }

  public RegisterRequest(String firstName, String lastName, String username, String password, String imageBytes, String imageURL) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.imageBytes = imageBytes;
    this.imageURL = imageURL;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getImageBytes() {
    return imageBytes;
  }

  public void setImageBytes(String imageBytes) {
    this.imageBytes = imageBytes;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }
}
