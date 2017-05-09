public class JniGo {
   
   static {
      System.loadLibrary("JniSample");
   }
	public native int Monte(int num_rand, int num_repeat);
    
    public static void main(String[] args) {

        int num_rand = 30000000;
        int num_repeat = 1;

        int count_total = new JniGo().Monte(num_rand,num_repeat);

        double pi_result = count_total /(double) num_rand / num_repeat * 4 ;

        System.out.println("Total Random Number:"+num_rand);
        System.out.println("Repeated:"+num_repeat);
        System.out.println("Calculated PI:"+pi_result);
        
    }
}