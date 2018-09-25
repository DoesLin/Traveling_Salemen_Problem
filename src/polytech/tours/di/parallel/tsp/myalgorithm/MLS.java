package polytech.tours.di.parallel.tsp.myalgorithm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import polytech.tours.di.parallel.tsp.Algorithm;
import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.InstanceReader;
import polytech.tours.di.parallel.tsp.Solution;

public class MLS implements Algorithm {

	private RecordInFile fMLSRecord;

	@Override
	public Solution run(Properties config) {
		// read instance
		InstanceReader ir = new InstanceReader();
		ir.buildInstance(config.getProperty("instance"));
		// get the instance
		Instance instance = ir.getInstance();

		// read maximum CPU time
		long max_cpu = Long.valueOf(config.getProperty("maxcpu"));

		/** s* = generateRandomSolution() */
		// build a random solution
		Random rnd = new Random(Long.valueOf(config.getProperty("seed")));

		/** Obtenir le nombre de thread disponible */
		int nbrThread = Runtime.getRuntime().availableProcessors();
		nbrThread = nbrThread >= 4 ? nbrThread : 4;
		// nbrThread = nbrThread * 9;
		// nbrThread = 7;

		/** instantiate timebomb */
		TimeBomb bomb = new TimeBomb(max_cpu, TimeUnit.SECONDS);

		/** create tasks */
		ArrayList<Callable<Solution>> tasks = new ArrayList<>();
		List<Future<Solution>> results = null;
		for (int t = 0; t < nbrThread; t++) {
			tasks.add(new LocalSearch(rnd.nextLong(), instance, bomb, ir.optInstance()));
		}

		/** Commencer mes tasks */
		// Instantiate a service executor
		ExecutorService executor = Executors.newFixedThreadPool(nbrThread);
		bomb.start();
		try {
			results = executor.invokeAll(tasks);
			executor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** Find the best result (solution) */
		Solution best = null;
		for (Future<Solution> r : results) {
			try {
				if (best == null)
					best = r.get();
				else if (r.get().getOF() < best.getOF())
					best = r.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("Final" + best);
		// this.fMLSRecord = new RecordInFile("ThreadsCompare", true);
		// DecimalFormat df = new DecimalFormat("######0.00");
		// fMLSRecord.recordResult(nbrThread + " " + df.format((best.getOF() - 9352) / 9352));

		return best;
	}

}
