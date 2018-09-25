package polytech.tours.di.parallel.tsp.myalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.Solution;

public class LocalSearch implements Callable<Solution> {
	private Random rLSRandom;
	private Instance iLSInstance;
	private TimeBomb bLSBomb;
	private TSPCostCalculatorThreadSafe cLSCalculator;
	private Solution dLSGlobalBest;
	private int nNbrThread;
	private int nNbrTask;
	private long lLSOptInstance;
	private boolean bLSGetOpt;
	private RecordInFile fLSRecord;

	public LocalSearch(long seed, Instance instance, TimeBomb bomb, long optInstance) {
		this.rLSRandom = new Random(seed);
		this.iLSInstance = instance;
		this.bLSBomb = bomb;
		this.lLSOptInstance = optInstance + 1;
		this.bLSGetOpt = false;
		this.cLSCalculator = new TSPCostCalculatorThreadSafe();
	}

	public Solution exploreNeighborhood(Solution sLSSolution) {
		Solution best = sLSSolution.clone();

		for (int i = 0; i < sLSSolution.size() - 1; i++) {
			for (int j = i + 1; j < sLSSolution.size(); j++) {
				sLSSolution.swap(i, j);
				sLSSolution.setOF(cLSCalculator.calc(iLSInstance, sLSSolution));

				if (sLSSolution.getOF() < best.getOF())
					best = sLSSolution.clone();
			}
		}

		return best;
	}

	public Solution exploreNeighborhood2(Solution sLSSolution) {
		Solution best = sLSSolution.clone();
		this.nNbrTask = sLSSolution.size() - 1;
		this.nNbrThread = Runtime.getRuntime().availableProcessors();
		this.nNbrThread = this.nNbrThread >= 4 ? this.nNbrThread : 4;
		// this.nNbrThread = nNbrThread * 3;
		// this.nNbrThread = 1;

		/** create tasks */
		ArrayList<Callable<Solution>> tasks = new ArrayList<>();
		List<Future<Solution>> results = null;

		for (int t = 0; t < nNbrTask; t++) {
			tasks.add(new SwapMovement(sLSSolution.clone(), iLSInstance, t));
		}

		/** Commencer mes tasks */
		// Instantiate a service executor
		ExecutorService executor = Executors.newFixedThreadPool(nNbrThread);
		try {
			results = executor.invokeAll(tasks);
			executor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** Find the best result (solution) */
		for (Future<Solution> r : results) {
			try {
				if (r.get().getOF() < best.getOF())
					best = r.get().clone();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		// System.out.println(result);

		return best;
	}

	@Override
	public Solution call() throws Exception {
		// this.fLSRecord = new RecordInFile(Thread.currentThread().getName());
		Solution solution = new Solution();
		for (int i = 0; i < iLSInstance.getN(); i++) {
			solution.add(i);
		}
		Collections.shuffle(solution, rLSRandom);
		solution.setOF(cLSCalculator.calc(iLSInstance, solution));

		dLSGlobalBest = solution.clone();
		Solution localBest = solution.clone();
		// Solution result = solution.clone();
		/** carry MLS iterations */
		while (!bLSBomb.exploded()) {
			/* perform local search */
			// solution = exploreNeighborhood(solution);

			solution = exploreNeighborhood2(solution);

			/* update local best */
			if (solution.getOF() < localBest.getOF()) {
				localBest = solution.clone();

				/* update global best */
				if (localBest.getOF() < dLSGlobalBest.getOF()) {
					dLSGlobalBest = solution.clone();

					/* Record in file */
					// fLSRecord.recordResult((int) dLSGlobalBest.getOF() + " " +
					// bLSBomb.usedTime());

					if (bLSGetOpt == false && dLSGlobalBest.getOF() < lLSOptInstance) {
						System.out.println(dLSGlobalBest + " BEST TOUR");
						System.out.println(Thread.currentThread().getName() + " We use " + bLSBomb.usedTime()
								+ " seconds to obtain the optmal tour");
						bLSGetOpt = true;
					}
				}
			} else {
				/* generate a new random solution */
				Collections.shuffle(solution, rLSRandom);
				solution.setOF(cLSCalculator.calc(iLSInstance, solution));
				localBest = solution.clone();
			}

		}
		return dLSGlobalBest;
	}

}
