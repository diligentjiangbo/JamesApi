package bo.jiang;

/**
 * @author shumpert.jiang
 */
public class RegisterExample {
  public static void main(String[] args) {
    registerServiceDemo();
  }

  private static void registerServiceDemo() {
    JamesClient jamesClient = new JamesClient();
    jamesClient.connect("localhost", 6789);
    Service service = new Service("12345", MyService.class);
    try {
      boolean flag = jamesClient.registerService(service);
      if (flag) {
        System.out.println("服务注册成功");
      } else {
        System.out.println("服务注册失败");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }



}
