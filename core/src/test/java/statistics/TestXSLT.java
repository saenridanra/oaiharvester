package statistics;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestXSLT {

	String stylesheet;
	Document document;
	Document styleDocument;

	@BeforeClass
	public void setUp() {
		stylesheet = "src/main/resources/statistics.xsl";
		try {
			document = (new SAXReader()).read(new File(
					"src/test/resources/statistics.xml"));

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testXslt() {
		try {
			styleDocument = styleDocument(document, stylesheet);
			System.out.println(styleDocument.asXML());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Document styleDocument(Document document, String stylesheet)
			throws Exception {

		// load the transformer using JAXP
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(
				stylesheet));

		// now lets style the given document
		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);

		// return the transformed document
		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}
}
