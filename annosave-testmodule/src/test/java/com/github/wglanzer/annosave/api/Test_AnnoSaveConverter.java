package com.github.wglanzer.annosave.api;

import com.github.wglanzer.annosave.api.containers.IMethodContainer;
import com.github.wglanzer.annosave.api.testcontainer.*;
import com.google.common.base.Charsets;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.jetbrains.annotations.Nullable;
import org.junit.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class Test_AnnoSaveConverter
{

  @Test
  public void test_writeReadSimple()
  {
    StringWriter stringWriter = new StringWriter();
    List<IAnnotationContainer> createdContainer = AnnoSave.write(TestVersionContainerImpl.class, new WriterOutputStream(stringWriter, Charsets.UTF_8));
    List<IAnnotationContainer> readContainer = AnnoSave.read(new ReaderInputStream(new StringReader(stringWriter.toString())));
    Assert.assertEquals(createdContainer, readContainer);
  }

  @Test
  public void test_writeReadExtended()
  {
    StringWriter stringWriter = new StringWriter();
    AnnoSave.write(TestVersionContainerImpl.class, new WriterOutputStream(stringWriter, Charsets.UTF_8));
    Map<String, IAnnotationContainer> containers = AnnoSave.readAsMap(new ReaderInputStream(new StringReader(stringWriter.toString())));
    _test_TestVersionContainerImpl(containers.get(TestVersionContainerImpl.class.getName()));
    _test_InnerClass(containers.get(TestVersionContainerImpl.InnerClass.class.getName()));
  }

  @Test
  public void test_writeReadZip() throws URISyntaxException
  {
    File zipFile = new File(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()), "test_writeReadZip.zip");
    IAnnotationContainer[] containersOrig = AnnoSaveGZip.write(new Class[]{TestVersionContainerImpl.class, TestVersionContainerImpl2.class}, zipFile);
    IAnnotationContainer[] containersRead = AnnoSaveGZip.read(zipFile);
    Assert.assertArrayEquals(containersOrig, containersRead);
  }

  @Test
  public void test_writeReadZipInputStream() throws Exception
  {
    File zipFile = new File(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()), "test_writeReadZipStream.zip");
    IAnnotationContainer[] containersOrig = AnnoSaveGZip.write(new Class[]{TestVersionContainerImpl.class, TestVersionContainerImpl2.class}, zipFile);
    IAnnotationContainer[] containersRead = AnnoSaveGZip.read(new FileInputStream(zipFile));
    Assert.assertArrayEquals(containersOrig, containersRead);
  }

  @Test
  public void test_annotationProcessor() throws URISyntaxException
  {
    File annosaveZip = new File(getClass().getResource("/annosave.zip").toURI());
    Map<String, IAnnotationContainer> containers = AnnoSaveGZip.readAsMap(annosaveZip);
    Assert.assertEquals(4, containers.size());
    _test_TestVersionContainerImpl(containers.get(TestVersionContainerImpl.class.getName()));
    _test_InnerClass(containers.get(TestVersionContainerImpl.InnerClass.class.getName()));
  }

  private void _test_InnerClass(IAnnotationContainer pInnerClassRoot)
  {
    Assert.assertNotNull(pInnerClassRoot);
    Assert.assertEquals(TestVersionContainerImpl.InnerClass.class, pInnerClassRoot.getType().getInstance());

    IAnnotationContainer mt_getIntArray = Stream.of(pInnerClassRoot.getChildren()).collect(Collectors.toMap(IAnnotationContainer::getName, pE -> pE)).get("INNER_getIntArray");
    Assert.assertTrue(mt_getIntArray instanceof IMethodContainer); // "INNER_getIntArray" is a method -> MethodContainer
    IType[] methodParameters = ((IMethodContainer) mt_getIntArray).getMethodParameters();
    Assert.assertEquals(1, methodParameters.length);
    Assert.assertEquals(methodParameters[0].getInstance(), String.class);

    IAnnotation[] annotations = mt_getIntArray.getAnnotations();
    Assert.assertEquals(1, annotations.length);
    IAnnotation obsoleteVersionsAnno = annotations[0];
    Assert.assertEquals(ObsoleteVersions.class, obsoleteVersionsAnno.getType().getInstance());
    IAnnotationParameter[] obsoleteVersionsAnnoParameters = obsoleteVersionsAnno.getParameters();
    Assert.assertEquals(1, obsoleteVersionsAnnoParameters.length);
    _assertParameter(obsoleteVersionsAnnoParameters[0], "value", ObsoleteVersion[].class, null);
    IAnnotation[] childAnnotations = (IAnnotation[]) obsoleteVersionsAnnoParameters[0].getValue();
    Assert.assertEquals(2, childAnnotations.length);

    // Annotation with index 0
    Map<String, IAnnotationParameter> anno0ChildParams = _toMap(childAnnotations[0].getParameters());
    Assert.assertEquals(5, anno0ChildParams.size());
    _assertParameter(anno0ChildParams.get("version"), "version", int.class, 0);
    _assertParameter(anno0ChildParams.get("pkgName"), "pkgName", String.class, "inner_obso");
    _assertParameter(anno0ChildParams.get("id"), "id", String.class, "getDoubleArr");
    _assertParameter(anno0ChildParams.get("type"), "type", Class.class, double[].class);
    _assertParameter(anno0ChildParams.get("parameters"), "parameters", Class[].class, null);

    // Annotation with index 1
    Map<String, IAnnotationParameter> anno1ChildParams = _toMap(childAnnotations[1].getParameters());
    Assert.assertEquals(5, anno0ChildParams.size());
    _assertParameter(anno1ChildParams.get("version"), "version", int.class, 1);
    _assertParameter(anno1ChildParams.get("pkgName"), "pkgName", String.class, "");
    _assertParameter(anno1ChildParams.get("id"), "id", String.class, "getIntList");
    _assertParameter(anno1ChildParams.get("type"), "type", Class.class, Void.class);
    _assertParameter(anno1ChildParams.get("parameters"), "parameters", Class[].class, new Class[]{double.class, int[].class, TestVersionContainerImpl.MyClass.class});
  }

  private void _test_TestVersionContainerImpl(IAnnotationContainer pContainer)
  {
    Assert.assertNotNull(pContainer);

    // Check Root-Annotation
    IAnnotation[] rootAnnotations = pContainer.getAnnotations();
    Assert.assertEquals(1, rootAnnotations.length);
    IAnnotation obsoleteVersionContainerRoot = rootAnnotations[0];
    Assert.assertEquals(ObsoleteVersionContainer.class, obsoleteVersionContainerRoot.getType().getInstance());
    IAnnotationParameter[] obsoleteVersionContainerRootParameters = obsoleteVersionContainerRoot.getParameters();
    Assert.assertEquals(3, obsoleteVersionContainerRootParameters.length);
    Map<String, IAnnotationParameter> params = _toMap(obsoleteVersionContainerRootParameters);
    _assertParameter(params.get("pkgName"), "pkgName", String.class, "container");
    _assertParameter(params.get("category"), "category", String.class, "js");

    // Check "getIntArray"-Method
    IAnnotationContainer mt_getIntArray = Stream.of(pContainer.getChildren()).collect(Collectors.toMap(IAnnotationContainer::getName, pE -> pE)).get("getIntArray");
    IAnnotation[] annotations = mt_getIntArray.getAnnotations();
    Assert.assertEquals(1, annotations.length);
    IAnnotation obsoleteVersionsAnno = annotations[0];
    Assert.assertEquals(ObsoleteVersions.class, obsoleteVersionsAnno.getType().getInstance());
    IAnnotationParameter[] obsoleteVersionsAnnoParameters = obsoleteVersionsAnno.getParameters();
    Assert.assertEquals(1, obsoleteVersionsAnnoParameters.length);
    _assertParameter(obsoleteVersionsAnnoParameters[0], "value", ObsoleteVersion[].class, null);
    IAnnotation[] childAnnotations = (IAnnotation[]) obsoleteVersionsAnnoParameters[0].getValue();
    Assert.assertEquals(2, childAnnotations.length);

    // Annotation with index 0
    Map<String, IAnnotationParameter> anno0ChildParams = _toMap(childAnnotations[0].getParameters());
    Assert.assertEquals(5, anno0ChildParams.size());
    _assertParameter(anno0ChildParams.get("version"), "version", int.class, 0);
    _assertParameter(anno0ChildParams.get("pkgName"), "pkgName", String.class, "obso");
    _assertParameter(anno0ChildParams.get("id"), "id", String.class, "getDoubleArr");
    _assertParameter(anno0ChildParams.get("type"), "type", Class.class, double[].class);
    _assertParameter(anno0ChildParams.get("parameters"), "parameters", Class[].class, null);

    // Annotation with index 1
    Map<String, IAnnotationParameter> anno1ChildParams = _toMap(childAnnotations[1].getParameters());
    Assert.assertEquals(5, anno0ChildParams.size());
    _assertParameter(anno1ChildParams.get("version"), "version", int.class, 1);
    _assertParameter(anno1ChildParams.get("pkgName"), "pkgName", String.class, "");
    _assertParameter(anno1ChildParams.get("id"), "id", String.class, "getIntList");
    _assertParameter(anno1ChildParams.get("type"), "type", Class.class, Void.class);
    _assertParameter(anno1ChildParams.get("parameters"), "parameters", Class[].class, new Class[]{double.class, int[].class});
  }

  private Map<String, IAnnotationParameter> _toMap(IAnnotationParameter[] pParameters)
  {
    return Arrays.stream(pParameters).collect(Collectors.toMap(IAnnotationParameter::getName, pParam -> pParam));
  }

  private void _assertParameter(IAnnotationParameter pParameter, String pName, Class<?> pType, @Nullable Object pValue)
  {
    Assert.assertEquals(pName, pParameter.getName());
    Assert.assertEquals(pType, pParameter.getType().getInstance());
    if(pValue != null)
    {
      if(pValue.getClass().isArray())
        Assert.assertArrayEquals((Object[]) pValue, (Object[]) pParameter.getValue());
      else
        Assert.assertEquals(pValue, pParameter.getValue());
    }
  }

}
