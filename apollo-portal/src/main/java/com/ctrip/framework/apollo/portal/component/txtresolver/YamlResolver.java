package com.ctrip.framework.apollo.portal.component.txtresolver;

import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ItemDTO;
import com.ctrip.framework.apollo.common.utils.BeanUtils;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by kezhenxu at 2019/1/12 14:26
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
@Component("yamlResolver")
public class YamlResolver implements ConfigTextResolver {
  private final YamlPropertySourceLoader yamlPropertySourceLoader;

  public YamlResolver() {
    this.yamlPropertySourceLoader = new YamlPropertySourceLoader();
  }

  @Override
  public ItemChangeSets resolve(final long namespaceId, final String configText, final List<ItemDTO> baseItems) {
    try {
      final List<PropertySource<?>> propertySourceList =
          yamlPropertySourceLoader.load(String.valueOf(namespaceId), new ByteArrayResource(configText.getBytes()));
      if (propertySourceList.isEmpty()) {
        return new ItemChangeSets();
      }
      final ItemChangeSets changeSets = new ItemChangeSets();
      for (final PropertySource<?> propertySource : propertySourceList) {
        final Object source = propertySource.getSource();
        if (!(source instanceof Map)) {
          throw new RuntimeException("Unknown error occurs while parsing YAML/YML configurations.");
        }

        // noinspection unchecked
        final Map<String, Object> properties = (Map<String, Object>) source;

        final Map<String, ItemDTO> oldItemsByKey = BeanUtils.mapByKey("key", baseItems);
        final Set<String> oldKeys = oldItemsByKey.keySet();
        final Set<String> newKeys = properties.keySet();

        for (final Map.Entry<String, Object> entry : properties.entrySet()) {
          final String key = entry.getKey();
          final String value = Objects.toString(entry.getValue(), "");
          if (!oldKeys.contains(key)) { // created
            final ItemDTO createdItem = new ItemDTO(key, value, "", 0);
            createdItem.setNamespaceId(namespaceId);
            changeSets.addCreateItem(createdItem);
          } else if (oldKeys.contains(key)) { // updated
            final ItemDTO existedItem = oldItemsByKey.get(key);
            existedItem.setValue(value);
            changeSets.addUpdateItem(existedItem);
          }
        }

        oldKeys.removeAll(newKeys);
        for (final String oldKey : oldKeys) {
          changeSets.addDeleteItem(oldItemsByKey.get(oldKey));
        }
      }
      return changeSets;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
