public class JniGo {
   
   static {
      System.loadLibrary("JniSample");
   }
	public native int Monte(int num_rand, int num_repeat);
  public native double Bench(int n);
    
    public static void main(String[] args) {

        int num_rand = 10000;
        int num_repeat = 3;

        int count_total = new JniGo().Monte(num_rand,num_repeat);
        double bench_result = new JniGo().Bench(1000);

        double pi_result = count_total /(double) num_rand / num_repeat * 4 ;

        System.out.println("Bench_result:"+bench_result);
        System.out.println("Total Random Number:"+num_rand);
        System.out.println("Repeated:"+num_repeat);
        System.out.println("Calculated PI:"+pi_result);
        
    }
}