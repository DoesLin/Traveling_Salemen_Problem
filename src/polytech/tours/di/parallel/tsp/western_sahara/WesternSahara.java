package polytech.tours.di.parallel.tsp.western_sahara;


import polytech.tours.di.parallel.tsp.EuclideanCalculator;
import polytech.tours.di.parallel.tsp.Instance;

/**
 * Implements a TSP mock builder that builds the Western Sahara instance reported at:
 * http://www.math.uwaterloo.ca/tsp/world/countries.html
 * 
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class WesternSahara{

	
	public static Instance buildMock() {
		double[][] coordinates= {
				{20833.3333,17100.0000},
				{20900.0000,17066.6667},
				{21300.0000,13016.6667},
				{21600.0000,14150.0000},
				{21600.0000,14966.6667},
				{21600.0000,16500.0000},
				{22183.3333,13133.3333},
				{22583.3333,14300.0000},
				{22683.3333,12716.6667},
				{23616.6667,15866.6667},
				{23700.0000,15933.3333},
				{23883.3333,14533.3333},
				{24166.6667,13250.0000},
				{25149.1667,12365.8333},
				{26133.3333,14500.0000},
				{26150.0000,10550.0000},
				{26283.3333,12766.6667},
				{26433.3333,13433.3333},
				{26550.0000,13850.0000},
				{26733.3333,11683.3333},
				{27026.1111,13051.9444},
				{27096.1111,13415.8333},
				{27153.6111,13203.3333},
				{27166.6667,9833.3333},
				{27233.3333,10450.0000},
				{27233.3333,11783.3333},
				{27266.6667,10383.3333},
				{27433.3333,12400.0000},
				{27462.5000,12992.2222},
		}; 
		
		return buildEuclideanInstanceFromCoordinates(29, coordinates);	
	}
	
	/**
	 * Builds a TSP instance from a coordinates matrix
	 * @param n the number of nodes in the instance
	 * @param coordinates the matrix holding the coordinates of the nodes
	 * @return the TSP instance
	 */
	public static Instance buildEuclideanInstanceFromCoordinates(int n, double[][] coordinates){
		double[][] matrix=EuclideanCalculator.calc(coordinates);
		Instance instance=new Instance(matrix);
		return instance;
	}

}
