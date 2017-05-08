public class JniGo {
   
   static {
      System.loadLibrary("JniBench");
   }
	public native double Bench(int num_rand, int num_repeat);
    
    public static void main(String[] args) {

        int num_rand = 10000000;
        int num_repeat = 12;

        double bench_score = new JniGo().Bench(num_rand,num_repeat);

        System.out.println("Benchmark Score:"+bench_score);
        
    }
}