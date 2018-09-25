package polytech.tours.di.parallel.tsp.myalgorithm;

import java.util.concurrent.Callable;

import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.Solution;

public class SwapMovement implements Callable<Solution> {

	private int nSMBoucleI;
	private int nSMBoucleJ;
	private Solution sSMSolution;
	private Instance iSMInstance;
	private TSPCostCalculatorThreadSafe cSMCalculator;

	public SwapMovement(Solution sSolution, Instance iInstance, int nBoucleI) {
		this.nSMBoucleI = nBoucleI;
		this.iSMInstance = iInstance;
		this.cSMCalculator = new TSPCostCalculatorThreadSafe();
		this.sSMSolution = sSolution;
		this.nSMBoucleJ = nSMBoucleI + 1;
	}

	@Override
	public Solution call() throws Exception {

		Solution best = sSMSolution.clone();

		// System.out.println("Begin : " + Thread.currentThread().getName() + " i=" +
		// nSMBoucleI + " j=" + nSMBoucleJ);

		while (nSMBoucleJ < sSMSolution.size()) {
			if (nSMBoucleJ != nSMBoucleI) {
				sSMSolution.swap(nSMBoucleI, nSMBoucleJ);
				sSMSolution.setOF(cSMCalculator.calc(iSMInstance, sSMSolution));
				// System.out.println(Thread.currentThread().getName() + " i=" + nSMBoucleI + "
				// j=" + nSMBoucleJ);
				if (sSMSolution.getOF() < best.getOF()) {
					best = sSMSolution.clone();
				}
			}
			nSMBoucleJ++;
		}

		// System.out.println("Leaving : " + Thread.currentThread().getName() + " i=" +
		// nSMBoucleI + " j=" + nSMBoucleJ);

		return best;
	}

}
