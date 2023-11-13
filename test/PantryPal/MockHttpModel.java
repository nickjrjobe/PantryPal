package PantryPal;


/**
 * Mock HttpModel for testing
 */
public class MockHttpModel implements HttpModel {
  private String path;
  private String mockResponse;

  @Override
  public void setPath(String path) {
    // Mock constuctor
  }

  @Override
  public String performRequest(String method, String query, String request) {
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
