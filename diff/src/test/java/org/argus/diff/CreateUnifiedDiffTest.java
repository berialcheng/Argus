package org.argus.diff;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class CreateUnifiedDiffTest {

	@Test
	public void testCreateUnifiedDiff() throws Exception {
		URL previous = this.getClass().getResource("pom.xml-v2096");
		URL after = this.getClass().getResource("pom.xml-v2635");
		CreateUnifiedDiff diff = new CreateUnifiedDiff(new File(previous.toURI()), new File(after.toURI()), 3);
		for (SequenceElement<String> se : diff.getDiff()) {
			System.out.println(diff.stringRepresentation);
		}

	}
}
