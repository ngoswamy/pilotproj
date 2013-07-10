
import java.io.InputStream;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.easymock.EasyMock.*;
import org.easymock.EasyMock;

public class Mock2TestClient {

    private Document feedRequest;

    private PilEndpoint endpoint;

    private PilService serviceMock;

    private Calendar createdCalendar;

    private Calendar closeCalendar;
    
    private String accountId;
    private double amount;
    //private Date createdDate;
    //private Date closeDate;
    private boolean isClose;
    private double totalOpportunityQuantity;
    private String type;

    @Before
    public void setUp() throws Exception {
    	//ApplicationContext ctx = new ClassPathXmlApplicationContext(
    	//	    "spring-ws-servlet.xml");
        serviceMock = createMock(PilService.class);
        SAXBuilder builder = new SAXBuilder();
        InputStream is = getClass().getResourceAsStream("feedRequest.xml");
        try {
            feedRequest = builder.build(is);
        }
        finally {
            is.close();
        }
        endpoint = new PilEndpoint(serviceMock);
        createdCalendar = Calendar.getInstance();
        createdCalendar.clear();
        createdCalendar.set(2013, Calendar.JULY, 3);
        closeCalendar = Calendar.getInstance();
        closeCalendar.clear();
        closeCalendar.set(2013, Calendar.JULY, 7);
        accountId="234";
        amount = 2000.35D;
        isClose= true;
        totalOpportunityQuantity = 213D;
        type = "Events";
    }

    @Test
    public void handleFeedRequest() throws Exception {
        SalesforceRecord sRec = new SalesforceRecord();
        sRec.setAccountId(accountId);
        sRec.setAmount(amount);
        sRec.setCreatedDate(new Date(createdCalendar.getTimeInMillis()));
        sRec.setCloseDate(new Date(closeCalendar.getTimeInMillis()));
        sRec.setClose(isClose);
        sRec.setTotalOpportunityQuantity(totalOpportunityQuantity);
        sRec.setType(type);
        serviceMock.storeRecord(EasyMock.geq(sRec));
        replay(serviceMock);
        endpoint.handleFeedRequest(feedRequest.getRootElement());
        verify(serviceMock);
    }


}

