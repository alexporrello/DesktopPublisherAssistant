package pdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ui.Tools;

public class XMPReader extends HashMap<String, String> {
	private static final long serialVersionUID = 2150338051657398900L;

	/**
	 * Reads an XMP file per the requirements at National Instruments.
	 * @param url the url of the XMP file.
	 */
	public XMPReader(String url) {
		addAllTagsToReader(removeBoilerplate(removeAllXMPTags(url)));
	}

	/**
	 * Removes all of the XMP tags, making the document more resemble an XML file.
	 * @return an ArrayList, where each item in the array is a line in the XMP.
	 */
	private ArrayList<String> removeAllXMPTags(String url) {
		ArrayList<String> array = new ArrayList<String>();

		for(String s : Tools.loadPlainTextFile(url)) {
			s = s.trim();

			String[] toReplace = {"xmp:", "dc:", "pdf:", "pdfx:", "xmpMM:", "rdf:", 
					"<Alt>", "</Alt>", "<li>", "</li>", "<Bag>", "</Bag>"};

			for(String replace : toReplace) {
				s = s.replace(replace, "");
			}

			s = s.replace(" about=\"\"", ">");
			s = s.replace("<li xml:lang=\"x-default\">", "");

			if(!s.equals("")) {
				array.add(s);
			}
		}
		
		return array;
	}
	
	/**
	 * Removes the boilerplate top and bottom of the document.
	 * @param array create this array in {@link #removeAllXMPTags(String)}
	 * @return an updated Array with the boilerplate removed
	 */
	private ArrayList<String> removeBoilerplate(ArrayList<String> array) {
		Boolean flagTop = false;
		Boolean flagBot = false;

		ArrayList<String> newArray = new ArrayList<String>();

		for(String s : array) {			
			if(!flagTop) {
				flagTop = s.contains("<Description>");
			}

			if(!flagBot) {
				flagBot = s.contains("</Description>");
			}

			if(flagTop) {
				newArray.add(s);
			}

			if(flagTop && flagBot) {
				break;
			}
		}
		
		return newArray;
	}

	/**
	 * Locates all viable tags and adds the key, value pair to this.
	 * @param array create this array in {@link #removeBoilerplate(ArrayList)}
	 */
	private void addAllTagsToReader(ArrayList<String> array) {
		for(String s : array) {
			if(s.contains("<title>")) {
				if(s.contains("</title>")) {
					s = s.split(">")[1].split("<")[0];
					put("Title", s);
				} else {
					put("Title", array.get(array.indexOf(s)+1));
				}
			}

			if(s.contains("<subject>")) {
				if(s.contains("</subject>")) {
					s = s.split(">")[1].split("<")[0];
					put("Subject", s);
				} else {
					put("Subject", array.get(array.indexOf(s)+1));
				}
			}
		}

		for(String s : array) {
			if(s.contains("<")) {
				if(s.split(">").length > 1) {
					String value = s.split(">")[1].split("<")[0];
					String key   = s.split(">")[0].replace("<", "");

					if(!value.equals("")) {
						this.put(key, value);
					}
				}
			}
		}
	}

	/**
	 * @return An ArrayList of the keys, sorted alphabetically.
	 */
	public ArrayList<String> sortedKeySet() {
		ArrayList<String> toSort = new ArrayList<String>();

		for(String s : this.keySet()) {
			toSort.add(s);
		}

		Collections.sort(toSort);

		return toSort;
	}
}
