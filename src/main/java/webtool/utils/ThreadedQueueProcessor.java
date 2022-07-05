package webtool.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public abstract class ThreadedQueueProcessor</* Request type */CMD_TYPE /*extends CmdInterface*/> {

		protected static Logger log = Logger.getLogger(ThreadedQueueProcessor.class);

		private BlockingQueue<CMD_TYPE> cmdQueue = new ArrayBlockingQueue<CMD_TYPE>(500000); //new LinkedBlockingDeque<CMD_TYPE>();

		boolean quit = false;
		String processorName = "undefined";
		public static final int MAX_FAIL_COUNT = 3;
		protected int systemFailCount = 0;
		final ExecutorService executorService;
		
		public abstract boolean processMessage(CMD_TYPE cmd);
		public abstract void shutdown();
		//abstract protected Y processReply(ByteBuffer msg); // send command to the gateway
		// take the byte result and create a reply object Y

		public ThreadedQueueProcessor(int threads,String procName){
			super();
			this.processorName = procName;
			executorService = Executors.newFixedThreadPool(threads + 1); //extra thread for cmdrun()
			executorService.submit(() -> cmdrun());  // run the service
				
		}

		public void stop() {
			quit = true;
		}
		
		public String addCmd(CMD_TYPE cmd) {
			cmdQueue.add(cmd);
			return "";
		}

		public int queueSize() {
			return cmdQueue.size();
		}
		
		/**
		 * When running state, take commands off the queue and process
		 * 
		 * @return
		 */
		public void cmdrun() {

			// loop until quit = true
			log.info("Starting command queue");		
			while (!quit) {
				// when in running state, process messages				
				try {
					CMD_TYPE cmd = cmdQueue.take();
					executorService.submit( ()-> processMessage(cmd) ); 
				} catch (Exception e) {
					log.error("Problem with "+processorName,e);
				}				
				//Thread.yield();
			}
			shutdown();
			log.info(this.processorName + " command processor stopping");		
			// when not loop
		}
	
}
