import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.regex.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import java.util.StringTokenizer;


public class ScoopServlet extends HttpServlet {
public void doGet(HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException
{
response.setContentType("text/XML;charset=ISO-8859-1");
PrintWriter out = response.getWriter();
//String inputV = request.getParameter("inputValue");
HttpClient httpClient = new HttpClient();
String temp = "http://www.feedrinse.com/services/rinse/?rinsedurl=396ac3b0142bb6360003c8dbac4f8d47";
PostMethod post = new PostMethod(temp);
String XMLstr="<FEED>";
StringTokenizer strtok=new StringTokenizer(inputV,":");
 try{
	httpClient.executeMethod(post);
	String xmlstr=post.getResponseBodyAsString();
	SAXBuilder sax=new SAXBuilder();
	while(strtok.hasMoreTokens()){
	String keyword=strtok.nextToken();
	Document doc= (Document) sax.build(new StringReader(xmlstr));
	Element rootElem=doc.getRootElement();
	Element res=rootElem.getChild("result");
	List docs=res.getChildren("doc");
	for(int i=0;i<docs.size();i++)
	{
		//out.println("docsSize = " + docs.size());
		Element row= (Element)docs.get(i);
		List strs=row.getChildren("str");
		//out.println("strs = " + strs.size());
		Element strRow=(Element)strs.get(0);
		String strContent=strRow.getText().toString();
		Element strRow1=(Element)strs.get(1);
		String Lati=strRow1.getText().toString();
		Element strRow2=(Element)strs.get(2);
		String Longi=strRow2.getText().toString();
		Element strRow3=(Element)strs.get(3);
		String FileName=strRow3.getText().toString();
		Pattern p= Pattern.compile("\\b"+keyword+"\\b",Pattern.CASE_INSENSITIVE);
		Matcher m= p.matcher(strContent);
		if(m.find())
			{
				XMLstr+="<File><id>"+FileName+"</id><Lat>"+Lati+"</Lat><Long>"+Longi+"</Long></File>\n";
			}	
		//out.println(strRow.getText());
		//break;
	}
	}
	XMLstr+="</FEED>";
	out.println(XMLstr);
}catch(Exception e)
{
	out.println(e);
}	
}
}

