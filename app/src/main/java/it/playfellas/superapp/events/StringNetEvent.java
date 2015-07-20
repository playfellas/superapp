package it.playfellas.superapp.events;

public class StringNetEvent extends NetEvent {
  private String body;

  public StringNetEvent(String body) {
    this.body = this.myDeviceName + " -> " + body;
  }

  public String getBody() {
    return body;
  }
}
