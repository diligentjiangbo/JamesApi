package bo.jiang;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shumpert.jiang
 */
public class ServiceContext {
  private final Map<String, Class<? extends IService>> map = new ConcurrentHashMap<String, Class<? extends IService>>();
  private static ServiceContext serviceContext;

  private ServiceContext() {}

  public static ServiceContext getInstance() {
    if (serviceContext == null) {
      synchronized (ServiceContext.class) {
        if (serviceContext == null) {
          serviceContext = new ServiceContext();
        }
      }
    }
    return serviceContext;
  }

  public void put(String str, Class<? extends IService> clazz) {
    map.put(str, clazz);
  }

  public Class get(String str) {
    return map.get(str);
  }

  public void remove(String str) {
    map.remove(str);
  }
}
