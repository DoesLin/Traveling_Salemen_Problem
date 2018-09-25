package polytech.tours.di.parallel.tsp.myalgorithm;

import polytech.tours.di.parallel.tsp.Instance;
import polytech.tours.di.parallel.tsp.Solution;

public class TSPCostCalculatorThreadSafe {

	public double calc(Instance instance, Solution s){
		double cost=0;
		for(int i=1;i<s.size();i++){
			cost=cost+instance.getDistance(s.get(i-1),s.get(i));
		}
		cost=cost+instance.getDistance(s.get(s.size()-1),s.get(0));
		return cost;
	}
	
}
