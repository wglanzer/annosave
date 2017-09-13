package com.github.wglanzer.annosave.api.testcontainer;

/**
 * @author W.Glanzer, 11.09.2017
 */
@SuppressWarnings("unused")
@ObsoleteVersionContainer(category = "js", pkgName = "container")
public class TestVersionContainerImpl
{
  @ObsoleteVersions({
      @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE", type = int.class)
  })
  public final static String CLASSIFICATION_PUBLIC = "asdf";

  @ObsoleteVersions({
      @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE", type = String.class)
  })
  public final static String CLASSIFICATION_PUBLIC_STRING = "asdf";

  @ObsoleteVersions({
      @ObsoleteVersion(version = 0, pkgName = "obso", id = "getDoubleArr", type = double[].class),
      @ObsoleteVersion(version = 1, id = "getIntList", parameters = {double.class, int[].class})
  })
  public int[] getIntArray(String pParam)
  {
    return new int[0];
  }

  @ObsoleteVersionContainer(category = "js", pkgName = "inner_container")
  public static class InnerClass
  {

    @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE", type = int.class)
    public final static String INNER_CLASSIFICATION_PUBLIC = "asdf";

    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE", type = String.class)
    })
    public final static String INNER_CLASSIFICATION_PUBLIC_STRING = "asdf";

    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, pkgName = "inner_obso", id = "getDoubleArr", type = double[].class),
        @ObsoleteVersion(version = 1, id = "getIntList", parameters = {double.class, int[].class})
    })
    public int[] INNER_getIntArray(String pParam)
    {
      return new int[0];
    }

  }

}
