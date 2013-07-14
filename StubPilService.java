
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service                                                                                 
public class StubPilService implements PilService {
	private DriverManagerDataSource dataSource;
	public StubPilService(DriverManagerDataSource dataSource){
		this.dataSource = dataSource;
	}
	private static final Log logger = LogFactory.getLog(StubPilService.class);
    public void storeRecord(SalesforceRecord salesRecord) {
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //'2008-7-04', '2008-7-06'
    	String crDate= dateFormat.format(salesRecord.getCreatedDate());
    	String clDate= dateFormat.format(salesRecord.getCloseDate());
    	String isclosed = salesRecord.isClose() ? "TRUE" : "FALSE";
    	JdbcTemplate jt = new JdbcTemplate(dataSource); 
        jt.execute("insert into salesforce (accountid, amount, createddate, closedate, isclosed, totalopportunityquantity, type) values('"+salesRecord.getAccountId()+"',"+salesRecord.getAmount()+", '"+crDate+"', '"+clDate+"', "+isclosed+","+salesRecord.getTotalOpportunityQuantity()+", '"+salesRecord.getType()+"')");
        logger.info("Insert Statement executed");
        logger.info("insert into salesforce (accountid, amount, createddate, closedate, isclosed, totalopportunityquantity, type) values('"+salesRecord.getAccountId()+"',"+salesRecord.getAmount()+", '"+crDate+"', '"+clDate+"', "+isclosed+","+salesRecord.getTotalOpportunityQuantity()+", '"+salesRecord.getType()+"')");
    }
}