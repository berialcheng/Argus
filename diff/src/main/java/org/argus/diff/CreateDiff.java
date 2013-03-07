package org.argus.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateDiff {

	protected LinkedList<SequenceElement<String>> beginOffset;
	protected LinkedList<SequenceElement<String>> endOffset;

	protected LinkedList<String> firstFile;
	protected LinkedList<String> secondFile;

	protected ArrayList<SequenceElement<String>> diff;

	public CreateDiff(File fst, File snd) throws FileNotFoundException, IOException {

		firstFile = loadLines(fst);
		secondFile = loadLines(snd);

		beginOffset = new LinkedList<SequenceElement<String>>();
		endOffset = new LinkedList<SequenceElement<String>>();

		stripMargin();
		diffFiles();

	}

	public CreateDiff(List<SequenceElement<String>> d) {
		diff = new ArrayList<SequenceElement<String>>(d);
	}

	/**
	 * @brief Nacteni radku souboru.
	 * 
	 *        Nacte soubor radek po radku.
	 * 
	 * @param f
	 *            Soubor, ktery se ma nacist.
	 * @return Seznam radku souboru.
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	protected LinkedList<String> loadLines(File f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		LinkedList<String> lines = new LinkedList<String>();
		String l;
		while ((l = reader.readLine()) != null) {
			lines.addLast(l);
		}
		return lines;
	}

	protected void stripMargin() {
		while (!firstFile.isEmpty() && !secondFile.isEmpty() && firstFile.getFirst().equals(secondFile.getFirst())) {
			beginOffset.addLast(new SequenceElement<String>(firstFile.pollFirst(), SequenceElement.Status.UNTOUCHED));
			secondFile.pollFirst();
		}

		while (!firstFile.isEmpty() && !secondFile.isEmpty() && firstFile.getLast().equals(secondFile.getLast())) {
			endOffset.addFirst(new SequenceElement<String>(firstFile.pollLast(), SequenceElement.Status.UNTOUCHED));
			secondFile.pollLast();
		}
	}

	protected void diffFiles() {
		LongestCommonSubsequence<String> lcs = new LongestCommonSubsequence<String>(firstFile, secondFile);
		diff = new ArrayList<SequenceElement<String>>(beginOffset);
		diff.addAll(lcs.findDiff());
		diff.addAll(endOffset);
	}

	public ArrayList<SequenceElement<String>> getDiff() {
		return diff;
	}

	@Override
	public String toString() {
		return diff.toString() + "\n";
	}
}