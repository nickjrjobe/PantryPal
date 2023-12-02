package PantryPal;

/** Mock HttpModel for testing */
public class MockHttpModel implements HttpModel {
  public String mockResponse;
  public String method;
  public String query;
  public String request;

  @Override
  public boolean tryConnect(String method, String query, String request) {
    // Mock constuctor
    return false;
  }

  @Override
  public void setPath(String path) {
    // Mock constuctor
  }

  @Override
  public String performRequest(String method, String query, String request) {
    this.method = method;
    this.query = query;
    this.request = request;
    return this.mockResponse;
  }

  /**
   * Sets the response for calling performRequest
   *
   * @param mockResponse
   */
  public void setMockResponse(String mockResponse) {
    this.mockResponse = mockResponse;
  }
}
