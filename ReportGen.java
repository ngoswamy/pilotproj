import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class ReportGen {

	private static DriverManagerDataSource dataSource;
	private static final Log logger = LogFactory.getLog(ReportGen.class);
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;

	@SuppressWarnings("static-access")
	public ReportGen(DriverManagerDataSource dataSource){
		this.dataSource = dataSource;
	}
	public DriverManagerDataSource getDataSource(){
		return this.dataSource;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SalesforceRecord sfRec;
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
	    "spring-ws-servlet.xml");
		ReportGen repgen = (ReportGen)ctx.getBean("repGen");
		JdbcTemplate jt = new JdbcTemplate(repgen.getDataSource());
        String sql_query="Select * from salesforce";
        List<SalesforceRecord> sfRecords= jt.query(sql_query, new SalesForceMinMapper());
        Iterator<SalesforceRecord> it= sfRecords.iterator();
        logger.info("Account Id Amount  Created Date Close Date  Closed Total Opportunity Cost(Summary) Type");
        while(it.hasNext()){
        	sfRec = it.next();
        	
        	logger.info(sfRec.getAccountId()+"    "+sfRec.getAmount()+"   "+sfRec.getCreatedDate().toString()+"    "+sfRec.getCloseDate().toString()+"    "+sfRec.isClose()+"         "+sfRec.getTotalOpportunityQuantity()+"         "+sfRec.getType());
        }
        String sql_query3="Select * from salesforce";
        List<SalesforceRecord> sfRecords3= jt.query(sql_query3, new SalesForceMinMapper());
        Iterator<SalesforceRecord> it3= sfRecords3.iterator();
        
        long atotal = 0L;
        int numrecs=0;
        while(it3.hasNext()){
        	sfRec = it3.next();
        	numrecs++;
        	atotal = atotal + (sfRec.getCloseDate().getTime() - sfRec.getCreatedDate().getTime()); 
        }
        atotal = atotal/numrecs;
        //logger.info(""+atotal);
        StringBuffer text = new StringBuffer("");
        if (atotal > DAY) {
          text.append(atotal / DAY).append(" days ");
          atotal %= DAY;
        }
        if (atotal > HOUR) {
          text.append(atotal / HOUR).append(" hours ");
          atotal %= HOUR;
        }
        if (atotal > MINUTE) {
          text.append(atotal / MINUTE).append(" minutes ");
          atotal %= MINUTE;
        }
        if (atotal > SECOND) {
          text.append(atotal / SECOND).append(" seconds ");
          atotal %= SECOND;
        }
        text.append(atotal + " ms");
        logger.info("Average time from creation to close  "+text.toString());
        logger.info("Summary of Total Opportuniy Quantity");
        String sql_query1="Select accountid,amount,createddate, closedate, isclosed, SUM(totalopportunityquantity) as totalopportunityquantity,type from salesforce group by type";
        List<SalesforceRecord> sfRecords1= jt.query(sql_query1, new SalesForceMinMapper());
        Iterator<SalesforceRecord> it1= sfRecords1.iterator();
        logger.info("Total Opportunity Cost(Summary) Type");
        while(it1.hasNext()){
        	sfRec = it1.next();
        	
        	logger.info("    "+sfRec.getTotalOpportunityQuantity()+"                 "+sfRec.getType());
        }
	}

}
