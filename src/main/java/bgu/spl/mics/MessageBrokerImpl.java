package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private ConcurrentHashMap <Subscriber, BlockingQueue<Message>> All_Subscribers_map;
	private ConcurrentHashMap <Event, Future> EventFuture_map;
	private ConcurrentHashMap <Class<? extends Event>,BlockingQueue<Subscriber>> EventsSubscribersMap;
	private ConcurrentHashMap <Class<? extends Broadcast>,LinkedList<Subscriber>> BroadcastMap;

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class MessageBrokerHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private MessageBrokerImpl(){

		All_Subscribers_map = new ConcurrentHashMap<>();
		EventFuture_map = new ConcurrentHashMap<>();
		EventsSubscribersMap = new ConcurrentHashMap<>();
		BroadcastMap = new ConcurrentHashMap<>();
	}


	public static MessageBroker getInstance() {
		return MessageBrokerImpl.MessageBrokerHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		EventsSubscribersMap.putIfAbsent(type,new LinkedBlockingDeque<>());
		EventsSubscribersMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		BroadcastMap.putIfAbsent(type,new LinkedList<>());
		synchronized (BroadcastMap.get(type)) {
			BroadcastMap.get(type).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		EventFuture_map.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		LinkedList<Subscriber> L= BroadcastMap.get(b.getClass());


		if (L!=null && !L.isEmpty()) {
				synchronized (L) {
					   for (Subscriber i : L) {
						All_Subscribers_map.get(i).add(b);
					}
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		BlockingQueue B = EventsSubscribersMap.get(e.getClass());
		if (B!=null||!B.isEmpty()) {
			Subscriber sub = null;

			if(B==null||B.isEmpty())
				return null;

			synchronized (B) {
				if (B.isEmpty() || B == null)
					return null;
				try {
					sub = (Subscriber) B.take();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				B.add(sub);

			if (sub!=null) {
				synchronized (sub){
					if(All_Subscribers_map.containsKey(sub)) {
						Future<T> future = new Future<>();
						EventFuture_map.put(e, future);
						All_Subscribers_map.get(sub).add(e);
						return future;
					}else
						return null;
		}}
			else return null;
		}}
		else return null;

	}



	@Override
	public void register(Subscriber m) {
    	All_Subscribers_map.put(m,new LinkedBlockingDeque<>());
	}

	@Override
	public void unregister(Subscriber m) {
		//TODO:CHECK ABOUT SYNCRONIZE



			for (Map.Entry<Class<? extends Event>, BlockingQueue<Subscriber>> entry : EventsSubscribersMap.entrySet()) {
				entry.getValue().remove(m);
			}

			for (Map.Entry<Class<? extends Broadcast>, LinkedList<Subscriber>> entry : BroadcastMap.entrySet()) {
				synchronized (entry.getValue()) {
					entry.getValue().remove(m);
				}
			}

        BlockingQueue b =All_Subscribers_map.get(m);
			synchronized (m){
		while (!b.isEmpty()) {
			Message M = All_Subscribers_map.get(m).poll();
			EventFuture_map.get(M).resolve(null);

		}
          All_Subscribers_map.remove(m);
			}

		}


	@Override
	public  Message awaitMessage(Subscriber m) throws InterruptedException {
			return All_Subscribers_map.get(m).take();
	}



}
