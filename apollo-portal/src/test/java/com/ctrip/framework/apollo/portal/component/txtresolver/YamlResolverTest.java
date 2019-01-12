package com.ctrip.framework.apollo.portal.component.txtresolver;

import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ItemDTO;
import com.ctrip.framework.apollo.portal.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;

/**
 * Created by kezhenxu at 2019/1/12 14:34
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
public class YamlResolverTest extends AbstractUnitTest {
  private YamlResolver resolver;

  @Before
  public void setUp() {
    resolver = new YamlResolver();
  }

  @Test
  public void shouldParseSimpleYamlProperly() {
    ItemDTO key1 = new ItemDTO("key1", "value2", "", -1);
    ItemDTO key2 = new ItemDTO("key2", "value2", "", -1);
    ItemDTO key3 = new ItemDTO("key3", "value3", "", -1);

    ItemChangeSets changeSets = resolver.resolve(0, "key1: value1\nkey2: value2", Collections.emptyList());
    Assert.assertThat(changeSets.getCreateItems(), hasSize(2));
    Assert.assertThat(changeSets.getUpdateItems(), hasSize(0));
    Assert.assertThat(changeSets.getDeleteItems(), hasSize(0));

    changeSets = resolver.resolve(0, "key1: value1\nkey2: value2", Collections.singletonList(key1));
    Assert.assertThat(changeSets.getCreateItems(), hasSize(1));
    Assert.assertThat(changeSets.getUpdateItems(), hasSize(1));
    Assert.assertThat(changeSets.getDeleteItems(), hasSize(0));

    changeSets = resolver.resolve(0, "key1: value1\nkey2: value2", Arrays.asList(key1, key2, key3));
    Assert.assertThat(changeSets.getCreateItems(), hasSize(0));
    Assert.assertThat(changeSets.getUpdateItems(), hasSize(2));
    Assert.assertThat(changeSets.getDeleteItems(), hasSize(1));
  }
}
