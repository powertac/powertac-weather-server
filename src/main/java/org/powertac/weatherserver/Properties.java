package org.powertac.weatherserver;

import java.io.IOException;


public class Properties
{
  private String resourceName = "weatherserver.properties";
  private static java.util.Properties properties = new java.util.Properties();
  private static boolean loaded = false;

  public String getProperty (String key)
  {
    loadIfNecessary();
    return properties.getProperty(key);
  }

  // lazy loader
  private void loadIfNecessary ()
  {
    if (!loaded) {
      try {
        properties.load(Properties.class.getClassLoader()
            .getResourceAsStream(resourceName));
        loaded = true;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}