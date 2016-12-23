package bo.jiang;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * @author shumpert.jiang
 */
public class SyncContext {
  private final Map<String, SynchronousQueue<Message>> map = new ConcurrentHashMap<String, SynchronousQueue<Message>>();
  private static SyncContext context;

  private SyncContext() {}

  public static SyncContext getInstance() {
    if (context == null) {
      synchronized (SyncContext.class) {
        if (context == null) {
          context = new SyncContext();
        }
      }
    }
    return context;
  }

  public void put(String str, SynchronousQueue<Message> syncQueue) {
    map.put(str, syncQueue);
  }

  public SynchronousQueue<Message> get(String str) {
    return map.get(str);
  }

  public void remove(String str) {
    map.remove(str);
  }
}
