package bo.jiang;

/**
 * @author shumpert.jiang
 */
public class MyService implements IService {
  public Message execute(Message message) throws Exception {
    System.out.println("Hello World");
    return message;
  }
}
