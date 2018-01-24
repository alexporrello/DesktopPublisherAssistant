package pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.xml.xmp.XmpWriter;

public class XMPUpdater {
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
		stamper.setMoreInfo(info);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		XmpWriter xmp = new XmpWriter(baos);
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
		String pathToOutputPDF = pathToPDF.replace(".pdf", "_tagged.pdf");
		updatePDFXMP(pathToPDF, pathToOutputPDF, pathToXMP);
	}
}
