package org.powertac.weatherserver;

import java.io.IOException;


public class Properties
{
  private static String resourceName = "weatherserver.properties";
  private static java.util.Properties properties = new java.util.Properties();
  private static boolean loaded = false;

  public String getProperty (String key)
  {
    loadIfNecessary();
    return properties.getProperty(key);
  }

  public String getProperty (String key, String defaultValue)
  {
    loadIfNecessary();
    return properties.getProperty(key, defaultValue);
  }

  // lazy loader
  private void loadIfNecessary ()
  {
    if (!loaded) {
      try {
        properties.load(Properties.class.getClassLoader()
            .getResourceAsStream(resourceName));
        loaded = true;

        // Assure proper separator for  flatFileLocation
        String fileSeparator = System.getProperty("file.separator");
        if (properties.getProperty("useFlatFiles", "").equals("true") &&
            !properties.getProperty("flatFileLocation").endsWith(fileSeparator)) {
          properties.put("flatFileLocation",
              properties.getProperty("flatFileLocation") + fileSeparator);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}