package pdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFPropertiesUpdater {
	/**
	 * Updates the default view properties of the PDF to align with NI's
	 * desktop publishing standards.
	 * @param pathToPDF the path to the PDF to be updated.
	 * @param pathToOutputPDF the path to the output PDF.
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void updateOpenProperties(String pathToPDF, String pathToOutputPDF) throws IOException, DocumentException {
		PdfReader  reader  = new PdfReader(new FileInputStream(pathToPDF));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathToOutputPDF));

		stamper.getWriter().setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.FIT), stamper.getWriter()));
		stamper.getWriter().setViewerPreferences(PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseOutlines);
		
		stamper.close();
		reader.close();
	}

	/**
	 * Updates the default view properties of the PDF to align with NI's
	 * desktop publishing standards.
	 * @param pathToPDF the PDF to be udpated.
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void updateOpenProperties(String pathToPDF) throws IOException, DocumentException {
		updateOpenProperties(pathToPDF, pathToPDF);
	}
}
