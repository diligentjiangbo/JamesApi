package bo.jiang;

/**
 * @author shumpert.jiang
 */
public class SendRequestExample {
  public static void main(String[] args) {
    try {
      sendRequestDemo();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  private static void sendRequestDemo() throws InterruptedException {
    JamesClient jamesClient = new JamesClient();
    jamesClient.connect("localhost", 6789);
    String str = "Hello World";
    Message message = new Message();
    message.setBody(str.getBytes());
    message.getHeader().setServiceId("12345");

    Message returnMsg = jamesClient.sendRequest(message);

    String str2 = new String(returnMsg.getBody());
    System.out.println(str2);
  }
}
