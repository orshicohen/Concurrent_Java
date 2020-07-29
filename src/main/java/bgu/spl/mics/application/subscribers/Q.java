package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private int currenttime=0;

	public Q() {
		super("Q");
	}

	@Override
	protected void initialize() {
		MessageBrokerImpl.getInstance().register(this);
		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent x)-> {
			boolean gadget = Inventory.getInstance().getItem(x.getGadget());
			Result result = new Result();
			result.setQtime(currenttime);
			result.setGadgetAvailable(gadget);
		//	System.out.println("send to complete: " +"result: " + result.isGadgetAvailable()+ " for Gadget Available event: "+ x.getGadget() );
			complete(x,result);
		});
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast x)-> {
			currenttime=x.getTime();
		});
		this.subscribeBroadcast(TerminatedEventBroadcast.class, (TerminatedEventBroadcast x)-> {terminate();
		//	System.out.println("Q " + " terminated");

		});
	}

}
