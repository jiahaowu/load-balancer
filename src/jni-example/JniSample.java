public class JniSample {

    public native int sayHello();

    public static void main(String[] args) {

        System.loadLibrary("JniSample");
        System.out.println("In java main");

        JniSample s = new JniSample();
        int a=s.sayHello();
        
    }
}