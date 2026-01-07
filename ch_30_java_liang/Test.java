public class Test {
    int x;

    public Test(){
        x = 5;
    }

    public static void main(String[] args){
        Test newObj = new Test();
        System.out.println(newObj.x);
    }
}