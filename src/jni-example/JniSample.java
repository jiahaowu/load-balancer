public class JniSample {
   
   static {
      System.loadLibrary("JniSample");
   }
	public native double sayHello(int a);
    
    public static void main(String[] args) {

        System.out.println("In java main");
        
        //int a=s.sayHello(7);
        System.out.println(new JniSample().sayHello(3));
        
    }
}