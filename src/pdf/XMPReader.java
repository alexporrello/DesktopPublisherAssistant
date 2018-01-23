package pdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ui.Tools;

public class XMPReader extends HashMap<String, String> {
	private static final long serialVersionUID = 2150338051657398900L;

	public XMPReader(String url) {
		
		//TODO Try converting the file to an XML file, then getting attributes. This might work much better than this random guesswork.
		
		for(String s : Tools.loadPlainTextFile(url)) {
			s = s.trim();

			if(s.startsWith("<pdfx:")) {
				s = manageTag(s, "<pdfx:");
			} else if(s.startsWith("<xmpMM:")) {
				s = manageTag(s, "<xmpMM:");
			} else if(s.startsWith("<xmp:")) {
				s = manageTag(s, "<xmp:");
			} else if(s.startsWith("<pdf:")) {
				s = manageTag(s, "<pdf:");
			} else if(s.startsWith("<rdf:li xml:lang=\"x-default\">")) {
				s = s.split(">")[1].split("<")[0];
				this.put("Title", s);
			}
		}
	}

	public ArrayList<String> sortedKeySet() {
		ArrayList<String> toSort = new ArrayList<String>();

		for(String s : this.keySet()) {
			toSort.add(s);
		}

		Collections.sort(toSort);

		return toSort;
	}

	public String manageTag(String s, String tag) {
		s = s.replace(tag, "");

		String[] split = s.split(">");

		for(int i = 0; i < split.length; i++) {
			if(split[i].contains("<")) {
				split[i] = split[i].split("<")[0];
			}
		}

		if(split.length == 2) {
			this.put(split[0], split[1]);
		}

		return s;
	}

}
