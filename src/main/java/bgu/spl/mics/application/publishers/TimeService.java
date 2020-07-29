package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.TerminatedEventBroadcast;
import bgu.spl.mics.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {


	private int duration;
	private AtomicInteger currentTick;
	private Timer timer;


	public TimeService(int duration) {
		super("TimeService");
		this.duration=duration;
		currentTick=new AtomicInteger(1);
		timer=new Timer();
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (currentTick.get() < duration) {
					getSimplePublisher().sendBroadcast(new TickBroadcast(currentTick.getAndIncrement()));
					int i= currentTick.get()-1;
			///		System.out.println(i + " tickBroadcast sent");

				} else { //this the termination tick
					getSimplePublisher().sendBroadcast(new TerminatedEventBroadcast());
	//				System.out.println(" TerminatedEventBroadcast sent");
					timer.cancel();
				//	Diary.getInstance().getTotal();
				}
			}
		}, 1000, 100);
		////subscribeBroadcast(TickFinalBroadcast.class, (TickFinalBroadcast tick) -> {this.terminate();});

	}

}
