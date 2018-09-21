package cybereast.model;

public class FileView {
  private String name;
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public FileView(){}

  public FileView(String name) {
    this.name = name;
  }

  public FileView(String name, String url) {
    this.name = name;
    this.url = url;
  }
}
