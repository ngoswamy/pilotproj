
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.xpath.XPath;

@SuppressWarnings("deprecation")
@Endpoint                                                                                
public class PilEndpoint {

  private static final Logger log = Logger.getLogger(PilEndpoint.class);
	
  private static final String NAMESPACE_URI = "http://solutionxt.com/pil/schemas";
  
  public SalesforceRecord salesRec;
  
  private XPath accountIdExpression;
  
  private XPath amountExpression;

  private XPath createdDateExpression;

  private XPath closeDateExpression;

  private XPath isCloseExpression;
  
  private XPath totalOpportunityQuantityExpression;
  
  private XPath typeExpression;

  private PilService pilService;

@Autowired
  public PilEndpoint(PilService pilService)                      
      throws JDOMException {
	//ApplicationContext ctx = new ClassPathXmlApplicationContext(
    //"spring-ws-servlet.xml");

    this.pilService = pilService;

    Namespace namespace = Namespace.getNamespace("pil", NAMESPACE_URI);

    accountIdExpression = XPath.newInstance("//pil:AccountId");
    accountIdExpression.addNamespace(namespace);

    amountExpression = XPath.newInstance("//pil:Amount");
    amountExpression.addNamespace(namespace);
    
    createdDateExpression = XPath.newInstance("//pil:CreatedDate");
    createdDateExpression.addNamespace(namespace);
    
    closeDateExpression = XPath.newInstance("//pil:CloseDate");
    closeDateExpression.addNamespace(namespace);

    isCloseExpression = XPath.newInstance("//pil:IsClose");
    isCloseExpression.addNamespace(namespace);
    
    totalOpportunityQuantityExpression = XPath.newInstance("//pil:TotalOpportunityQuantity");
    totalOpportunityQuantityExpression.addNamespace(namespace);
    
    typeExpression = XPath.newInstance("//pil:Type");
    typeExpression.addNamespace(namespace);

  }

@PayloadRoot(namespace = NAMESPACE_URI, localPart = "FeedRequest")                  
  public void handleFeedRequest(@RequestPayload Element feedRequest)               
      throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String accountId = accountIdExpression.toString();
    double amount = Double.parseDouble(amountExpression.valueOf(feedRequest));
    Date createdDate = dateFormat.parse(createdDateExpression.valueOf(feedRequest));
    Date closeDate = dateFormat.parse(closeDateExpression.valueOf(feedRequest));
    boolean isClose = isCloseExpression.valueOf(feedRequest).equalsIgnoreCase("true")? true : false;
    double totalOpportunityQuantity = Double.parseDouble(totalOpportunityQuantityExpression.valueOf(feedRequest));
    String type = typeExpression.valueOf(feedRequest);

    SalesforceRecord salesRec = new SalesforceRecord();
    salesRec.setAccountId(accountId);
    salesRec.setAmount(amount);
    salesRec.setCreatedDate(createdDate);
    salesRec.setCloseDate(closeDate);
    salesRec.setClose(isClose);
    salesRec.setTotalOpportunityQuantity(totalOpportunityQuantity);
    salesRec.setType(type);
    pilService.storeRecord(salesRec);
  }

}