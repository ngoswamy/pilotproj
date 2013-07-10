
import javax.xml.transform.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xml.transform.StringSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.ws.test.server.MockWebServiceClient;                          
import static org.springframework.ws.test.server.RequestCreators.*;                      
import static org.springframework.ws.test.server.ResponseMatchers.*;                     

@RunWith(SpringJUnit4ClassRunner.class)                                                  
//@ContextConfiguration("WEB-INF/spring-ws-servlet.xml")                                           
public class MockTestClient {

  //@Autowired
  private ApplicationContext applicationContext = //;                                         
  //ApplicationContext ctx =
	    new FileSystemXmlApplicationContext("spring-ws-servlet.xml");
  private MockWebServiceClient mockClient;

  @Before
  public void createClient() {
    mockClient = MockWebServiceClient.createClient(applicationContext);                  
  }

  @Test
  public void customerEndpoint() throws Exception {
    Source requestPayload = new StringSource(
      "<feedRequest xmlns='http://springframework.org/spring-ws'>" +
        "<AccountId>1234</AccountId>" +
      "</feedRequest>");
    Source responsePayload = new StringSource(
      "<feedResponse xmlns='http://springframework.org/spring-ws'>" +
        "<duration>10</duration>" +
      "</feedResponse>");

    mockClient.sendRequest(withPayload(requestPayload)).                                 
      andExpect(payload(responsePayload));                                               
  }
}