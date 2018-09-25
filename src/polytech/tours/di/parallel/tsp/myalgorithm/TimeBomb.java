package polytech.tours.di.parallel.tsp.myalgorithm;

import java.util.concurrent.TimeUnit;

public class TimeBomb {

	private final long maxcpu;
	private long start;

	public TimeBomb(long maxcpu, TimeUnit timeUnit) {
		this.maxcpu = TimeUnit.MILLISECONDS.convert(maxcpu, timeUnit);
	}

	public synchronized void start() {
		this.start = System.currentTimeMillis();
	}

	public boolean exploded() {
		return System.currentTimeMillis() - start > maxcpu;
	}

	public long usedTime() {
		return TimeUnit.SECONDS.convert(System.currentTimeMillis() - this.start, TimeUnit.MILLISECONDS);
	}

}
