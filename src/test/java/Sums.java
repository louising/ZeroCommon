import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static java.util.Arrays.asList;

public class Sums {
    
    static class Sum implements Callable<Long> {
        private final long from;
        private final long to;
        
        Sum(long from, long to) {
            this.from = from;
            this.to = to;
        }
        
        @Override
        public Long call() {
            long acc = 0;
            for (long i = from; i <= to; i++) {
                acc = acc + i;
            }
            return acc;
        }                
    }
    
    public static void main(String[] args) throws Exception {
        String[] strs = "Hello Alice, James! How are you".split("(\\s|\\p{Punct})+");
        for (String s: strs) 
            System.out.println(s);
        
        /*
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<Long>> results = executor.invokeAll(asList(
            new Sum(0, 10), new Sum(1, 100), new Sum(10_000, 1_000_000)
        ));
        executor.shutdown(); //Initiates an orderly shutdown in which previously submittedtasks are executed, but no new tasks will be accepted
        
        for (Future<Long> result : results) {
            System.out.println(result.get());
        }
        
        */
    }    
}