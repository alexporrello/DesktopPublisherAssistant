package pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.xml.xmp.XmpWriter;


public class XMPWork {

	public XMPWork() throws DocumentException, IOException {
		//		Document document = new Document();
		//	    PdfWriter.getInstance(document, new FileOutputStream("2.pdf"));
		//	    document.open();
		//	    document.add(new Paragraph("Hello World"));
		//	    document.close();

		//		Path path = Paths.get("path/to/file");
		//		XmpReader xmp = new XmpReader(Files.readAllBytes(path));

		
		
		String path = "D:\\users\\aporrell\\Desktop\\Maintaining NI CMS-9065 Hardware for an NI InsightCM System\\PDFs";

		updatePDFXMP(path + "\\376988c_01.pdf", path + "\\376988c_Edited.pdf", path + "\\376988c.xmp");
	}
	
	/**
	 * Updates a PDFs XMP, given the PDF's path, the path to the updated PDF, and the path to the XMP.
	 * @param pathToPDF the original PDF
	 * @param pathToOutputPDF the PDF updated with XMP
	 * @param pathToXMP the path to the XMP to be added to the PDF
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void updatePDFXMP(String pathToPDF, String pathToOutputPDF, String pathToXMP) throws IOException, DocumentException {
		PdfReader  reader  = new PdfReader(pathToPDF);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathToOutputPDF));
		
		HashMap<String, String> info = new XMPReader(pathToXMP);
		//stamper.setXmpMetadata(Files.readAllBytes(Paths.get(pathToXMP)));
		stamper.setMoreInfo(info);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmpWriter xmp = new XmpWriter(baos, info);
		xmp.close();

		stamper.setXmpMetadata(baos.toByteArray());
		stamper.close();
	}

	/**
	 * Updates a PDFs XMP, given the PDF's path and the path to the XMP.
	 * @param pathToPDF the PDF to be updated
	 * @param pathToXMP the path to the XMP to be added to the PDF
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void updatePDFXMP(String pathToPDF, String pathToXMP) throws IOException, DocumentException {
		updatePDFXMP(pathToPDF, pathToPDF, pathToXMP);
	}
}
