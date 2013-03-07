package org.argus.diff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LongestCommonSubsequence<T> {

	ArrayList<T> firstList, secondList;

	public LongestCommonSubsequence(List<T> first, List<T> second) {
		this.firstList = new ArrayList<T>(first);
		this.secondList = new ArrayList<T>(second);
	}

	protected int[][] computeLength() {
		int table[][] = new int[this.firstList.size() + 1][this.secondList.size() + 1];

		for (int i = 0; i < firstList.size(); i++) {
			for (int j = 0; j < secondList.size(); j++) {
				if (firstList.get(i).equals(secondList.get(j))) {
					table[i + 1][j + 1] = table[i][j] + 1;
				} else {
					table[i + 1][j + 1] = Math.max(table[i + 1][j], table[i][j + 1]);
				}
			}
		}

		return table;
	}

	public List<SequenceElement<T>> findDiff() {
		LinkedList<SequenceElement<T>> sequence = new LinkedList<SequenceElement<T>>();
		int[][] table = computeLength();

		int i = table.length - 1;
		int j = table[0].length - 1;
		while (i > 0 && j > 0) {

			if (firstList.get(i - 1).equals(secondList.get(j - 1))) {
				sequence.addFirst(new SequenceElement<T>(firstList.get(i - 1), SequenceElement.Status.UNTOUCHED));
				i--;
				j--;
			} else if (table[i][j - 1] >= table[i - 1][j]) {
				sequence.addFirst(new SequenceElement<T>(secondList.get(j - 1), SequenceElement.Status.ADDED));
				j--;
			} else {
				sequence.addFirst(new SequenceElement<T>(firstList.get(i - 1), SequenceElement.Status.REMOVED));
				i--;
			}
		}

		while (i > 0) {
			sequence.addFirst(new SequenceElement<T>(firstList.get(i - 1), SequenceElement.Status.REMOVED));
			i--;
		}
		while (j > 0) {
			sequence.addFirst(new SequenceElement<T>(secondList.get(j - 1), SequenceElement.Status.ADDED));
			j--;
		}

		return sequence;
	}
}