public class JniSample {
   
   static {
      System.loadLibrary("JniSample");
   }
	public native int sayHello(int num_rand, int num_repeat);
    
    public static void main(String[] args) {

        System.out.println("In java main");
        
        //int a=s.sayHello(7);
        int a = new JniSample().sayHello(10000,3);
        System.out.println(a);

        
    }
}