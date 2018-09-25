package polytech.tours.di.parallel.tsp.myalgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RecordInFile {

	private String sRINFileName;

	public RecordInFile(String sFileName) {
		this.sRINFileName = "record/" + sFileName + ".txt";
		FileWriter fw = null;
		try {
			fw = new FileWriter(sRINFileName);
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}
		}
	}

	public RecordInFile(String sFileName, boolean apprend) {
		this.sRINFileName = "record/" + sFileName + ".txt";
		if (apprend == true) {
			;
		} else {
			FileWriter fw = null;
			try {
				fw = new FileWriter(sRINFileName);
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			} finally {
				try {
					if (fw != null)
						fw.close();
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
			}
		}
	}

	public void recordResult(String sRecord) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(sRINFileName, true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			out.println(sRecord);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} finally {
			if (out != null)
				out.close();
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}
		}
	}

	public void test() {
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(sRINFileName, true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			out.println("the text");
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} finally {
			if (out != null)
				out.close();
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}
		}
	}
}
