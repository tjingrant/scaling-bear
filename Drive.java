import java.util.Random;
import java.util.concurrent.Callable;



public class Drive {

	
	public static void main(String[] args) throws InterruptedException {
		
		final LinkedList lsl = new LinkedList();
		lsl.Insert(5);
		System.out.print(lsl.Contains(5));
		lsl.Insert(6);
		lsl.Delete(6);
		System.out.print(lsl.Contains(6));
		lsl.printOut();
//		
//		//lsl.Delete(5);
//		(new Thread() {
//			  public void run() {
//				Random generator = new Random();
//				
//			    for(int i=0; i<5; i++) {
//			    	lsl.Insert(5);
//			    	try {
//			    		Thread.sleep(generator.nextInt(10)*100);
//			    	} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//			    		e.printStackTrace();
//			    	}
//			    	lsl.Delete(5);
//			    	try {
//			    		Thread.sleep(generator.nextInt(10)*150);
//			    	} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//			    		e.printStackTrace();
//			    	}
//			    }
//			  }
//		}).start();
//		execute(lsl, 
//			new Callable<TemporaryResult>() {
//				public TemporaryResult call() {
//					return lsl.Contains(5);
//				}
//			},
//			new Callable<Integer>() {
//				public Integer call() {
//					Random generator = new Random();
//					try {
//						Thread.sleep(generator.nextInt(10)*100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.print("Hey you've got your 5 dollars!\n");
//					return 0;
//			}},
//			 new Callable<Boolean>() {
//				public Boolean call() {
//					System.out.print("Never mind, just kidding!\n");
//				    return true;
//			}});
//		//System.out.print(lsl.Validify(tr));
////	
////		lsl.printOut();
		for (int j=0; j<200; j++) {
			(new Thread() {
				  public void run() {
					Random generator = new Random();
				    for(int i=0; i<5; i++) {
					  try {
						lsl.Insert(generator.nextInt(10));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  try {
						lsl.Delete(5);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					  try {
						lsl.Contains(generator.nextInt(10));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    }
				  }
			}).start();
		}
		
		Thread.sleep(1000*5);
		System.out.print(lsl.invariantCheck());
		lsl.printOut();
////		
////		lsl.hybernate = true;
////		//Destructive Invariant Checker
////		
////		try {
////			Thread.sleep(1000*3);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//		
////		System.out.print(lsl.invariantCheck());
	}

}
